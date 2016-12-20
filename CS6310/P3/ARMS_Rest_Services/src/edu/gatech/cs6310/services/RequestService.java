/**
 * 
 */
package edu.gatech.cs6310.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import edu.gatech.cs6310.datatier.BaseDataAccess;
import edu.gatech.cs6310.datatier.Course;
import edu.gatech.cs6310.datatier.Prerequisites;
import edu.gatech.cs6310.datatier.Requests;
import edu.gatech.cs6310.datatier.Requests.CoursesRequestedByStudents;
import edu.gatech.cs6310.datatier.Student;
import edu.gatech.cs6310.scheduler.SchedulerEngine;
import gurobi.GRBException;

/**
 * @author ubuntu
 * 
 */
@Path("/RequestService")
@JsonIgnoreProperties
public class RequestService {

	private static Logger log = Logger
			.getLogger(RequestService.class.getName());
	
	// The following classes translate the Service Engine work to the data tier work.
	public static class CourseResult {
		@JsonCreator
		public CourseResult() {
			courseId = new Integer(-1);
			semester = new Integer(-1);
		}
		public Integer courseId;
		public Integer semester;
		public Integer getCourseId() {
			return courseId;
		}
		public void setCourseId(Integer courseId) {
			this.courseId = courseId;
		}
		public Integer getSemester() {
			return semester;
		}
		public void setSemester(Integer semester) {
			this.semester = semester;
		}
		
		
	}
	
	public static class StudentResult {
		@JsonCreator
		public StudentResult() {
			studentId = new Integer(-1);
			courses = new ArrayList<CourseResult>();
		}
		public Integer studentId;
		public ArrayList<CourseResult> courses;
		public Integer getStudentId() {
			return studentId;
		}
		public void setStudentId(Integer studentId) {
			this.studentId = studentId;
		}
		public ArrayList<CourseResult> getCourses() {
			return courses;
		}
		public void setCourses(ArrayList<CourseResult> courses) {
			this.courses = courses;
		}
		
	}
	
	@JsonIgnoreProperties
	public static class StudentResults {
		@JsonCreator
		public StudentResults() {
			studentResults = new ArrayList<StudentResult>();
		}
		public ArrayList<StudentResult> studentResults;
		public ArrayList<StudentResult> getStudentResults() {
			return studentResults;
		}
		public void setStudentResults(ArrayList<StudentResult> studentResults) {
			this.studentResults = studentResults;
		}
		
		@JsonIgnoreProperties
		public static StudentResults fromJSONtoStudentResults( String jsonString ) {

			StudentResults aSetOfResults = new StudentResults();
			try {
				ObjectMapper om = new ObjectMapper();
				aSetOfResults = om.readValue(jsonString, StudentResults.class );
			} catch (Exception e) {
				log.severe("Failed to map JSON String to StudentResult " + jsonString);
				log.severe("Exception is " + e.getLocalizedMessage());
			}
			return aSetOfResults;
			
		}

	}

	public class ShortCourse {
		public String id = "";		
	}

	public class StudentRequest {
		public String studentId = "";
		public String yearsEnrolled = "";
		public ArrayList<ShortCourse> courses = new ArrayList<ShortCourse>();		
	}

	public class ShortTitle {
		public String courseId = "";
		public String courseName = "";
		public String maxSize = "";
		public Boolean isOfferedSpring = false;
		public Boolean isOfferedSummer = false;
		public Boolean isOfferedFall = false;
	}
	
	public class ShortPrerequisite {
		public String preReq = "";
		public String forTitleId = "";
	}
	
	public class Policies {
		public String maxCoursesPerSem = "3";
	}
	
	public class ScheduleEngineInput {

		public ArrayList<ShortTitle> courseTitles = new ArrayList<ShortTitle>();
		public ArrayList<StudentRequest> studentInterests = new ArrayList<StudentRequest>();
		public ArrayList<ShortPrerequisite> titlePreReqs = new ArrayList<ShortPrerequisite>();
		public Policies policies = new Policies();	
	}

	// The following are tool functions to convert from and to json for the various objects in this class
	
	private String toJSON(ScheduleEngineInput aRequest) {
		String json = "If you see this you have trouble";
		try {
			ObjectMapper om = new ObjectMapper();
			om.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
			json = om.writeValueAsString(aRequest);
		} catch (Exception e) {
			log.severe("Failed to write ScheduleEngineInput as JSON");
			log.severe(e.getLocalizedMessage());
		}
		return json;
	}

	private  String toJSON(Requests aRequest) {
		String json = "If you see this you have trouble";
		try {
			ObjectMapper om = new ObjectMapper();
			om.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
			json = om.writeValueAsString(aRequest);
		} catch (Exception e) {
			log.severe("Failed to write Request as JSON");
		}
		return json;
	}

	private static String toJSON(ArrayList<Requests> aList) {
		String json = "If you see this there is trouble";

		try {
			ObjectMapper om = new ObjectMapper();
			om.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
			json = om.writeValueAsString(aList);

		} catch (Exception e) {
			log.severe("Failed to write Request List to JSON "
					+ e.getLocalizedMessage());
		}

		return json;
	}

	private static Requests fromJSON(String jsonString) {
		Requests aRequest = new Requests();
		try {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
			ObjectMapper om = new ObjectMapper();
			om.setDateFormat(dateFormatter);
			aRequest = om.readValue(jsonString, Requests.class);
		} catch (Exception e) {
			log.severe("Failed to map JSON String to Request " + jsonString);
			log.severe("Exception is " + e.getLocalizedMessage());
		}
		return aRequest;
	}
	
	// These implement the Jersey services that are called by the user
	@GET
	@Path("/fetchStudentRequests/{idStudent: \\d+}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response fetchStudentRequests(
			@PathParam("idStudent") Integer idStudent) {

		// Defaulting University to 1 for DEMO
		Requests aRequest = new Requests();
		aRequest.setIdStudent(idStudent);

		ArrayList<BaseDataAccess> ourList = aRequest
				.fetchList(Requests.FETCH_ALL_STUDENT_REQUESTS);
		Iterator<BaseDataAccess> it = ourList.iterator();
		ArrayList<Requests> finalList = new ArrayList<Requests>();
		Requests tempCourse = null;
		while (it.hasNext()) {
			tempCourse = (Requests) it.next();
			finalList.add(tempCourse);
		}
		return Response.status(200).entity(toJSON(finalList)).build();
	}

	// These implement the Jersey services that are called by the user
	@GET
	@Path("/fetchAllRequests")
	@Produces(MediaType.APPLICATION_JSON)
	public Response fetchAllRequests() {

		// Defaulting University to 1 for DEMO
		Requests aRequest = new Requests();

		ArrayList<BaseDataAccess> ourList = aRequest
				.fetchAllRequests();
		Iterator<BaseDataAccess> it = ourList.iterator();
		ArrayList<Requests> finalList = new ArrayList<Requests>();
		Requests tempCourse = null;
		while (it.hasNext()) {
			tempCourse = (Requests) it.next();
			finalList.add(tempCourse);
		}
		return Response.status(200).entity(toJSON(finalList)).build();
	}


	@GET
	@Path("/fetchCourseRequests")
	@Produces(MediaType.APPLICATION_JSON)
	public Response fetchCourseRequests() {

		// Defaulting University to 1 for DEMO
		Requests aRequest = new Requests();

		String json = aRequest.fetchAllStudentsByCourse();
		return Response.status(200).entity(json).build();
	}


	@GET
	@Path("/fetchStudentRequest/{idRequest: \\d+}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response fetchCourse(@PathParam("idRequest") Integer idRequest) {

		// Defaulting University to 1 for DEMO
		Requests aRequest = new Requests();
		aRequest.setIdRequest(idRequest);
		if (aRequest.fetch() != BaseDataAccess.SUCCESS) {
			String msg = "Failed to fetch Student Request with id = "
					+ idRequest;
			log.severe(msg);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(msg).build();
		} else {
			return Response.status(200).entity(toJSON(aRequest)).build();
		}
	}

	/*
	 * Looks up this student and calculates the number of years this student has
	 * been enrolled in the program
	 */
	private String calculateYearsEnrolled(String idStudent) {

		Integer years = -1;

		try {
			Student aStudent = new Student(); // This is the data tier student
			aStudent.setId( new Integer(idStudent) );
			if (aStudent.fetch() != BaseDataAccess.SUCCESS) {
				log.severe("Failed to fetch student when calculating years enrolled. - "
						+ idStudent);
			} else {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date aDate = sdf.parse(aStudent.getEnrollmentDate());
				Date now = new Date();
				Calendar enrollmentCal = Calendar.getInstance();
				enrollmentCal.setTime(aDate);
				Calendar nowCal = Calendar.getInstance();
				nowCal.setTime(now);

				years = nowCal.get(Calendar.YEAR)
						- enrollmentCal.get(Calendar.YEAR);
			}
		} catch (Exception e) {
			log.severe("Failed to parse Student's enrollment date"
					+ e.getLocalizedMessage());
		}

		return years.toString();
	}

	// Takes the datatier objects and maps them to the engine data structures
	private String loadResourceData(Requests aRequest) {

		String jsonStr = "If you see this you have trouble";
		ScheduleEngineInput sei = new ScheduleEngineInput();

		// Load course titles from the database
		Course aCourseTool = new Course(); // This is the data tier Course to
											// fetch from the database
		ArrayList<BaseDataAccess> theList = aCourseTool
				.fetchList(Course.FETCH_ALL_COURSES);
		for (int i = 0; i < theList.size(); i++) {

			Course aCourse = (Course) theList.get(i);
			ShortTitle aST = new ShortTitle();
			aST.courseId = aCourse.getId().toString();
			aST.courseName = aCourse.getName();
			aST.maxSize = aCourse.getEnrollmentLimit().toString();
			if( aCourse.getAvailableTerms().contains("Fall")) {
				aST.isOfferedFall = true;
			}
			if( aCourse.getAvailableTerms().contains("Spring") ) {
				aST.isOfferedSpring = true;
			}
			if( aCourse.getAvailableTerms().contains("Summer")) {
				aST.isOfferedSummer = true;
			}
			sei.courseTitles.add(aST);

		}

		// Need to add the latest requests from all of the students that have entered something before
		Requests requestTool = new Requests();
		ArrayList<BaseDataAccess> rList = requestTool.fetchList( Requests.FETCH_LATEST_STUDENTS_REQUEST );
		for( int i = 0; i < rList.size(); i++ ) {
			Requests studentRequest = (Requests) rList.get(i);
			if( studentRequest.getIdStudent() != aRequest.getIdStudent() ) {
				StudentRequest anSI = new StudentRequest();
				anSI.studentId = studentRequest.getIdStudent().toString();
				anSI.yearsEnrolled = calculateYearsEnrolled( anSI.studentId );
				ArrayList<Integer> courseList = studentRequest.getCourseList();
				for( int j = 0; j < courseList.size(); j++ ) {
					ShortCourse aShortCourse = new ShortCourse();
					aShortCourse.id = courseList.get(j).toString();
					anSI.courses.add(aShortCourse);					
				}
				sei.studentInterests.add( anSI );
			}
		}
		
		// Load the student's request information
		StudentRequest anSI = new StudentRequest();
		anSI.studentId = aRequest.getIdStudent().toString();
		anSI.yearsEnrolled = calculateYearsEnrolled(anSI.studentId);
		ArrayList<Integer> aCourseList = aRequest.getCourseList();
		for (int i = 0; i < aCourseList.size(); i++) {
			ShortCourse aShortCourse = new ShortCourse();
			aShortCourse.id = aCourseList.get(i).toString();
			anSI.courses.add(aShortCourse);
		}
		sei.studentInterests.add( anSI );

		// Load Prerequisite Information
		Prerequisites aPreReqTool = new Prerequisites();
		ArrayList<BaseDataAccess> aList = aPreReqTool.fetchList(0);
		for (int i = 0; i < aList.size(); i++) {
			Prerequisites aPreReq = (Prerequisites) aList.get(i);
			ShortPrerequisite aPR = new ShortPrerequisite();
			aPR.forTitleId = aPreReq.getForTitleId().toString();
			aPR.preReq = aPreReq.getPreReq().toString();
			sei.titlePreReqs.add(aPR);
		}

		jsonStr = toJSON(sei);
		return jsonStr;
	}
	
	/*
	 * calculateTerm - We are going to assume that this is summer of 2016 and that
	 * semester = 1 means Fall of 2016, 2 = Spring of 2017, etc
	 */
	private String calculateTerm( Integer semester ) {
		
		String termString = "";
		Integer term = semester % 3;
		switch( term ) {
		
			case 1:
				termString = "Fall - ";
				break;
			case 2:
				termString = "Spring - ";
				break;
			default:
				termString = "Summer - ";
				break;
		}
		
		Integer year;
		if( semester == 1 ) {
			year = 2016;
		} else {
			year = 2016 + ((semester - 1)/3);
		}
		
		return termString + year;
		
	}
	/*
	 * This takes the engine results and maps them back to the data tier structures
	 */
	private void loadResults( StudentResults results, Requests aRequest ) {

		ArrayList<StudentResult> srList = results.studentResults;
		
		for( int i = 0; i < srList.size(); i++ ) {
				
			StudentResult sr = srList.get(i);
			if( sr.studentId == aRequest.getIdStudent() ) {
			
				ArrayList<CourseResult> aList = sr.courses;
				ArrayList<Integer> aCourseList = new ArrayList<Integer>();
				ArrayList<String> termAvailableList = new ArrayList<String>();
				
				log.info("Found the student that requested class in results - idStudent = " + sr.studentId);
				
				for( int j = 0; j < aList.size(); j++ ) {
					CourseResult aResult = aList.get(j);
					aCourseList.add( new Integer(aResult.courseId) );
					String termAvailable = calculateTerm( aResult.semester );
					termAvailableList.add( termAvailable );
					log.info("Course " + aResult.courseId + " is available " + termAvailable );
				}
				
				aRequest.setCourseList( aCourseList );
				aRequest.setTermAvailableList( termAvailableList );
			}
		}
	}

	@PUT
	@Path("/makeScheduleRequest")
	@Produces(MediaType.APPLICATION_JSON)
	public Response makeScheduleRequest(String jsonString) {

		log.info("Got into makeScheduleRequest with jsonString of "
				+ jsonString);

		Requests aRequest = fromJSON(jsonString);

		// // Call the engine here and fill in the available Terms

		// AG: This json variable needs to be updated to abide by
		// the structure in the SchedulerEngine README.
		// String jsonStringExample =
		// "{\"courseTitles\" : [{\"courseId\" : \"1\",\"courseName\" : \"class\",\"maxSize\" : \"3\",\"isOfferedSpring\" : true,\"isOfferedSummer\" : false,\"isOfferedFall\" : true}, {\"courseId\" : \"2\",\"courseName\" : \"class2\",\"maxSize\" : \"3\",\"isOfferedSpring\" : true,\"isOfferedSummer\" : false,\"isOfferedFall\" : true}],\"studentInterests\" : [{\"studentId\" : \"1\",\"yearsEnrolled\" : \"3\",\"courses\" : [{\"id\":\"1\"}]}],\"titlePreReqs\" : [{\"preReq\" : \"1\",\"forTitleId\" : \"2\"}],\"policies\" :{\"maxCoursesPerSem\" : \"3\"}}";
		
		
		String jsonStr = loadResourceData(aRequest);
		JSONObject jsonObj = new JSONObject(jsonStr);
		log.info("JSON passed to scheduler: " + jsonObj);
		
		SchedulerEngine schedulerEngine = new SchedulerEngine(jsonObj);
		JSONObject scheduleResults = new JSONObject();
		try {
			scheduleResults = schedulerEngine.run();
		} catch (GRBException e) {
			e.printStackTrace();
			String msg = "Failed to run Gurobi Scheduler. With error: " + e;
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(msg).build();
		}

		log.info("Schedule Result: " + scheduleResults.toString());
		
		StudentResults results = StudentResults.fromJSONtoStudentResults( scheduleResults.toString());
		loadResults( results, aRequest );

		// Load scheduleResults back into aRequest object.

		if (aRequest.insert() != BaseDataAccess.SUCCESS) {
			String msg = "Failed to insert Student Request for studentId "
					+ aRequest.getIdStudent();
			log.severe(msg);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(msg).build();
		} else {
			// AG: How do we return the results of the scheduler? Format will be
			// in:
			/*
			 * { "studentResults" : [ { "studentId" : "12345", "courses" : [ {
			 * "courseId" : "1", "semester" : "1" } ] } ] }
			 */
			return Response.status(200).entity(toJSON(aRequest)).build();
		}
	}

}
