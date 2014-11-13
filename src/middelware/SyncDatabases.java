package middelware;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import api.State;
import api.Stoppable;
import connect.ConnectMysql;
import connect.ConnectPsql;

/**
 * The Class to sync the databases
 * @author Helmuth Brunner
 * @version Nov 4, 2014
 * Current project: VSDBSyncDB
 */
public class SyncDatabases implements Runnable, Stoppable {

	private ConnectPsql psql= ConnectPsql.get();
	private ConnectMysql mysql= ConnectMysql.get();
	
	private String oldversionpsql, oldversionmysql;
	
	private ResultSet rs;
	private ResultSetMetaData rsmd;
	
	private Mapper map= Mapper.get();
	
	private boolean running;
	
	public SyncDatabases() {
		running= true;
	}
	
	@Override
	public void run() {
		
		while(running) {
			
			rs= psql.execute("select * from deletedentry");
			
			try {
				
				if(rs.next()) {
//					rs.beforeFirst();

					System.out.println("in running");
					
					map.maptomysql(rs, State.DELETE);
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void stop() {
		
		running= false;
		
	}

}
