/**
 * 
 */
package edu.gatech.cs6310.datatier;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 * @author ubuntu
 *
 */
public abstract class BaseDataAccess {
	
	private static Logger log = Logger.getLogger(BaseDataAccess.class.getName());
	
	private static DataSource theDataSource = null;
	public static Boolean SUCCESS = true;
	protected static String databaseName = "ARMS_DB";
	

	/*
	 * constructor
	 */
	
	void init() {
		// First one grabs the dataSource
		if( theDataSource == null ) {
			theDataSource = getDataSource();
		}
	}
	
	/*
	 * getDataSource - Finds the data source from the Container's resources
	 */
	private DataSource getDataSource() {
		try {
			InitialContext initCtx = new InitialContext();
			Context webContext = (Context) initCtx.lookup("java:/comp/env");
			DataSource ds = (DataSource) webContext.lookup("jdbc/ARMS_DB");
			return ds;
		} catch( Exception e ) {
			log.severe("Failed to get the Initial DataSource" + e.getLocalizedMessage() );
		}
		return null;
	}
	
	/*
	 * getConnection - Gets a connection from the connection pool setup
	 * by the Container
	 */
	Connection getConnection() {
		
		if( theDataSource == null ) {
			theDataSource = getDataSource();
			if( theDataSource == null ) {
				log.severe("Really failed to get a dataSource");
				return null;
			}
		}
		
		Connection aConnection = null;
		try {
			aConnection = theDataSource.getConnection();
		} catch( Exception e ) {
			log.severe("Failed to get a connection with a valid dataSource" + e.getLocalizedMessage());
		}
		
		return aConnection;
	}
	
	/*
	 * fetch - Subclasses need to implement this method
	 */
	public abstract Boolean fetch();
	
	/*
	 * fetchList - Subclasses need to implement this method
	 */
	public abstract ArrayList<BaseDataAccess> fetchList( int selectCriteria );
	
	/*
	 * insert - Subclasses need to implement this method 
	 */
	public abstract Boolean insert();
	
	/*
	 * update - Subclasses need to implement this method
	 */
	public abstract Boolean update();
	
	/*
	 * delete - Subclasses need to implement this method
	 */
	public abstract Boolean delete();
}

