package connect;

import java.net.ConnectException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

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

	private Base b= Base.get();

	private Connection c;

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
			e.printStackTrace();
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
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 */
	public boolean update(String sql) {
		try {
			return c.prepareStatement(sql).execute();
		} catch (SQLException e) {}
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
			e.printStackTrace();
		}

		return erg;
	}
	
	@Override
	public void exit() {
		try {
			c.close();
		} catch (SQLException e) {
			System.out.println("Unable to disconnect from the server!");
		}
	}

}
