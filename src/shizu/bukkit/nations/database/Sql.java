package shizu.bukkit.nations.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import shizu.bukkit.nations.Nations;
import shizu.bukkit.nations.object.NAWObject;

/**
 * Saves and loads NAW data to and from a SQL datasource
 * 
 * @author Shizukesa
 */
public class Sql implements DataSource {
	
	private String SQL_DRIVER = "com.mysql.jdbc.Driver";
	private String DB_NAME;
	
	private static Nations plugin;
	
	public Sql(Nations instance) {
		plugin = instance;
		DB_NAME = plugin.config.get("mysql_db_name");
		initSqlConnection("jdbc:mysql://" + 
						  plugin.config.get("mysql_url") + ":" +
						  plugin.config.get("mysql_port"), 
						  plugin.config.get("mysql_user"), 
						  plugin.config.get("mysql_pass"));
		//Look for tables and load data if it exists
	}
	
	/**
	 * Initiates a connection to the NAW SQL database. If the mc_bukkit_naw 
	 * database does not exist, one is created
	 * 
	 * @param url The URL of the MySql server
	 * @param user The user name with which to access the database
	 * @param pass The password with which to access the database
	 * @return Returns true on a successful connection, false on error
	 */
	private boolean initSqlConnection(String url, String user, String pass) {
		
		try {
			boolean exists = false;
			Class.forName(SQL_DRIVER);
			Connection sqlConn = DriverManager.getConnection(url, user, pass);
			DatabaseMetaData dm = sqlConn.getMetaData();
			ResultSet rs = dm.getCatalogs();
			
			while (rs.next()) {
				if (rs.getString("TABLE_CAT").compareTo(DB_NAME) == 0) { exists = true; }
			}
			
			if (exists == false) {
				Statement smt = sqlConn.createStatement();
				smt.executeUpdate("CREATE DATABASE " + DB_NAME);
				smt.close();
				plugin.sendToLog("SQL Database not found, creating database: mc_bukkit_naw");	
			}
			
			rs.close();
			sqlConn.close();
			sqlConn = DriverManager.getConnection(url + DB_NAME, user, pass);
			plugin.sendToLog("SQL Database connection initialized");
			return true;
		} catch (Exception e) {
			plugin.sendToLog("SQL Database connection failed!: " + e.getMessage());
		}
		
		return false;
	}

	@Override
	public void save(String type, String key, NAWObject obj) {
		// TODO SQL: save
		
	}

	@Override
	public NAWObject load(String type, String key) {
		// TODO SQL: load
		return null;
	}
	
	@Override
	public void delete(String type, String key) {
		// TODO SQL: delete
		
	}

	@Override
	public ArrayList<String> gatherDataset(String type) {
		// TODO SQL: gatherDataset
		return new ArrayList<String>();
	}
}

