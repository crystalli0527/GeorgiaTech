/**
 * 
 */
package edu.gatech.cs6310.datatier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * @author ubuntu
 *
 */
public class Prerequisites extends BaseDataAccess {
	
	private static Logger log = Logger.getLogger(Prerequisites.class.getName());

	private Integer preReq = -1;
	private Integer forTitleId = -1;
	
	public Integer getPreReq() {
		return preReq;
	}

	public void setPreReq(Integer preReq) {
		this.preReq = preReq;
	}

	public Integer getForTitleId() {
		return forTitleId;
	}

	public void setForTitleId(Integer forTitleId) {
		this.forTitleId = forTitleId;
	}

	/* (non-Javadoc)
	 * @see edu.gatech.cs6310.datatier.BaseDataAccess#fetch()
	 */
	@Override
	public Boolean fetch() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.gatech.cs6310.datatier.BaseDataAccess#fetchList(int)
	 */
	@Override
	public ArrayList<BaseDataAccess> fetchList(int selectCriteria) {
		
		ArrayList<BaseDataAccess> theList = new ArrayList<BaseDataAccess>();
				
		String fetchSQL = "select * from " + databaseName + ".Prerequisites";
		
		Connection aConnection = this.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = aConnection.prepareStatement( fetchSQL );
			rs = stmt.executeQuery();
			while( rs.next() ) {
				Prerequisites aPreReq = new Prerequisites();
				
				aPreReq.forTitleId = rs.getInt("MainId");
				aPreReq.preReq = rs.getInt("PrerequisiteId");
				theList.add( aPreReq );
			}
			
			rs.close();
			rs = null;
			stmt.close();
			stmt = null;
			aConnection.close();
			aConnection = null;
			
		} catch (SQLException e ) {
			log.severe("Failed to fetch Prerequisite List" + e.getLocalizedMessage() );
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
		return theList;
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

}
