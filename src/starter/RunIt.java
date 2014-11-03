package starter;

import sync.Base;
import connect.ConnectMysql;
import connect.ConnectPsql;

/**
 * Just for test to run
 * @author Helmuth Brunner
 * @version Nov 3, 2014
 * Current project: VSDBSyncDB
 */
public class RunIt {

	public static void main(String[] args) {
		
		Base.get();
		
//		ConnectMysql cm= ConnectMysql.get();
//		
//
//		System.out.println( cm.full("select * from speise") ); // returns the whole table
//		
	
		ConnectPsql cp= ConnectPsql.get();
		
		System.out.println( cp.full("select * from speise") ); // returns the whole table
		
	}
	
}
