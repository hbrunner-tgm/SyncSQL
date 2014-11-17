package starter;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import middelware.SyncDatabases;

import sync.Base;

import connect.ConnectMysql;
import connect.ConnectPsql;

/**
 * Main-Class to start the program
 * @author Helmuth Brunner
 * @version Nov 3, 2014
 * 
 * Current project: VSDBSyncDB
 */
public class RunIt {
	public static void main(String[] args) throws InterruptedException {

		Base.get();
		Logger log= Logger.getLogger(RunIt.class.getName()); // for logging

		// Create the Connections
		ConnectPsql.get();
		ConnectMysql.get();

		Thread t= new Thread(new SyncDatabases()); // Create the thread
		t.start(); // start the thread

		Object[] options = {"Stop Syncing"};

		// opens a new OptionPane
		// if you press stop syncing the programm won't sync the data anymore
		int erg= JOptionPane.showOptionDialog(
				null,
				"Sync is running",
				"Info",
				JOptionPane.DEFAULT_OPTION, 
				JOptionPane.INFORMATION_MESSAGE, 
				null, options, options[0]);

		// if stop syncing is pressed
		if(erg == 0 || erg == -1) {
			t.stop();
			
			// Close the connections to the databases
			ConnectPsql.get().exit();
			ConnectMysql.get().exit();
			
			log.info("Connections Closed");
		}
	}
}
