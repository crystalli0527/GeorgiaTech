package edu.gatech.cs6310.scheduler;

import gurobi.GRBException;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.JSONObject;

public class Scheduler {

	public final static String PROJECT_ROOT = System.getProperty("user.dir");
	public final static String FILE_ROOT = PROJECT_ROOT + "/../src/OMSCS6310_Project1_TestFiles/";
	public final static Path COURSE_TITLE_FILE = Paths.get(FILE_ROOT + "Course_Titles.csv");
	public final static Path COURSE_PREREQS_FILE = Paths.get(FILE_ROOT + "Course_Prerequisites.csv");
	public final static Path COURSE_AVAILABILITY_FILE = Paths.get(FILE_ROOT + "Course_Availability.csv");
	/**
	 * @param args
	 * @throws GRBException 
	 */
	public static void main(String[] args) throws GRBException {

		// Read the -i flag
		
		if (args.length != 2) {
			System.out.println("Error with num args. FIRST Must have -i <PathToStudentDemandFile>");
			return;
		}
		String flag = args[0];
		
    	if (!flag.contains("-i")) {
    		System.out.println("Must have -i <PathToStudentDemandFile>");
    		return;
    	}
        Path STUDENT_DEMAND_FILE = Paths.get(args[1]);
    	
        
		// Create the University
		University university = new University("Georgia Tech");
		
		// Edit university policies
		
		JSONObject jsonPolicies = new JSONObject();
		JSONObject policies = new JSONObject();
		policies.put("maxCoursesPerSem", "4");
		jsonPolicies.put("policies", policies);
		university.editPolicies(jsonPolicies);
		
		// Process the Catalog File
		try
		{
			university.createCourseCatalog(COURSE_TITLE_FILE.toFile());
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		// Process the Title Prereq File
		try
		{
			university.editTitlePreReq(COURSE_PREREQS_FILE.toFile());
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		// Process the Title Availability File
		try
		{
			university.editTitleAvailability(COURSE_AVAILABILITY_FILE.toFile());
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		// Process the Student Interest File
		try
		{		university.processStudentInterests(STUDENT_DEMAND_FILE.toFile());
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}

		Constraint universityConstraint = new Constraint(
				university.getPolicy(), university.getStudents(), university.semesterStart, university.getCatalog().courseTitles);
		
		universityConstraint.generateSolution();
	}
}