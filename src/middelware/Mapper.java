package middelware;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import starter.RunIt;
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
	private static Logger log= Logger.getLogger(RunIt.class.getName());

	private static Mapper instance;

	/**
	 * Returns a instance from this class
	 * @return the instance
	 */
	public static Mapper get() {
		if(instance==null)
			instance= new Mapper();
		return instance;
	}

	/**
	 * Private Constructor for the Singleton-Pattern
	 */
	private Mapper() {

		//TODO think about this lines
		//idee to make a model to map the data
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

			try {

				int pkid= rs.getInt("id");

				log.info("maptopsql\n\t\tDELETE FROM raeder WHERE id="+ pkid);

				psql.update("DELETE FROM raeder WHERE id="+ pkid);

				mysql.update("DELETE FROM deletedEntry WHERE id="+ pkid);

			}catch(SQLException e) {
				log.info( "Error in maptopsql delete" );
				log.error(e);
			}


			break;

		case INSERT:

			try {

				int id= rs.getInt("id"),
						leistung= rs.getInt("leistung"),
						version= rs.getInt("version");

				String 	marke= rs.getString("marke"),
						modell= rs.getString("modell");


				boolean wreifen= rs.getBoolean("wreifen");
				String swreifen;
				if(wreifen==true)
					swreifen= "'1'";
				else
					swreifen= "'0'";

				String insertString= "INSERT into raeder VALUES ("
						+ id + ","
						+ "'"+ marke +"',"
						+ "'" + modell + "',"
						+ leistung + ","
						+ version + ","
						+ swreifen + ")";

				psql.update(insertString);

				mysql.update("DELETE FROM insertEntry WHERE id="+ id);

			} catch (SQLException e) {
				log.info("Error in maptopsql insert");
				log.error(e);
			}

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

				log.info("maptomysql\n\t\tDELETE FROM raeder WHERE id="+ pkid);

				mysql.update("DELETE FROM raeder WHERE id="+ pkid);

				psql.update("DELETE FROM deletedentry WHERE id="+ pkid);

			}catch(SQLException e) {
				log.info( "Error in maptomysql delete" );
				log.error(e);
			}


			break;

		case INSERT:

			try {

				int id= rs.getInt("id"),
						leistung= rs.getInt("leistung"),
						version= rs.getInt("version");

				String 	marke= rs.getString("marke"),
						modell= rs.getString("modell");


				boolean wreifen= rs.getBoolean("wreifen");
				String swreifen;
				if(wreifen==true)
					swreifen= "'1'";
				else
					swreifen= "'0'";

				String insertString= "INSERT into raeder VALUES ("
						+ id + ","
						+ "'"+ marke +"',"
						+ "'" + modell + "',"
						+ leistung + ","
						+ version + ","
						+ swreifen + ")";

				mysql.update(insertString);

				psql.update("DELETE FROM insertentry WHERE id="+ id);


			} catch (SQLException e) {
				log.info("Error in maptomysql insert");
				log.error(e);
			}


			break;

		case UPDATE:
			break;
		}

	}

}
