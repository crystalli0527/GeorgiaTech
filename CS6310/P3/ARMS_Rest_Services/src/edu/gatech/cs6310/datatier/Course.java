/**
 * 
 */
package edu.gatech.cs6310.datatier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Logger;

/**
 * @author ubuntu
 *
 */
public class Course extends BaseDataAccess {

	public static Integer FETCH_ALL_COURSES = new Integer( 0 );

	private static Logger log = Logger.getLogger(Student.class.getName());

	private Integer id = -1;
	private String name = "";
	private String availableTerms = "";
	private Integer enrollmentLimit = -1;
	private Timestamp creationDate = new Timestamp( Calendar.getInstance().getTimeInMillis()); 
	private Integer idUniversity = 1;  // Default to GT
	private ArrayList<Course> prerequisites = new ArrayList<Course>();

	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAvailableTerms() {
		return availableTerms;
	}

	public void setAvailableTerms(String availableTerms) {
		this.availableTerms = availableTerms;
	}

	public Integer getEnrollmentLimit() {
		return enrollmentLimit;
	}

	public void setEnrollmentLimit(Integer enrollmentLimit) {
		this.enrollmentLimit = enrollmentLimit;
	}

	public Timestamp getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}

	public Integer getIdUniversity() {
		return idUniversity;
	}

	public void setIdUniversity(Integer idUniversity) {
		this.idUniversity = idUniversity;
	}

	public void setPrerequisites( ArrayList<Course> theList ) {
		this.prerequisites = theList;
	}
	
	public ArrayList<Course> getPrerequisites() {
		return this.prerequisites;
	}
	
	
	private ArrayList<Course> fetchPrerequisites( Integer id ) throws SQLException {
		
		ArrayList<Course> theList = new ArrayList<Course>();
		
		// Now fetch this classes' prerequisites
		Connection newConnection = this.getConnection();
		PreparedStatement stmt2 = null;
		ResultSet rs2 = null;
		
		String sql = "select * from " + databaseName + ".Prerequisites where MainId = ?";
		stmt2 = newConnection.prepareStatement(sql);
		stmt2.setInt(1, id);
		
		rs2 = stmt2.executeQuery();
		while( rs2.next() ) {
			
			Course aCourse = new Course();
			aCourse.setId( rs2.getInt("PrerequisiteId"));
			theList.add( aCourse );
		}
		
		rs2.close();
		rs2 = null;
		stmt2.close();
		newConnection.close();
		newConnection = null;
		
		return theList;
	}
	
	/* (non-Javadoc)
	 * @see edu.gatech.cs6310.datatier.BaseDataAccess#fetch()
	 */
	@Override
	public Boolean fetch() {
		Boolean rv = !SUCCESS;
		
		if( this.getId() < 0  ) {
			log.severe("Id has not been set, can't fetch");
			return rv;
		}
		
		String fetchSQL = "select * from " + databaseName + ".Courses where id = ?";
		
		Connection aConnection = this.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			stmt = aConnection.prepareStatement( fetchSQL );
			stmt.setInt(1, this.getId());
			rs = stmt.executeQuery();
			if( rs.next() ) {
				this.id = rs.getInt("id");
				this.name = rs.getString("name");
				this.availableTerms = rs.getString("availableTerms");
				this.enrollmentLimit = rs.getInt("enrollmentLimit");
				this.creationDate = rs.getTimestamp("creationDate");
				this.idUniversity = rs.getInt("University_id");
				rv = SUCCESS;

				this.prerequisites = fetchPrerequisites( this.id );
				
			} else {
				rv = !SUCCESS;
			}
			
			rs.close();
			rs = null;
			stmt.close();
			stmt = null;
			aConnection.close();
			aConnection = null;
			
		} catch( SQLException e ) {
			log.severe("SQLException when trying to fetch course with id of " + this.id + e.getLocalizedMessage());
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
		
		if( selectCriteria != Course.FETCH_ALL_COURSES ) {
			log.severe("Not sure what to fetch in Student fetchList");
			return thisList;
		}
		
		String fetchSQL = "select * from " + databaseName + ".Courses where University_id = 1 order by id";
		
		Connection aConnection = this.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = aConnection.prepareStatement( fetchSQL );
			rs = stmt.executeQuery();
			while( rs.next() ) {
				Course aCourse = new Course();
				
				aCourse.id = rs.getInt("id");
				aCourse.name = rs.getString("name");
				aCourse.availableTerms = rs.getString("availableTerms");
				aCourse.enrollmentLimit = rs.getInt("enrollmentLimit");
				aCourse.creationDate = rs.getTimestamp("creationDate");
				aCourse.idUniversity = rs.getInt("University_id");
				aCourse.setPrerequisites( fetchPrerequisites( aCourse.id ));
				thisList.add( aCourse );
			}
			
			rs.close();
			rs = null;
			stmt.close();
			stmt = null;
			aConnection.close();
			aConnection = null;
			
		} catch (SQLException e ) {
			log.severe("Failed to fetch Course List" + e.getLocalizedMessage() );
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
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.gatech.cs6310.datatier.BaseDataAccess#update()
	 */
	@Override
	public Boolean update() {
		Boolean rv = !SUCCESS;
		
		// Update requires an Id
		if( this.id < 0 ) {
			log.severe("Course update failed with id < 0 ");
			return rv;
		}
		
		String updateSQL = "update " + databaseName + ".Courses "
				+ "set name = ?, "
				+ "availableTerms = ?, "
				+ "enrollmentLimit = ?, "
				+ "creationDate = ?, "
				+ "University_id = ? "
				+ "where id = ? ";
		
		Connection aConnection = this.getConnection();
		PreparedStatement stmt = null;
		
		try {
			stmt = aConnection.prepareStatement( updateSQL );
			stmt.setString(1, this.name );
			stmt.setString(2, this.availableTerms );
			stmt.setInt(3, this.enrollmentLimit);
			stmt.setTimestamp(4, this.creationDate );
			stmt.setInt(5, this.idUniversity );
			stmt.setInt( 6, this.id );
			
			if( stmt.executeUpdate() <= 0 ) {
				log.severe("Failed to update Course with id = " + this.id );
			} else {
				log.info("Updated Course with id = " + this.id );
				rv = SUCCESS;
			}
			
			stmt.close();
			stmt = null;
			aConnection.close();
			aConnection = null;
			
		} catch (Exception e ) {
			log.severe("Failed to update Course " + e.getLocalizedMessage() );
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
	 * @see edu.gatech.cs6310.datatier.BaseDataAccess#delete()
	 */
	@Override
	public Boolean delete() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
