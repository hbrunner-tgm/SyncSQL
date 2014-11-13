package middelware;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import api.State;
import api.Stoppable;
import connect.ConnectMysql;
import connect.ConnectPsql;

/**
 * The Class to sync the databases
 * 
 * A Thread which syncs the two databases.
 * 
 * @author Helmuth Brunner
 * @version Nov 4, 2014
 * Current project: VSDBSyncDB
 */
public class SyncDatabases implements Runnable, Stoppable {

	private ConnectPsql psql= ConnectPsql.get();
	private ConnectMysql mysql= ConnectMysql.get();

	private ResultSet rs;
	
	private Mapper map= Mapper.get();
	
	private boolean running;

	private static Logger log= Logger.getLogger(SyncDatabases.class.getName());
	
	/**
	 * Constructor
	 */
	public SyncDatabases() {
		running= true;
	}
	
	@Override
	public void run() {
		
		while(running) {
			
			try {
				
				/* ----- psql ----- */
				
					// deletions
				
				/*
				 * Reads from the deletedentry table
				 * if there is a new row the data from the table will be deleted also on the mysql-database
				 */
				rs= psql.execute("select * from deletedentry");
				if(rs.next()) {

					log.info("delete in psql");
					
					map.maptomysql(rs, State.DELETE);
				}

				
					// inserts
				
				rs= psql.execute("select * from insertentry");
				
				if(rs.next()) {

					log.info("insert in psql");
					
					map.maptomysql(rs, State.INSERT);
				}

// ----------------------------------------------------------------------------------------------------------------------------------
				
				/* ----- mysql ----- */
				
					// deletions
				
				/*
				 * the same like psql
				 */
				rs= mysql.execute("select * from deletedEntry");
				
				if(rs.next()) {

					log.info("delete in mysql");
					
					map.maptopsql(rs, State.DELETE);
				}
				
					// inserts
				
				rs= mysql.execute("select * from insertEntry");
				
				if(rs.next()) {

					log.info("insert in mysql");
					
					map.maptopsql(rs, State.INSERT);
				}

				
			} catch (SQLException e) {
				log.error(e);
			}
		}
	}

	/**
	 * Method to stop the thread
	 */
	@Override
	public void stop() {
		running= false;
	}

}
