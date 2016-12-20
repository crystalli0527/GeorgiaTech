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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

/**
 * @author ubuntu
 *
 */
public class Student extends BaseDataAccess {
	
	private static Logger log = Logger.getLogger(Student.class.getName());
	
	private String studentId = "";
	private String password = "";
	private String firstName = "";
	private String lastName = "";
	private Integer id = -1;
	private String enrollmentDate = "";
	private Timestamp creationDate = new Timestamp( Calendar.getInstance().getTimeInMillis()); 
	private Integer idUniversity = 1;  // Default to GT
	
	
	// Fetch Criteria
	
	public static Integer FETCH_ALL_STUDENTS = new Integer( 0 );
	
	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getIdUniversity() {
		return idUniversity;
	}

	public void setIdUniversity(Integer idUniversity) {
		this.idUniversity = idUniversity;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEnrollmentDate() {
		return enrollmentDate;
	}

	public void setEnrollmentDate(String enrollmentDate) {
		this.enrollmentDate = enrollmentDate;
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
		String fetchSQL;
		
		if( this.getStudentId().isEmpty() ) {
			log.info("StudentId has not been set, trying unique id");
			if( this.getId() < 0 ) {
				log.severe("Neither the StudentId nor the unique id key have been set in Student Fetch");
				return rv;
			} else {
				fetchSQL = "select * from " + databaseName + ".Students where id = " + this.getId();
			}
		} else {
			fetchSQL = "select * from " + databaseName + ".Students where StudentId = '" + this.getStudentId() + "'";
		}
		
		
		Connection aConnection = this.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			stmt = aConnection.prepareStatement( fetchSQL );
			rs = stmt.executeQuery();
			if( rs.next() ) {
				this.id = rs.getInt("id");
				this.studentId = rs.getString("studentId");
				this.password = rs.getString("password");
				this.firstName = rs.getString("firstName");
				this.lastName = rs.getString("lastName");
				Timestamp aTime = rs.getTimestamp("enrollmentDate");
				this.enrollmentDate = sdf.format(aTime);
				this.creationDate = rs.getTimestamp("creationDate");
				this.idUniversity = rs.getInt("University_id");
				rv = SUCCESS;
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
			log.severe("SQLException when trying to fetch student with userId of " + this.studentId + e.getLocalizedMessage());
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
		
		if( selectCriteria != Student.FETCH_ALL_STUDENTS ) {
			log.severe("Not sure what to fetch in Student fetchList");
			return thisList;
		}
		
		String fetchSQL = "select * from " + databaseName + ".Students order by enrollmentDate";
		
		Connection aConnection = this.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		try {
			stmt = aConnection.prepareStatement( fetchSQL );
			rs = stmt.executeQuery();
			while( rs.next() ) {
				Student aStudent = new Student();
				
				aStudent.id = rs.getInt("id");
				aStudent.studentId = rs.getString("studentId");
				aStudent.password = rs.getString("password");
				aStudent.firstName = rs.getString("firstName");
				aStudent.lastName = rs.getString("lastName");
				Timestamp aTime = rs.getTimestamp("enrollmentDate");
				aStudent.enrollmentDate = sdf.format(aTime);
				aStudent.creationDate = rs.getTimestamp("creationDate");
				aStudent.idUniversity = rs.getInt("University_id");
				thisList.add( aStudent );
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
		if( this.idUniversity < 0 ) {
			log.severe("idUniversity must be filled in to insert a Student");
			return rv;
		}
		
		String insertSQL = "insert into " + databaseName + ".Students "
				+ "( studentId, password, firstName, lastName, enrollmentDate, creationDate, University_id )"
				+ "values (?, ?, ?, ?, ?, ? , ? )";
		
		Connection aConnection = this.getConnection();
		PreparedStatement stmt = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		try {
			stmt = aConnection.prepareStatement( insertSQL, Statement.RETURN_GENERATED_KEYS );
			stmt.setString(1,  this.studentId );
			stmt.setString(2,  this.password);
			stmt.setString(3,  this.firstName);
			stmt.setString(4,  this.lastName );
			Date aDate = sdf.parse( this.enrollmentDate );
			stmt.setTimestamp(5, new java.sql.Timestamp(aDate.getTime()) );
			stmt.setTimestamp(6,  this.creationDate );
			stmt.setInt(7, this.idUniversity );
			
			stmt.executeUpdate();
			
			// Fetch autoincremented id
			
			ResultSet tableKeys = stmt.getGeneratedKeys();
			tableKeys.next();
			this.id = tableKeys.getInt(1);
			tableKeys.close();
			tableKeys = null;
			
			log.info("Insert of Student " + this.lastName + " successful.  Id is " + this.id );
			rv = SUCCESS;
			stmt.close();
			stmt = null;
			aConnection.close();
			aConnection = null;
			
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
		
		Boolean rv = !SUCCESS;
		
		// Update requires an Id
		if( this.id < 0 ) {
			log.severe("Student update failed with id < 0 ");
			return rv;
		}
		
		String updateSQL = "update " + databaseName + ".Students "
				+ "set studentId = ?, "
				+ "password = ?, "
				+ "firstName = ?, "
				+ "lastName = ?, "
				+ "enrollmentDate = ?, "
				+ "creationDate = ?, "
				+ "University_id = ? "
				+ "where id = ? ";
		
		Connection aConnection = this.getConnection();
		PreparedStatement stmt = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		try {
			stmt = aConnection.prepareStatement( updateSQL );
			stmt.setString(1, this.studentId );
			stmt.setString(2, this.password );
			stmt.setString(3, this.firstName);
			stmt.setString(4, this.lastName );			
			Date aDate = sdf.parse( this.enrollmentDate );
			stmt.setTimestamp(5, new java.sql.Timestamp(aDate.getTime()) );
			stmt.setTimestamp(6, this.creationDate );
			stmt.setInt(7, this.idUniversity );
			stmt.setInt( 8, this.id );
			
			if( stmt.executeUpdate() <= 0 ) {
				log.severe("Failed to update Student with id = " + this.id );
			} else {
				log.info("Updated Student with id = " + this.id );
				rv = SUCCESS;
			}
			
			stmt.close();
			stmt = null;
			aConnection.close();
			aConnection = null;
			
		} catch (Exception e ) {
			log.severe("Failed to update Student" + e.getLocalizedMessage() );
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
		Boolean rv = !SUCCESS;
		
		// Delete requires an Id
		if( this.id < 0 ) {
			log.severe("Student delete failed with id < 0 ");
			return rv;
		}
		
		String deleteSQL = "delete from " + databaseName + ".Students "
				+ "where id = ? ";
		
		Connection aConnection = this.getConnection();
		PreparedStatement stmt = null;
		
		try {
			stmt = aConnection.prepareStatement( deleteSQL );
			stmt.setInt( 1, this.id );
			
			if( stmt.executeUpdate() <= 0 ) {
				log.severe("Failed to delete Student with id = " + this.id );
			} else {
				log.info("Removed Student with id = " + this.id );
				rv = SUCCESS;
			}
			
			stmt.close();
			stmt = null;
			aConnection.close();
			aConnection = null;
			
		} catch (SQLException e ) {
			log.severe("Failed to delete Student" + e.getLocalizedMessage() );
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

}
