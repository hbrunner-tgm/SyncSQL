package middelware;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import api.State;
import connect.ConnectMysql;
import connect.ConnectPsql;

/**
 * A Mapper
 * @author Helmuth Brunner
 * @version Nov 13, 2014
 * Current project: VSDBSyncDB
 */
public class Mapper {

	private Map<String, ArrayList<String>> mysqlModel, psqlModel;

	private ConnectPsql psql= ConnectPsql.get();
	private ConnectMysql mysql= ConnectMysql.get();

	private static Mapper instance;

	private ResultSetMetaData rsmd;

	public static Mapper get() {
		if(instance==null)
			instance= new Mapper();
		return instance;
	}

	private Mapper() {

		mysqlModel= new HashMap<String, ArrayList<String>>();
		psqlModel= new HashMap<String, ArrayList<String>>();

		ArrayList<String> raeder= new ArrayList<String>();
		raeder.add("id");
		raeder.add("marke");
		raeder.add("modell");
		raeder.add("leistung");
		raeder.add("date");

		mysqlModel.put("raeder", raeder);

		ArrayList<String> raederp= new ArrayList<String>();
		raederp.add("id");
		raederp.add("marke");
		raederp.add("modell");
		raederp.add("leistung");
		raederp.add("date");

		psqlModel.put("raeder", raederp);

	}

	/**
	 * Maps to psql
	 * @param rs the ResultSet
	 * @param s DELETE, INSERT, UPDATE
	 */
	public void maptopsql(ResultSet rs, State s) {

		switch(s) {

		case DELETE:

			break;

		case INSERT:



			break;

		case UPDATE:



			break;
		}


	}


	/**
	 * Maps to mysql
	 * @param rs the result set
	 * @param s DELETE, INSERT, UPDATE
	 */
	public void maptomysql(ResultSet rs, State s) {

		switch(s) {

		case DELETE:

			try {
				
				int pkid= rs.getInt("id");

				System.out.println(pkid);

				mysql.update("DELETE FROM raeder WHERE id="+ pkid);

				psql.update("DELETE FROM deletedentry WHERE id="+ pkid);

			}catch(SQLException e) {
				System.err.println("Error In mysqlmapper delete");
				System.err.println( e.getMessage() );
			}
			
			
			break;

		case INSERT:
			break;

		case UPDATE:
			break;
		}

	}

}
