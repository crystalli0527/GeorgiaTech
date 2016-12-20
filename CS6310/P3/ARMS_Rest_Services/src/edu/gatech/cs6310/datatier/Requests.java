/**
 * 
 */
package edu.gatech.cs6310.datatier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import edu.gatech.cs6310.services.RequestService.StudentResults;

/**
 * @author ubuntu
 *
 */
public class Requests extends BaseDataAccess {
	
	private static Logger log = Logger.getLogger(Requests.class.getName());
	public static Integer FETCH_ALL_STUDENT_REQUESTS = new Integer( 0 );
	public static Integer FETCH_LATEST_STUDENTS_REQUEST = new Integer( 1 );
	
	private Integer idStudent = -1;
	private Integer idRequest = -1;
	private Boolean addedByAdmin = false;
	private ArrayList<Integer> courseList = new ArrayList<Integer>();
	private ArrayList<String> termAvailableList = new ArrayList<String>();
	private Timestamp creationDate = new Timestamp( Calendar.getInstance().getTimeInMillis()); 

	

	
	public Boolean getAddedByAdmin() {
		return addedByAdmin;
	}

	public void setAddedByAdmin(Boolean addedByAdmin) {
		this.addedByAdmin = addedByAdmin;
	}

	public Integer getIdStudent() {
		return idStudent;
	}

	public void setIdStudent(Integer idStudent) {
		this.idStudent = idStudent;
	}

	public Integer getIdRequest() {
		return idRequest;
	}

	public void setIdRequest(Integer idRequest) {
		this.idRequest = idRequest;
	}

	public ArrayList<Integer> getCourseList() {
		return courseList;
	}

	public void setCourseList(ArrayList<Integer> theList ) {
		this.courseList = theList;
	}

	public ArrayList<String> getTermAvailableList() {
		return termAvailableList;
	}

	public void setTermAvailableList(ArrayList<String> theList ) {
		this.termAvailableList = theList;
	}

	public Timestamp getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}

	/* (non-Javadoc)
	 * @see edu.gatech.cs6310.datatier.BaseDataAccess#fetch()
	 */
	@Override
	public Boolean fetch() {
		
		Boolean rv = !SUCCESS;
		
		if( this.getIdRequest() < 0  ) {
			log.severe("RequestId has not been set, can't fetch");
			return rv;
		}
		
		String fetchSQL = "select sr.id as id, sr.Students_Id as idStudent, sr.creationDate as creationDate, "
				+ "sr.addedByAdmin as addedByAdmin, "
				+ "shc.idTermAvailable as idTermAvailable, shc.TermAvailableText as termAvailable, "
				+ "shc.Courses_id as idCourse "
				+ "  from " + databaseName + ".ScheduleRequests as sr join " + databaseName 
				+ " .ScheduleRequests_has_Courses as shc "
						+ "where sr.id = shc.ScheduleRequests_id and "
						+ "sr.id = ?";
		
		Connection aConnection = this.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = aConnection.prepareStatement( fetchSQL );
			stmt.setInt(1, this.getIdRequest());
			rs = stmt.executeQuery();
			while( rs.next() ) {
				this.idStudent = rs.getInt("idStudent");
				this.termAvailableList.add( rs.getString("termAvailable") );
				this.addedByAdmin = rs.getBoolean("addedByAdmin");
				this.courseList.add( rs.getInt("idCourse"));
				this.creationDate = rs.getTimestamp("creationDate");
			}
			rv = SUCCESS;
			
			rs.close();
			rs = null;
			stmt.close();
			stmt = null;
			aConnection.close();
			aConnection = null;
			
		} catch( SQLException e ) {
			log.severe("SQLException when trying to fetch student with userId of " + this.idRequest + e.getLocalizedMessage());
		}
		finally {
			if( stmt != null ) {
				try {stmt.close();} catch( Exception e2 ) {log.severe("Yikes - really closing stmt");}
				stmt = null;
			}
			if( aConnection != null ) {
				try{ aConnection.close();} catch( Exception e2 ) {log.severe("Yikes - really closing aConnection");}
				aConnection = null;
			}
		}
		return rv;
	}

	/* (non-Javadoc)
	 * @see edu.gatech.cs6310.datatier.BaseDataAccess#fetchList(int)
	 */
	@Override
	public ArrayList<BaseDataAccess> fetchList(int selectCriteria) {
		
		ArrayList<BaseDataAccess> thisList = new ArrayList<BaseDataAccess>();
		
		if( selectCriteria != Requests.FETCH_ALL_STUDENT_REQUESTS && selectCriteria != Requests.FETCH_LATEST_STUDENTS_REQUEST ) {
			log.severe("Not sure what to fetch in Student fetchList");
			return thisList;				
		}
		
		Connection aConnection = this.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			String fetchSQL = null;

			if( selectCriteria == Requests.FETCH_ALL_STUDENT_REQUESTS ) {
				fetchSQL = "select sr.id as idRequest, sr.Students_Id as idStudent, sr.creationDate as creationDate, "
						+ "sr.addedByAdmin as addedByAdmin, "
						+ "shc.idTermAvailable as idTermAvailable, shc.TermAvailableText as termAvailable, "
						+ "shc.Courses_id as idCourse "
						+ "  from " + databaseName + ".ScheduleRequests as sr join " + databaseName 
						+ " .ScheduleRequests_has_Courses as shc "
								+ "where sr.id = shc.ScheduleRequests_id and "
								+ "sr.Students_id = ? "
								+ "order by sr.Students_id";
				
				log.info(fetchSQL);
				
				stmt = aConnection.prepareStatement(fetchSQL);
				stmt.setInt(1, this.idStudent );
			} else {
				fetchSQL = "SELECT sr.id AS idRequest, sr.Students_id AS idStudent, sr.creationDate AS creationDate, "
						+ " sr.addedByAdmin as addedByAdmin, "
						+ "shc.idTermAvailable AS idTermAvailable, shc.TermAvailableText AS termAvailable, "
						+ "shc.Courses_id AS idCourse "
						+ "FROM ARMS_DB.ScheduleRequests AS sr, ARMS_DB.ScheduleRequests_has_Courses as shc "
						+ "WHERE sr.id = shc.ScheduleRequests_id "
						+ "group by sr.Students_id "
						+ "order by sr.creationDate";
				log.info( fetchSQL );
				stmt = aConnection.prepareStatement(fetchSQL);
			}

			Integer lastRequestId = new Integer(0);
			Requests aRequest = null;
							
			rs = stmt.executeQuery();
			while( rs.next() ) {
				if( rs.getInt("idRequest") != lastRequestId ) {
					if( lastRequestId != 0 ) {
						thisList.add( aRequest );
					}
					aRequest = new Requests();
					lastRequestId = rs.getInt( "idRequest");
				}
				
				aRequest.idStudent = rs.getInt("idStudent");
				aRequest.idRequest = rs.getInt("idRequest");
				aRequest.addedByAdmin = rs.getBoolean("addedByAdmin");
				aRequest.termAvailableList.add( rs.getString("termAvailable") );
				aRequest.courseList.add( rs.getInt( "idCourse"));
				aRequest.creationDate = rs.getTimestamp("creationDate");
			}
			
			if( lastRequestId != 0 ) {
				thisList.add( aRequest );				
			}
			
			rs.close();
			rs = null;
			stmt.close();
			stmt = null;
			aConnection.close();
			aConnection = null;
			
		} catch (SQLException e ) {
			log.severe("Failed to fetch StudentList" + e.getLocalizedMessage() );
		}
		finally {
			if( stmt != null ) {
				try {stmt.close();} catch( Exception e2 ) {log.severe("Yikes - really closing stmt");}
				stmt = null;
			}
			if( aConnection != null ) {
				try{ aConnection.close();} catch( Exception e2 ) {log.severe("Yikes - really closing aConnection");}
				aConnection = null;
			}
		}
		return thisList;
	}

	/* (non-Javadoc)
	 * @see edu.gatech.cs6310.datatier.BaseDataAccess#insert()
	 */
	@Override
	public Boolean insert() {
		Boolean rv = !SUCCESS;
		
		// In order to insert, we need at least the idUniversity filled in
		if( this.idStudent < 0 ) {
			log.severe("idStudent must be filled in to insert a Student Request");
			return rv;
		}
		
		String insertSQL = "insert into " + databaseName + ".ScheduleRequests "
				+ "( Students_id, addedByAdmin, creationDate)"
				+ "values (?, ?, ?)";
		
		Connection aConnection = this.getConnection();
		PreparedStatement stmt = null;
		
		try {
			stmt = aConnection.prepareStatement( insertSQL, Statement.RETURN_GENERATED_KEYS );
			stmt.setInt(1,  this.idStudent );
			stmt.setBoolean(2, this.addedByAdmin );
			stmt.setTimestamp(3,  this.creationDate );
			
			stmt.executeUpdate();
			
			// Fetch autoincremented id
			
			ResultSet tableKeys = stmt.getGeneratedKeys();
			tableKeys.next();
			this.idRequest = tableKeys.getInt(1);
			tableKeys.close();
			tableKeys = null;
			
			stmt.close();
			stmt = null;
			
			// Now add the courses to the list
			insertSQL = "insert into " + databaseName + " .ScheduleRequests_has_Courses "
					+ "( ScheduleRequests_id, Courses_id, TermAvailableText ) "
					+ "values( ?, ?, ? )";
			
			for( int i = 0; i < this.courseList.size(); i++ ) {
				
				stmt = aConnection.prepareStatement( insertSQL );
				stmt.setInt(1, this.idRequest );
				stmt.setInt(2,  this.courseList.get(i) );
				stmt.setString(3, this.termAvailableList.get(i) );
				
				stmt.executeUpdate();
				stmt.close();
			}
			
			stmt = null;
			aConnection.close();
			aConnection = null;
			rv = SUCCESS;
			
		} catch (Exception e ) {
			log.severe("Failed to insert Student" + e.getLocalizedMessage() );
		}
		finally {
			if( stmt != null ) {
				try {stmt.close();} catch( Exception e2 ) {log.severe("Yikes - really closing stmt");}
				stmt = null;
			}
			if( aConnection != null ) {
				try{ aConnection.close();} catch( Exception e2 ) {log.severe("Yikes - really closing aConnection");}
				aConnection = null;
			}
		}
		
		return rv;
	}

	/* (non-Javadoc)
	 * @see edu.gatech.cs6310.datatier.BaseDataAccess#update()
	 */
	@Override
	public Boolean update() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.gatech.cs6310.datatier.BaseDataAccess#delete()
	 */
	@Override
	public Boolean delete() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Boolean deleteStudentRequests( Integer idStudent ) {
		
		Boolean rv = !SUCCESS;
		
		String deleteSQL = "delete from " + databaseName + ".ScheduleRequests_has_Courses "
				+ "where ScheduleRequests_id in (select id from ScheduleRequests where Students_id = ? )";
		
		Connection aConnection = this.getConnection();
		PreparedStatement stmt = null;
		
		try {
			stmt = aConnection.prepareStatement( deleteSQL );
			stmt.setInt( 1, idStudent );
			
			log.info(deleteSQL);
			int updateRows = stmt.executeUpdate();
			log.info("Number of rows delete is " + updateRows );
			stmt.close();
			stmt = null;
			
			deleteSQL = "delete from " + databaseName + ".ScheduleRequests where Students_id = ? ";
			log.info(deleteSQL);
			stmt = aConnection.prepareStatement( deleteSQL );
			stmt.setInt( 1, idStudent );
			updateRows = stmt.executeUpdate();
			log.info("Number of rows delete is " + updateRows );

			rv = SUCCESS;			
			stmt.close();
			stmt = null;
			aConnection.close();
			aConnection = null;
			
		} catch (SQLException e ) {
			log.severe("Failed to delete ScheduleRequests" + e.getLocalizedMessage() );
		}
		finally {
			if( stmt != null ) {
				try {stmt.close();} catch( Exception e2 ) {log.severe("Yikes - really closing stmt");}
				stmt = null;
			}
			if( aConnection != null ) {
				try{ aConnection.close();} catch( Exception e2 ) {log.severe("Yikes - really closing aConnection");}
				aConnection = null;
			}
		}

		return rv;
		
	}
	
	
	public ArrayList<BaseDataAccess> fetchAllRequests() {
		
		ArrayList<BaseDataAccess> thisList = new ArrayList<BaseDataAccess>();
				
		Connection aConnection = this.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			String fetchSQL = null;

			fetchSQL = "select sr.id as idRequest, sr.Students_Id as idStudent, sr.creationDate as creationDate, "
					+ "sr.addedByAdmin as addedByAdmin, "
					+ "shc.idTermAvailable as idTermAvailable, shc.TermAvailableText as termAvailable, "
					+ "shc.Courses_id as idCourse "
					+ "  from " + databaseName + ".ScheduleRequests as sr join " + databaseName 
					+ " .ScheduleRequests_has_Courses as shc "
							+ "where sr.id = shc.ScheduleRequests_id "
							+ "order by sr.Students_id";
			
			log.info(fetchSQL);
				
			stmt = aConnection.prepareStatement(fetchSQL);

			Integer lastRequestId = new Integer(0);
			Requests aRequest = null;
							
			rs = stmt.executeQuery();
			while( rs.next() ) {
				if( rs.getInt("idRequest") != lastRequestId ) {
					if( lastRequestId != 0 ) {
						thisList.add( aRequest );
					}
					aRequest = new Requests();
					lastRequestId = rs.getInt( "idRequest");
				}
				
				aRequest.idStudent = rs.getInt("idStudent");
				aRequest.idRequest = rs.getInt("idRequest");
				aRequest.addedByAdmin = rs.getBoolean("addedByAdmin");
				aRequest.termAvailableList.add( rs.getString("termAvailable") );
				aRequest.courseList.add( rs.getInt( "idCourse"));
				aRequest.creationDate = rs.getTimestamp("creationDate");
			}
			
			if( lastRequestId != 0 ) {
				thisList.add( aRequest );				
			}
			
			rs.close();
			rs = null;
			stmt.close();
			stmt = null;
			aConnection.close();
			aConnection = null;
			
		} catch (SQLException e ) {
			log.severe("Failed to fetch StudentList" + e.getLocalizedMessage() );
		}
		finally {
			if( stmt != null ) {
				try {stmt.close();} catch( Exception e2 ) {log.severe("Yikes - really closing stmt");}
				stmt = null;
			}
			if( aConnection != null ) {
				try{ aConnection.close();} catch( Exception e2 ) {log.severe("Yikes - really closing aConnection");}
				aConnection = null;
			}
		}
		return thisList;
	}

	
	public static class CoursesRequestedByStudents {
		public Integer idCourse;
		public String courseName;
		public Integer studentCount;
		
		private static String toJSON(ArrayList<CoursesRequestedByStudents> aList) {
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

	}
	
	public String fetchAllStudentsByCourse() {
		
		ArrayList<CoursesRequestedByStudents> thisList = new ArrayList<CoursesRequestedByStudents>();
				
		Connection aConnection = this.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			String fetchSQL = null;

			fetchSQL = "select Count(sr.Students_id) as Count, "
					+ "shc.Courses_id as idCourse, c.name as courseName "
					+ "from ARMS_DB.ScheduleRequests as sr join ARMS_DB.ScheduleRequests_has_Courses as shc "
					+ "join ARMS_DB.Courses as c "
					+ "where sr.id = shc.ScheduleRequests_id "
					+ "and shc.Courses_id = c.id group by shc.Courses_id";			
						
			log.info(fetchSQL);
				
			stmt = aConnection.prepareStatement(fetchSQL);

			rs = stmt.executeQuery();
			while( rs.next() ) {
				CoursesRequestedByStudents aRecord = new CoursesRequestedByStudents();
				aRecord.courseName = rs.getString("courseName");
				aRecord.idCourse = rs.getInt("idCourse");
				aRecord.studentCount = rs.getInt("Count");
				thisList.add(aRecord);
			}
						
			rs.close();
			rs = null;
			stmt.close();
			stmt = null;
			aConnection.close();
			aConnection = null;
			
		} catch (SQLException e ) {
			log.severe("Failed to fetch StudentList" + e.getLocalizedMessage() );
		}
		finally {
			if( stmt != null ) {
				try {stmt.close();} catch( Exception e2 ) {log.severe("Yikes - really closing stmt");}
				stmt = null;
			}
			if( aConnection != null ) {
				try{ aConnection.close();} catch( Exception e2 ) {log.severe("Yikes - really closing aConnection");}
				aConnection = null;
			}
		}
		
		return CoursesRequestedByStudents.toJSON( thisList );
	}

	
}
