package middelware;

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
	
	private boolean running;
	
	public SyncDatabases() {
		running= true;
	}
	
	@Override
	public void run() {
		
		while(running) {
			
			
			
		}
		
	}

	@Override
	public void stop() {
		
		running= false;
		
	}

}
