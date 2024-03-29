package connect;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.ResultSetMetaData;

import org.apache.log4j.Logger;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import sync.Base;
import api.Connect;

/**
 * A Class to connect to a Mysql-Server
 * @author Helmuth Brunner
 * @version Nov 3, 2014
 * 
 * Current project: VSDBSyncDB
 */
public class ConnectMysql implements Connect {
	
	private static ConnectMysql instance;
	private Base b= Base.get();
	
	private String username, password, hostname, database;
	
	private static Logger log= Logger.getLogger(ConnectMysql.class.getName());
	
	private Connection c;
	
	/**
	 * Returns a instance from this class
	 * @return the instance from this class
	 */
	public static ConnectMysql get() {
		if(instance==null)
			instance= new ConnectMysql();
		return instance;
	}
	
	/**
	 * Private Constructor
	 */
	private ConnectMysql() {
		
		username= b.getSettingString("mysqlusername");
		password= b.getSettingString("mysqlpassword");
		hostname= b.getSettingString("mysqlhostname");
		database= b.getSettingString("mysqldatabase");
		
		this.connect();
		
	}

	/**
	 * Creates the connection to the database
	 */
	@Override
	public void connect() {

		MysqlDataSource mds= new MysqlDataSource();
		
		mds.setUser(this.username);
		mds.setPassword(this.password);
		mds.setServerName(this.hostname);
		mds.setDatabaseName(database);
		
		try {
			c= mds.getConnection();
		} catch (SQLException e) {
			log.error(e);
			System.exit(-1);
		}
		
	}
	
	/**
	 * Returns the Result-Set from the given sql-Statment
	 * @param sql the sql statment
	 * @return the ResultSet
	 */
	@Override
	public ResultSet execute(String query) {

		try {
			return c.createStatement().executeQuery(query);
		} catch (SQLException e) {
			log.error("Error in execute", e);
		}
		return null;
	
	}
	
	/**
	 * For an update on the database
	 * @return
	 */
	public int update(String sql) {
		try {
			return c.createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			log.error("Error in update", e);
		}
		return 0;
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
			log.error("Error in full", e);
		}

		return erg;
	}
	
	/**
	 * Closes the connections
	 */
	@Override
	public void exit() {
		try {
			c.close();
		} catch (SQLException e) {
			log.error("Unable to disconnect from the server!");
		}
	}
	
}
