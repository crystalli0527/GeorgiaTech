/**
 * 
 */
package edu.gatech.cs6310.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import edu.gatech.cs6310.datatier.BaseDataAccess;
import edu.gatech.cs6310.datatier.Course;

/**
 * @author ubuntu
 *
 */
@Path("/CourseService")
public class CourseService {
	
	private static Logger log = Logger.getLogger(CourseService.class.getName());
	
	private static String toJSON( Course aCourse ) {
		String json = "If you see this you have trouble";
		try {
			ObjectMapper om = new ObjectMapper();
			om.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false );
			json = om.writeValueAsString( aCourse );
		} catch( Exception e ) {
			log.severe("Failed to write Course as JSON" );
		}
		return json;
	}
	
	private static String toJSON( ArrayList<Course> aList ) {
		String json = "If you see this there is trouble";
		
		try {
			ObjectMapper om = new ObjectMapper();
			om.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false );
			json = om.writeValueAsString( aList );
			
		} catch( Exception e ) {
			log.severe("Failed to write Course List to JSON " + e.getLocalizedMessage());
		}
		
		return json;
	}
	
	private static Course fromJSON( String jsonString ) {
		Course aCourse = new Course();
		try {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
			ObjectMapper om = new ObjectMapper();
			om.setDateFormat( dateFormatter );
			aCourse = om.readValue( jsonString, Course.class );
		} catch( Exception e ) {
			log.severe("Failed to map JSON String to Course " + jsonString );
			log.severe("Exception is " + e.getLocalizedMessage() );
		}
		return aCourse;
	}

	
	@GET
	@Path("/fetchCourses")
	@Produces(MediaType.APPLICATION_JSON)
	public Response fetchCourses() {
		
		// Defaulting University to 1 for DEMO
		Course aCourse = new Course();
		aCourse.setIdUniversity( 1 );
		
		ArrayList<BaseDataAccess> ourList = aCourse.fetchList( Course.FETCH_ALL_COURSES );
		Iterator<BaseDataAccess> it = ourList.iterator();
		ArrayList<Course> finalList = new ArrayList<Course>();
		Course tempCourse = null;
		while( it.hasNext() ) {
			tempCourse = (Course) it.next();
			finalList.add( tempCourse );
		}
		return Response.status(200).entity(toJSON( finalList )).build();
	}
	
	@GET
	@Path("/fetchCourse/{id: \\d+}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response fetchCourse(@PathParam("id") Integer id ) {
		
		// Defaulting University to 1 for DEMO
		Course aCourse = new Course();
		aCourse.setIdUniversity( 1 );
		aCourse.setId( id );
		if( aCourse.fetch() != BaseDataAccess.SUCCESS ) {
			String msg = "Failed to fetch Course with id = " + id;
			log.severe(msg);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(msg).build();
		} else {
			return Response.status(200).entity(toJSON( aCourse )).build();			
		}
	}
	
	@PUT
	@Path("/updateCourse")
	@Produces( MediaType.APPLICATION_JSON )
	public Response updateCourse( String jsonString ) {
		
		log.info("Got into updateCourse with jsonString of " + jsonString );
		
		Course newCourse = fromJSON( jsonString );
		Course oldCourse = new Course();
		oldCourse.setId( newCourse.getId() );
		
		if( oldCourse.fetch() != BaseDataAccess.SUCCESS ) {
			String msg = "Failed to fetch Course with Id of " + oldCourse.getId();
			log.severe( msg );
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(msg).build();
		} else {
			oldCourse.setName( newCourse.getName());
			oldCourse.setAvailableTerms( newCourse.getAvailableTerms());
			oldCourse.setEnrollmentLimit( newCourse.getEnrollmentLimit() );
			if( oldCourse.update() != BaseDataAccess.SUCCESS ) {
				String msg = "Failed to update Course with Id = " + newCourse.getId();
				log.severe(msg);
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(msg).build();
			} else {
				return Response.status(200).entity(toJSON( oldCourse)).build();
			}
		}
	}
	
}
