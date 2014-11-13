package api;

import java.sql.ResultSet;

/**
 * 
 * @author Helmuth Brunner
 * @version Nov 3, 2014
 * Current project: VSDBSyncDB
 */
public interface Connect {

	/**
	 * Creates the connection to the database
	 */
	public void connect();
	
	/**
	 * Returns the Result-Set from the given sql-Statment
	 * @param sql the sql statment
	 * @return the ResultSet
	 */
	public ResultSet execute(String sql);
	
	/**
	 * Close the connection
	 */
	public void exit();
	
}
