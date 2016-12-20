package edu.gatech.cs6310.scheduler;

import edu.gatech.cs6310.services.RequestService;
import gurobi.GRBException;

import java.io.IOException;
import java.util.logging.Logger;

import org.json.JSONObject;

/*
 * This class is responsible for creating the scheduler
 * that will take in as input a JSON object in the form of:
 * 	{
		"courseTitles" : [
		   {
		      "courseId" : "1",
		      "courseName" : "class",
		      "maxSize" : "3",
		      "isOfferedSpring" : true,
		      "isOfferedSummer" : false,
		      "isOfferedFall" : true
		   }
		],
		"studentInterests" : [
		   {
		      "studentId" : "1",
		      "yearsEnrolled" : "3",
		      "courses" : [
		   	     {"id":"1"},{"id":"2"},{"id":"3"}
		   	  ]
		   }
		],
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
 */
public class SchedulerEngine {
	
	private static Logger log = Logger.getLogger(RequestService.class.getName());

	private JSONObject jsonArgument = new JSONObject();
	public SchedulerEngine(JSONObject jsonArg) {
		log.info("JSON Input passed: " + jsonArg);
		jsonArgument = jsonArg;
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
	public JSONObject run() throws GRBException {
        
		// Create the University
		University university = new University("Georgia Tech");
		
		// Process the Catalog File
		try
		{
			log.info("Processing Course Catalog...");
			university.createCourseCatalog(jsonArgument);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		// Process the Title Prereq File
		try
		{
			log.info("Editing the Title PreReqs...");
			university.editTitlePreReq(jsonArgument);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		// Process the Student Interest File
		try
		{		
			log.info("Process Student Interests...");
			university.processStudentInterests(jsonArgument);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}

		// Edit university policies
		
		log.info("Editing University policies...");
		university.editPolicies(jsonArgument);
				
		Constraint universityConstraint = new Constraint(
				university.getPolicy(), university.getStudents(), university.semesterStart, university.getCatalog().courseTitles);
		
		return universityConstraint.generateSolution();
	}
}