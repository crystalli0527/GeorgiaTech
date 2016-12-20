package edu.gatech.cs6310.scheduler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.gatech.cs6310.services.RequestService;

public class University {
	private static Logger log = Logger.getLogger(RequestService.class.getName());
	
	public String name;
	private Catalog catalog;
	private Policy policy;
	private HashMap<String, Student> students;
	public Semester semesterStart;
	public int yearStart;
	
	/**
	 * University Constructor
	 */
	public University(String name) {
		this.name = name;
		this.catalog = new Catalog();
		this.policy = new Policy();
		this.students = new HashMap<String, Student>();
		this.semesterStart = Semester.FALL;
		this.yearStart = 2016;
	}

	public Catalog getCatalog() {
		return catalog;
	}

	public Policy getPolicy() {
		return policy;
	}

	public HashMap<String, Student> getStudents() {
		return students;
	}

	public void setMaxCourseSemPolicy(int max) {
		this.policy.setMaxCoursesSem(max);
	}
	
	public void setTotalSemAvailablePolicy(int sem) {
		this.policy.setTotalSemAvailable(sem);
	}
	
	// Expects JSON object with the following structure:
	/*
	{
		"courseTitles" : [
		   {
		      "courseId" : "1",
		      "courseName" : "class",
		      "maxSize" : "3",
		      "isOfferedSpring" : true,
		      "isOfferedSummer" : false,
		      "isOfferedFall" : true,
		      "titlePreReqs" : [
		   	     {
		      	    "preReq" : "1",
		      		"forTitleId" : "2"
		   		 }
		   	  ],
		   	  "policies" :
		   		 {
		      		"maxCoursesPerSem" : "3"
		   		 }
		   }
		]
	}
	*/
	public void createCourseCatalog(JSONObject jsonObj) throws IOException {

		JSONArray ja = jsonObj.getJSONArray("courseTitles");
		for (int i = 0; i < ja.length(); i++) {
			
			String courseId = ja.getJSONObject(i).getString("courseId");
			String courseName = ja.getJSONObject(i).getString("courseName");
			String courseMaxSize = ja.getJSONObject(i).getString("maxSize");
			Boolean isOfferedSpring = ja.getJSONObject(i).getBoolean("isOfferedSpring");
			Boolean isOfferedSummer = ja.getJSONObject(i).getBoolean("isOfferedSummer");
			Boolean isOfferedFall = ja.getJSONObject(i).getBoolean("isOfferedFall");
			
			log.info("Adding course: " + courseName + ", ID: " + courseId + "; maxSize: " + courseMaxSize);
			Title title = new Title(Integer.parseInt(courseId), courseName,Integer.parseInt(courseMaxSize));
			title.isOfferedSpring = isOfferedSpring;
			title.isOfferedSummer = isOfferedSummer;
			title.isOfferedFall = isOfferedFall;
			
			this.catalog.addCourseTitle(title);
			
			
		}
	}
	
	public void createCourseCatalog(File file) throws IOException {
		FileInputStream fs = new FileInputStream(file);
		JSONObject jo = new JSONObject();
		JSONArray jsonArr = new JSONArray();
		
		BufferedReader bReader = new BufferedReader(new InputStreamReader(fs));
	 
		String line = null;
		// Read the first row
		line = bReader.readLine();
		
		while ((line = bReader.readLine()) != null) {
			JSONObject courseTitle = new JSONObject();
			
			String[] tokens = line.split(",", -1);
			String courseId = tokens[0];
			String courseName = tokens[1];
			String courseMaxSize = tokens[2];
			
			courseTitle.put("courseId", courseId);
			courseTitle.put("courseName", courseName);
			courseTitle.put("maxSize", courseMaxSize);
			
			jsonArr.put(courseTitle);
		}
		bReader.close();
		jo.put("courseTitles", jsonArr);
		JSONArray ja = jo.getJSONArray("courseTitles");
		for (int i = 0; i < ja.length(); i++) {
			String courseId = ja.getJSONObject(i).getString("courseId");
			String courseName = ja.getJSONObject(i).getString("courseName");
			String courseMaxSize = ja.getJSONObject(i).getString("maxSize");
		
			Title title = new Title(Integer.parseInt(courseId), courseName,Integer.parseInt(courseMaxSize));
			this.catalog.addCourseTitle(title);
		}
	}
	
	// Expects JSON object with the following structure:
	/*
	{
		"policies" :
		   {
		      "maxCoursesPerSem" : "3"
		   }
	}
	*/
	public void editPolicies(JSONObject jsonObj) {
		JSONObject policies = jsonObj.getJSONObject("policies");
		String maxCoursesPerSem = policies.getString("maxCoursesPerSem");
		
		log.info("Setting Max Courses for a Semester at: " + maxCoursesPerSem);
		this.policy.setMaxCoursesSem(Integer.parseInt(maxCoursesPerSem));
		this.policy.setTotalCoursesAvailable(this.catalog.courseTitles.size());
	}
	
	
	// Expects JSON object with the following structure:
	/*
	{
		"titlePreReqs" : [
		   {
		      "preReq" : "1",
		      "forTitleId" : "2"
		   }
		]
	}
	*/
	public void editTitlePreReq(JSONObject jsonObj) throws IOException {
		JSONArray ja = jsonObj.getJSONArray("titlePreReqs");
		log.info("Got this JSONArray for title preReqs: " + ja.toString());
		
		for (int i = 0; i < ja.length(); i++) {
			String titleId = ja.getJSONObject(i).getString("forTitleId");
			String preReq = ja.getJSONObject(i).getString("preReq");
			
			log.info("Trying to add PreReq CourseId: " + preReq + ", to CourseId: " + titleId);
			this.catalog.courseTitles.get(titleId).addPreReq(this.catalog.courseTitles.get(preReq));
		}
	}
	
	public void editTitlePreReq(File file) throws IOException {
		FileInputStream fs = new FileInputStream(file);
		JSONObject jo = new JSONObject();
		JSONArray jsonArr = new JSONArray();
		
		BufferedReader bReader = new BufferedReader(new InputStreamReader(fs));
	 
		String line = null;
		// Read the first row
		line = bReader.readLine();
		
		while ((line = bReader.readLine()) != null) {
			JSONObject preReqEntry = new JSONObject();
			
			String[] tokens = line.split(",", -1);
			String preReq = tokens[0];
			String titleId = tokens[1];
			
			preReqEntry.put("forTitleId", titleId);
			preReqEntry.put("preReq", preReq);
			
			jsonArr.put(preReqEntry);
		}
		bReader.close();
		
		jo.put("titlePreReqs", jsonArr);
		editTitlePreReq(jo);
	}

	public void editTitleAvailability(File file) throws IOException {
		FileInputStream fs = new FileInputStream(file);
		 
		BufferedReader bReader = new BufferedReader(new InputStreamReader(fs));
	 
		String line = null;
		// Read the first row
		line = bReader.readLine();
		
		while ((line = bReader.readLine()) != null) {
			String[] tokens = line.split(",", -1);
			String titleId = tokens[0];
			Semester sem = Semester.getSemester(Integer.parseInt(tokens[1]));
			
			Title title = this.catalog.courseTitles.get(titleId);
			if (sem == Semester.FALL) {
				title.isOfferedFall = true;
			}
			else if (sem == Semester.SPRING) {
				title.isOfferedSpring = true;
			}
			else {
				title.isOfferedSummer = true;
			}
		}
		bReader.close();
	}
	
	/*
	{
		"studentInterests" : [
		   {
		      "studentId" : "1",
		      "yearsEnrolled" : "3",
		      "courses" : [
		   	     {"id":"1"},{"id":"2"},{"id":"3"}
		   	  ]
		   }
		]
	}
	*/
	public void processStudentInterests(JSONObject jsonObj) throws IOException {
		
		HashMap<String, List<Title>> studentsPreReqsToAdd = new HashMap<String, List<Title>>();
		
		JSONArray ja = jsonObj.getJSONArray("studentInterests");		
		for (int i = 0; i < ja.length(); i++) {
			String studentId = ja.getJSONObject(i).getString("studentId");
			String studentYearsEnrolled = ja.getJSONObject(i).getString("yearsEnrolled");
			
			// Create the Student if it isn't found in the University
			if (!this.students.containsKey(studentId)) {
				Student student = new Student(Integer.parseInt(studentId), Integer.parseInt(studentYearsEnrolled));
				students.put(studentId, student);
			}
						
			for (int j = 0; j < ja.getJSONObject(i).getJSONArray("courses").length(); j++) {
				String titleId = ja.getJSONObject(i).getJSONArray("courses").getJSONObject(j).getString("id");				
				Title title = this.catalog.courseTitles.get(titleId);
			
				// Keep track of any possible missing prereqs
				if (!studentsPreReqsToAdd.containsKey(studentId)) {
					studentsPreReqsToAdd.put(studentId, new ArrayList<Title>());
				}
				else {
					// Remove the title from the list of missing prereqs
					studentsPreReqsToAdd.get(studentId).remove(title);
				}
				
				// Add the Interest to the student
				this.students.get(studentId).addInterest(title);
			
				// Increment a list of prereqs from adding this interest
				List<Integer> ancestorPreReqs = new ArrayList<Integer>();
				ancestorPreReqs = Title.getAncestorPreReqs(title);
				for (int k : ancestorPreReqs) {
					if (!this.students.get(studentId).getInterestedTitleIds().contains(k)
							&& !studentsPreReqsToAdd.get(studentId).
							contains(this.catalog.courseTitles.get(String.valueOf(k)))) {
						studentsPreReqsToAdd.get(studentId).add(this.catalog.courseTitles.get(String.valueOf(k)));
					}
				}
			}	
		}
		
		// Go through the prereqs to add
		for (Entry<String, List<Title>> e : studentsPreReqsToAdd.entrySet()) {
			String studentId = e.getKey();
			for (Title t : e.getValue()) {
				this.students.get(studentId).addInterest(t,true);
			}
		}
	}
	
	public void processStudentInterests(File file) throws IOException {
		FileInputStream fs = new FileInputStream(file);
		BufferedReader bReader = new BufferedReader(new InputStreamReader(fs));
		JSONObject jo = new JSONObject();
		JSONArray jsonArr = new JSONArray();

		
		String line = null;
		// Read the first row
		line = bReader.readLine();
		
		while ((line = bReader.readLine()) != null) {
			JSONObject studentEntry = new JSONObject();
			JSONObject course = new JSONObject();
			JSONArray courses = new JSONArray();
			
			String[] tokens = line.split(",", -1);
			String studentId = tokens[0];
			String titleId = tokens[1];
			String studentYearsEnrolled = tokens[2];
			
			course.put("id",titleId);
			courses.put(course);
			studentEntry.put("studentId",studentId);
			studentEntry.put("yearsEnrolled",studentYearsEnrolled);
			studentEntry.put("courses",courses);
			
			jsonArr.put(studentEntry);
		}
		bReader.close();
		jo.put("studentInterests", jsonArr);
		
		processStudentInterests(jo);
	}
}
