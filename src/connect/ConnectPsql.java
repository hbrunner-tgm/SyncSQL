package connect;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.postgresql.jdbc2.optional.SimpleDataSource;

import sync.Base;
import api.Connect;

/**
 * A Class to connect to psql
 * @author Helmuth Brunner
 * @version Nov 3, 2014
 * Current project: VSDBSyncDB
 */
public class ConnectPsql implements Connect {

	private static ConnectPsql instance;
	private String username, password, hostname, database;
	private Connection c;
	
	private Base b= Base.get();
	private static Logger log= Logger.getLogger(ConnectPsql.class.getName());
	
	/**
	 * Returns the instance from this class
	 * @return the instance
	 */
	public static ConnectPsql get() {
		if(instance==null)
			instance= new ConnectPsql();
		return instance;
	}

	/**
	 * Private Constructor
	 */
	private ConnectPsql() {

		username= b.getSettingString("psqlusername");
		password= b.getSettingString("psqlpassword");
		hostname= b.getSettingString("psqlhostname");
		database= b.getSettingString("psqldatabase");

		this.connect();
	}

	/**
	 * Creates a Connection to the database
	 */
	@Override
	public void connect() {

		SimpleDataSource sds= new SimpleDataSource();
		sds.setServerName(this.hostname);
		sds.setUser(this.username);
		sds.setPassword(this.password);
		sds.setDatabaseName(this.database);

		try {
			c= sds.getConnection();
		} catch (SQLException e) {
			log.error(e);
			System.exit(-1);
		}

	}

	/**
	 * Returns a Result-Set for the given sql-query
	 */
	@Override
	public ResultSet execute(String sql) {
		try {
			return c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE).executeQuery(sql);
		} catch (SQLException e) {
			log.error(e);
		}
		return null;
	}
	
	/**
	 * Method to make an update oder insert into the database
	 * @param sql the sql-query
	 * @return if it was successful
	 */
	public boolean update(String sql) {
		try {
			return c.prepareStatement(sql).execute();
		} catch (SQLException e) {
			log.error(e);
		}
		return false;
	}

	// Meanwhile only to print the whole select
	/**
	 * Prints the whole query as text
	 * @param query a sql-query
	 * @return the text
	 */
	public String full(String query) {
		ResultSet rs= this.execute(query);

		String erg= "";
		int columns= 0;
		
		try {
			ResultSetMetaData rsmd= rs.getMetaData();
			columns= rsmd.getColumnCount();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		try {
			while(rs.next()) {
				for(int i= 0;i<columns;i++) {
					erg+= " -|- " + rs.getString(i+1) ;
				}

				erg+= "\n";
			}
		} catch (SQLException e) {
			log.error(e);
		}

		return erg;
	}
	
	/**
	 * Close the connection to the database
	 */
	@Override
	public void exit() {
		try {
			c.close();
		} catch (SQLException e) {
			log.info("Unable to disconnect from the server!");
		}
	}

}
