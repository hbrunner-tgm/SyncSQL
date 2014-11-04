package middelware;

import connect.ConnectMysql;
import connect.ConnectPsql;

/**
 * The middelware class
 * @author Helmuth Brunner
 * @version Nov 4, 2014
 * Current project: VSDBSyncDB
 */
public class Middelware {

	private ConnectMysql mysql= ConnectMysql.get();
	private ConnectPsql psql= ConnectPsql.get();
	
	public Middelware() {
		
		//TODO start the Thread
		
	}
	
	
	
}
