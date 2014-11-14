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
 * A Mapper which maps the data to the different databases
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

		/*
		 * 
		 * Snycs a delete
		 * 
		 */
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

		
		/*
		 *
		 * Snycs a insert
		 * 
		 */
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

		/*
		 * 
		 * Syncs a update
		 * 
		 */
		case UPDATE:

			try {

				ResultSet respsql;

				for(int i=0; rs.next(); i++) {

					log.info("Counter:" +i);

					int id= rs.getInt("id");
					respsql= psql.execute("select * from raeder where id= "+id);
					respsql.next();

					if(rs.getInt("version") > respsql.getInt("version")) {
						// Do update
						log.info("there is an update in MYSQL on pk= " + rs.getInt("id"));

						int leistung= rs.getInt("leistung"),
								version= rs.getInt("version");

						String 	marke= rs.getString("marke"),
								modell= rs.getString("modell");

						boolean wreifen= rs.getBoolean("wreifen");
						String swreifen;

						// Maps a boolean value
						if(wreifen==true)
							swreifen= "'1'";
						else
							swreifen= "'0'";

						String updateString= "UPDATE raeder SET marke= '"+ marke + "',"

								+ " modell= '" + modell + "',"
								+ " leistung= "+ leistung + ","
								+ "version= "+ version + ","
								+ "wreifen=" + swreifen
								+ "where id= "+rs.getInt("id");

						log.info(updateString);

						psql.update(updateString);

					}

				}

			} catch (SQLException e) {
				log.info("Error in maptomysql update");
				log.error(e);
			}

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

		/*
		 * Syncs a delete
		 */
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

		/*
		 * Syncs a insert
		 */
		case INSERT:

			try {

				int id= rs.getInt("id"),
						leistung= rs.getInt("leistung"),
						version= rs.getInt("version");

				String 	marke= rs.getString("marke"),
						modell= rs.getString("modell");


				boolean wreifen= rs.getBoolean("wreifen");
				String swreifen;

				// Maps a boolean value
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

		/*
		 * Syncs a update
		 */
		case UPDATE:


			try {

				ResultSet resmy;

				// iterats thru the whole tabel
				for(int i=0; rs.next(); i++) {

					log.info("Counter:" +i);

					int id= rs.getInt("id");
					resmy= mysql.execute("select * from raeder where id= "+id);
					resmy.next();

					if(rs.getInt("version") > resmy.getInt("version")) {
						// Do update
						log.info("there is an update in PSQL on pk= " + rs.getInt("id"));

						int leistung= rs.getInt("leistung"),
								version= rs.getInt("version");

						String 	marke= rs.getString("marke"),
								modell= rs.getString("modell");

						boolean wreifen= rs.getBoolean("wreifen");
						String swreifen;

						// Maps a boolean value
						if(wreifen==true)
							swreifen= "'1'";
						else
							swreifen= "'0'";

						String updateString= "UPDATE raeder SET marke= '"+ marke + "',"

								+ " modell= '" + modell + "',"
								+ " leistung= "+ leistung + ","
								+ "version= "+ version + ","
								+ "wreifen=" + swreifen
								+ "where id= "+rs.getInt("id");

						log.info(updateString);

						mysql.update(updateString);

					}

				}

			} catch (SQLException e) {
				log.info("Error in maptomysql update");
				log.error(e);
			}

			break;
		}

	}

}
