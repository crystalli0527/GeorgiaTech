/**
 * 
 */
package edu.gatech.cs6310.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import edu.gatech.cs6310.datatier.BaseDataAccess;
import edu.gatech.cs6310.datatier.Requests;
import edu.gatech.cs6310.datatier.Student;

/**
 * @author ubuntu
 *
 */

@Path("/StudentService")
public class StudentService {
	
	private static Logger log = Logger.getLogger(StudentService.class.getName());
	
	private static String toJSON( Student aStudent ) {
		String json = "If you see this you have trouble";
		try {
			ObjectMapper om = new ObjectMapper();
			om.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false );
			json = om.writeValueAsString( aStudent );
		} catch( Exception e ) {
			log.severe("Failed to write Student as JSON" );
		}
		return json;
	}
	
	private static String toJSON( ArrayList<Student> aList ) {
		String json = "If you see this there is trouble";
		
		try {
			ObjectMapper om = new ObjectMapper();
			om.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false );
			json = om.writeValueAsString( aList );
			
		} catch( Exception e ) {
			log.severe("Failed to write Student List to JSON " + e.getLocalizedMessage());
		}
		
		return json;
	}
	private static Student fromJSON( String jsonString ) {
		Student aStudent = new Student();
		try {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
			ObjectMapper om = new ObjectMapper();
			om.setDateFormat( dateFormatter );
			aStudent = om.readValue( jsonString, Student.class );
		} catch( Exception e ) {
			log.severe("Failed to map JSON String to Student " + jsonString );
			log.severe("Exception is " + e.getLocalizedMessage() );
		}
		return aStudent;
	}

	@POST
	@Path("insertStudent")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response insertProfile( String jsonString ) {
		
		log.info("Got to insert Student Web Service - jsonString is " + jsonString );
		
		Student aStudent = fromJSON( jsonString );
		if( aStudent.insert() != BaseDataAccess.SUCCESS ) {
			String msg = "Failed to insert Student with lastName = " + aStudent.getLastName();
			log.severe( msg );
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(msg).build();
		} else {
			return Response.status( 200 ).entity(toJSON( aStudent)).build();
		}
	}
	
	@GET
	@Path("/fetchStudents")
	@Produces(MediaType.APPLICATION_JSON)
	public Response fetchStudents() {
		
		// Defaulting University to 1 for DEMO
		Student aStudent = new Student();
		aStudent.setIdUniversity( 1 );
		
		ArrayList<BaseDataAccess> ourList = aStudent.fetchList( Student.FETCH_ALL_STUDENTS);
		Iterator<BaseDataAccess> it = ourList.iterator();
		ArrayList<Student> finalList = new ArrayList<Student>();
		Student tempStudent = null;
		while( it.hasNext() ) {
			tempStudent = (Student) it.next();
			finalList.add( tempStudent);
		}
		return Response.status(200).entity(toJSON( finalList )).build();
	}
	
	@GET
	@Path("/fetchStudent/{studentId: [a-zA-Z0-9_/]+}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response fetchStudent(@PathParam("studentId") String studentId ) {
		
		// Defaulting University to 1 for DEMO
		Student aStudent = new Student();
		aStudent.setIdUniversity( 1 );
		aStudent.setStudentId( studentId );
		if( aStudent.fetch() != BaseDataAccess.SUCCESS ) {
			String msg = "Failed to fetch Student with studentId = " + studentId;
			log.severe(msg);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(msg).build();
		} else {
			return Response.status(200).entity(toJSON( aStudent )).build();			
		}
	}
	
	@PUT
	@Path("/updateStudent")
	@Produces( MediaType.APPLICATION_JSON )
	public Response updateStudent( String jsonString ) {
		
		log.info("Got into updateStudent with jsonString of " + jsonString );
		
		Student newStudent = fromJSON( jsonString );
		Student oldStudent = new Student();
		oldStudent.setStudentId( newStudent.getStudentId() );
		
		if( oldStudent.fetch() ) {
			String msg = "Failed to fetch Student with studentId of " + newStudent.getStudentId();
			log.severe( msg );
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(msg).build();
		} else {
			oldStudent.setPassword( newStudent.getPassword());
			oldStudent.setFirstName( newStudent.getFirstName());
			oldStudent.setLastName( newStudent.getLastName() );
			oldStudent.setEnrollmentDate( newStudent.getEnrollmentDate() );
			if( oldStudent.update() != BaseDataAccess.SUCCESS ) {
				String msg = "Failed to update student with studentId = " + newStudent.getStudentId();
				log.severe(msg);
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(msg).build();
			} else {
				return Response.status(200).entity(toJSON( oldStudent)).build();
			}
		}
	}
	
	@DELETE
	@Path("/deleteStudent/{idStudent: \\d+}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteStudent(@PathParam("idStudent") Integer idStudent ) {
		
		// First, we need to delete all of the student requests we might have for this student
		Requests aRequestTool = new Requests();
		if( aRequestTool.deleteStudentRequests( idStudent ) != BaseDataAccess.SUCCESS ) {
			String msg = "Failed to delete Student because it contains requests = " + idStudent;
			log.severe(msg);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(msg).build();			
		}
		
		Student aStudent = new Student();
		aStudent.setId( idStudent );
		if( aStudent.delete() != BaseDataAccess.SUCCESS ) {
			String msg = "Failed to delete Student with studentId = " + idStudent;
			log.severe(msg);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(msg).build();
		} else {
			return Response.status(200).entity(toJSON( aStudent )).build();			
		}
	}
	
	@POST
	@Path("/login")
	@Produces( MediaType.APPLICATION_JSON )
	public Response login( String jsonString ) {
		log.info("Got to login Student Web Service - jsonString is " + jsonString );
	
		Student aStudent = fromJSON( jsonString );

		String potentialPassword = aStudent.getPassword();
		
		if( aStudent.fetch() != BaseDataAccess.SUCCESS ) {
			String msg = "{\"studentId\" : \"" + aStudent.getStudentId() + "\","
					+ "\"authenticated\" : \"false\"}";
			return Response.status( 200 ).entity(msg).build();
		} else {
			String responseMsg = null;
			if( potentialPassword.equals( aStudent.getPassword())) {
				responseMsg = "{\"studentId\" : \"" + aStudent.getStudentId() + "\","
						+ "\"authenticated\" : \"true\"}";
			} else {
				responseMsg = "{\"studentId\" : \"" + aStudent.getStudentId() + "\","
						+ "\"authenticated\" : \"false\"}";
				
			}
			return Response.status( 200 ).entity(responseMsg).build();
		}
		
	}
}
