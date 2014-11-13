package starter;

import javax.swing.JOptionPane;

import middelware.SyncDatabases;
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

	public static void main(String[] args) throws InterruptedException {

		Base.get();

		//		ConnectMysql cm= ConnectMysql.get();
		//		
		//
		//		System.out.println( cm.full("select * from speise") ); // returns the whole table
		//		

		ConnectPsql.get();
		ConnectMysql.get();

//		System.out.println( cp.full("select * from raeder") ); // returns the whole table

		Thread t= new Thread(new SyncDatabases());
		t.start();

		Object[] options = {"Stop Syncing"};

		int erg= JOptionPane.showOptionDialog(
				null,
				"Sync is running",
				"Info",
				JOptionPane.DEFAULT_OPTION, 
				JOptionPane.INFORMATION_MESSAGE, 
				null, options, options[0]);

		if(erg==0) {
			t.stop();
			
			ConnectPsql.get().exit();
			ConnectMysql.get().exit();
			
			System.out.println("Connections Closed");
			
		}
	}
}
