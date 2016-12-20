package edu.gatech.cs6310.scheduler;

import edu.gatech.cs6310.services.RequestService;
import gurobi.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;


public class Constraint {
	private static Logger log = Logger.getLogger(RequestService.class.getName());
	
	public GRBVar[][][] gurobiConstraint;
	public GRBVar X;
	public GRBVar Y;
	public GRBEnv env;
	public GRBModel model;
	public List<Integer> fallSemesterIndexes = new ArrayList<Integer>();
	public List<Integer> springSemesterIndexes = new ArrayList<Integer>();
	public List<Integer> summerSemesterIndexes = new ArrayList<Integer>();
	public int maxCoursesAllowedPerSem;
	public Policy policyConstraint;
	public HashMap<String,Student> studentsConstraint;
	public HashMap<String, Title> titles;

	/**
	 * Constraint Constructor
	 * @throws GRBException 
	 */
	public Constraint(Policy policy, HashMap<String,Student> students, Semester start, HashMap<String, Title> titleList) throws GRBException {
		policyConstraint = policy;
		studentsConstraint = students;
		titles = titleList;
		
		int numStudents = studentsConstraint.size();
		int numSemesters = policyConstraint.getTotalSemAvailable();
		int numCourses = policyConstraint.getTotalCoursesAvailable();

		maxCoursesAllowedPerSem = policyConstraint.getMaxCoursesSem();
		env = new GRBEnv();
		model = new GRBModel(env);
		model.getEnv().set(GRB.IntParam.OutputFlag, 0);
		model.set(GRB.StringAttr.ModelName, "ARMS");
		if (start == Semester.FALL) {
			for(int i = 0; i < numSemesters; i = i+3) {
				fallSemesterIndexes.add(i);
			}
			for(int i = 1; i < numSemesters; i = i+3) {
				springSemesterIndexes.add(i);
			}
			for(int i = 2; i < numSemesters; i = i+3) {
				summerSemesterIndexes.add(i);
			}
		}
		else if (start == Semester.SPRING) {
			for(int i = 2; i < numSemesters; i = i+3) {
				fallSemesterIndexes.add(i);
			}
			for(int i = 0; i < numSemesters; i = i+3) {
				springSemesterIndexes.add(i);
			}
			for(int i = 1; i < numSemesters; i = i+3) {
				summerSemesterIndexes.add(i);
			}
		}
		else {
			for(int i = 1; i < numSemesters; i = i+3) {
				fallSemesterIndexes.add(i);
			}
			for(int i = 2; i < numSemesters; i = i+3) {
				springSemesterIndexes.add(i);
			}
			for(int i = 0; i < numSemesters; i = i+3) {
				summerSemesterIndexes.add(i);
			}
		}
		
		gurobiConstraint = 
				new GRBVar [numStudents][numCourses][numSemesters];
		for (int i=0; i < numStudents; i++) {
			for (int j = 0; j < numCourses; j++) {
				gurobiConstraint[i][j] = model.addVars(numSemesters, GRB.BINARY);
			}
		}
		X = model.addVar(0, GRB.INFINITY, 0, GRB.INTEGER, "X");
		Y = model.addVar(0, GRB.INFINITY, 0, GRB.INTEGER, "Y");
		
		model.update();
	}
	
	public JSONObject generateSolution() throws GRBException {
		
		generateMaxCoursesSemConstraint();
		generateMaxCourseClassSizeConstraint();
		generateMustTakeCourseConstraint();
		generatePrerequisiteConstraint();
			
		// Create the constraint for minimizing the largest class
		for (int j = 0; j < policyConstraint.getTotalCoursesAvailable(); j++) {
			for (int k = 0; k < policyConstraint.getTotalSemAvailable(); k++) {
				GRBLinExpr minMaxClassSize = new GRBLinExpr();
				for (int i = 0; i < studentsConstraint.size(); i++) {
					minMaxClassSize.addTerm(1,gurobiConstraint[i][j][k]);
				}
				String cName = "NUMSTUDENTS_COURSE:" + j + "_SEMESTER:" + k;
				model.addConstr(minMaxClassSize, GRB.LESS_EQUAL, X, cName);
			}
		}
		
		// Create constraint for maximizing the senior students to get
		// their choices in the first semester
		/*
		for (int j = 0; j < policyConstraint.getTotalCoursesAvailable(); j++) {
			GRBLinExpr maxSeniorFirstSemester = new GRBLinExpr();
			for (int i = 0; i < studentsConstraint.size(); i++) {
				maxSeniorFirstSemester.addTerm((studentsConstraint.get(Integer.toString(i+1))).yearsEnrolled, gurobiConstraint[i][j][0]);
				System.out.println("StudentID: " + (studentsConstraint.get(Integer.toString(i+1))).studentId + "; is years: " + (studentsConstraint.get(Integer.toString(i+1))).yearsEnrolled);
			}
			String cName = "NUMSTUDENTS_COURSE:" + j + "_SEMESTER0 maximizing";
			model.addConstr(maxSeniorFirstSemester, GRB.GREATER_EQUAL, Y, cName);
		}
		*/
		GRBLinExpr objectiveFunction = new GRBLinExpr();
		objectiveFunction.addTerm(1, X);
		model.setObjective(objectiveFunction, GRB.MINIMIZE);
		
		//objectiveFunction.addTerm(1, Y);
		//model.setObjective(objectiveFunction, GRB.MAXIMIZE);
		/*
		GRBLinExpr objectiveFunction2 = new GRBLinExpr();
		objectiveFunction2.addTerm(1, Y);
		model.setObjective(objectiveFunction2, GRB.MAXIMIZE);
		*/
		model.optimize();

		JSONObject scheduleRes = printSolution(model, gurobiConstraint);
		
		// Dispose of model and environment
		model.dispose();
		env.dispose();
		
		return scheduleRes;
	}
	
	// Should return a JSONObject with the following structure:
	// If no feasible schedule was determined... returns null
	/*
	 * { 
	 *    "studentResults" : [
	 *      {
	 *         "studentId" : "12345",
	 *         "courses" : [
	 *            {
	 *               "courseId" : "1",
	 *               "semester" : "1"
	 *            }
	 *         ]
	 *      }
	 *    ]
	 * }
	 */
	private JSONObject printSolution(GRBModel model, GRBVar[][][] results) throws GRBException {
		
		if(model.get(GRB.IntAttr.Status) == GRB.Status.OPTIMAL) {
			//double solution = model.get(GRB.DoubleAttr.ObjVal);
			//System.out.println("X=" + String.format("%.2f",solution));
			JSONObject jsonResult = new JSONObject();
			JSONArray students = new JSONArray();
			
			for (int i = 0; i < studentsConstraint.size(); i++) {
				JSONObject student = new JSONObject();
				JSONArray courses = new JSONArray();
	
				for (int j = 0; j < policyConstraint.getTotalCoursesAvailable(); j++) {
					for (int k = 0; k < policyConstraint.getTotalSemAvailable(); k++) {
						
						Boolean hasStudentBeenAdded = false;
						if (results[i][j][k].get(GRB.DoubleAttr.X) > 0) {
							System.out.println("Student: " + (i+1) + "; CourseNum: " + (j+1) + "; SemesterNum: " + (k+1));
							if (!hasStudentBeenAdded) {
								student.put("studentId", i+1);
								hasStudentBeenAdded = true;
							}
							JSONObject course = new JSONObject();
							course.put("courseId", j+1);
							course.put("semester", k+1);
							courses.put(course);
						}
					}
				}
				if (courses.length() > 0) {
					student.put("courses", courses);
				}
				students.put(student);
			}
			
			jsonResult.put("studentResults", students);
			//System.out.println("------------");
			//System.out.println(jsonResult.toString());
			return jsonResult;
		}
		
		else {
			//System.out.println("No Solution was found");	
			return null;
		}
	}
	
	private void generateMaxCourseClassSizeConstraint() throws GRBException {
		log.info("Generating Max Course Class Size Constraint...");
		// Gotten from the instructions
		for (int j = 0; j < policyConstraint.getTotalCoursesAvailable(); j++) {
			for (int k = 0; k < policyConstraint.getTotalSemAvailable(); k++) {
				GRBLinExpr maxCoursesClassSizeConstraint = new GRBLinExpr();
				for (int i = 0; i < studentsConstraint.size(); i++) {
					maxCoursesClassSizeConstraint.addTerm(1, gurobiConstraint[i][j][k]);
				}
				int maxCourseClassSize = titles.get(Integer.toString(j+1)).maxClassSize;
				
				String cName = "MAXCOURSE_" + (j+1) + "_SEMESTER" + k + "_IS_" + maxCourseClassSize;
				try {
					model.addConstr(maxCoursesClassSizeConstraint, GRB.LESS_EQUAL, maxCourseClassSize, cName);		
				}
				catch(Exception e) {
					System.out.println(e.toString());
				}
			}
		}
	}

	private void generateMaxCoursesSemConstraint() throws GRBException {
		
		log.info("Generating Max Course per Semester Constraint...");
		// Gotten from the instructions
		for (int i = 0; i < studentsConstraint.size(); i++) {
			for (int k = 0; k < policyConstraint.getTotalSemAvailable(); k++) {
				GRBLinExpr maxCoursesConstraint = new GRBLinExpr();
				for (int j = 0; j < policyConstraint.getTotalCoursesAvailable(); j++) {
					maxCoursesConstraint.addTerm(1, gurobiConstraint[i][j][k]);
				}
				String cName = "MAXCOURSES_Student" + i + "_SEMESTER" + k;
				try {
					model.addConstr(maxCoursesConstraint, GRB.LESS_EQUAL, maxCoursesAllowedPerSem, cName);		
				}
				catch(Exception e) {
					System.out.println(e.toString());
				}
			}
		}
	}
	
	private void generateMustTakeCourseConstraint() throws GRBException {
		// Constraint formula gotten from instructions
		log.info("Generate Must Take Constraints...");
		
		// Go through the list of Students, and for each one, go through
		// each StudentInterest, and for each one create the constraint
		// Additionally, also create constraint for when that course is offered
		for (Student s : studentsConstraint.values()) {
			for (StudentInterest st : s.interests) {
				GRBLinExpr mustTakeConstraint = new GRBLinExpr();
				GRBLinExpr courseAvailConstraint = new GRBLinExpr();
				String cAvailNameEnding = "";
				if (!st.title.isOfferedFall) {
					cAvailNameEnding = cAvailNameEnding + "FALL;";
				}
				if (!st.title.isOfferedSpring) {
					cAvailNameEnding = cAvailNameEnding + "SPRING;";
				}
				if (!st.title.isOfferedSummer) {
					cAvailNameEnding = cAvailNameEnding + "SUMMER;";
				}
				for (int k = 0; k < policyConstraint.getTotalSemAvailable(); k++) {
					mustTakeConstraint.addTerm(
							1, gurobiConstraint[s.studentId-1][st.title.courseId-1][k]);
					
					if(!st.title.isOfferedFall && fallSemesterIndexes.contains(k)) {
						courseAvailConstraint.addTerm(1, gurobiConstraint[s.studentId-1][st.title.courseId-1][k]);
					}
					if(!st.title.isOfferedSpring && springSemesterIndexes.contains(k)) {
						courseAvailConstraint.addTerm(1, gurobiConstraint[s.studentId-1][st.title.courseId-1][k]);
					}
					if(!st.title.isOfferedSummer && summerSemesterIndexes.contains(k)) {
						courseAvailConstraint.addTerm(1, gurobiConstraint[s.studentId-1][st.title.courseId-1][k]);
					}
				}
				String cName = "Student:" + s.studentId + "_MUSTTAKECOURSE:" + st.title.courseId;
				String cAvailName = "Student:" + s.studentId + "_CANTTAKECOURSE:" + st.title.courseId + "_SEMESTERS:" + cAvailNameEnding;
				model.addConstr(mustTakeConstraint, GRB.EQUAL, 1, cName);
				model.addConstr(courseAvailConstraint, GRB.EQUAL, 0, cAvailName);
			}
		}
	}
	
	private void generatePrerequisiteConstraint() throws GRBException {
		// Constraint formula gotten from instructions
		log.info("Generating Prerequisite Constraints...");
		
		// Go through the list of Students, and for each one, go through
		// each StudentInterest, 
		// For each Interest, if the Course has a Prerequiste, 
		//   then create a create a constraint for each one
		for (Student s : studentsConstraint.values()) {
			for (StudentInterest st : s.interests) {
				if (st.title.preReqs != null && st.title.preReqs.size() > 0) {
					for (Title preReq : st.title.preReqs) {
						GRBLinExpr preRequisiteConstraint = new GRBLinExpr();
						for (int k = 0; k < policyConstraint.getTotalSemAvailable(); k++) {
							preRequisiteConstraint.addTerm(
									(-1*(k+1)), gurobiConstraint[s.studentId-1][st.title.courseId-1][k]);
							preRequisiteConstraint.addTerm(
									k+1, gurobiConstraint[s.studentId-1][preReq.courseId-1][k]);
						}
						String cName = "Student:" + s.studentId + "_COURSE:" + st.title.courseId + "_PREREQ:" + preReq.courseId;
						model.addConstr(preRequisiteConstraint, GRB.LESS_EQUAL, -1, cName);	
					}
				}
			}
		}
	}
}

