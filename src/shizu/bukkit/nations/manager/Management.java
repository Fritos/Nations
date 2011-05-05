package shizu.bukkit.nations.manager;

import java.util.ArrayList;
import java.util.HashMap;

import shizu.bukkit.nations.Nations;
import shizu.bukkit.nations.database.DataSource;
import shizu.bukkit.nations.database.FlatFile;
import shizu.bukkit.nations.database.Sql;
import shizu.bukkit.nations.object.*;

/**
 * Abstract class to handle data interaction between the server
 * and the data source. The type of data that is being handled is
 * determined by the type of subclass that calls it.
 * 
 * @author Shizukesa
 */
public abstract class Management {
	
	// TODO Config: pull from config or something
	private final Boolean useSql = false;
	
	// TODO Collection : Must typecast any value coming out of the map
	public HashMap<String, NAWObject> collection;
	protected String type; 
	protected DataSource database;
	protected Nations plugin;
	
	public Management(Nations instance) {
		
		plugin = instance;
		database = (DataSource) (useSql ? new Sql(plugin) : new FlatFile(plugin));
	}
	
	/**
	 * Stores an object from the server to the data source
	 * 
	 * @param key The key identifier of the data to store
	 */
	public void saveObject(String key) {
		
		NAWObject obj = (NAWObject)collection.get(key);
		database.save(type, key, obj);
	}
	
	/**
	 * Loads an object from the data source to the server
	 * 
	 * @param key The key identifier of the data to load
	 */
	public Boolean loadObject(String key) {
		
		NAWObject obj = database.load(type, key);
		
		if (obj != null) {
			
			obj.setWorld(plugin.getWorld());
			collection.put(key, obj);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Deletes an object from the data source and the server
	 * 
	 * @param key The key identifier of the data to delete
	 */
	public void deleteObject(String key) {
		database.delete(type, key);
		collection.remove(key);
	}
	
	/**
	 * Stores all objects on the server to the data source
	 */
	public void saveAll() {
		
		for (String key : collection.keySet()) {
			saveObject(key.toString());
		}
	}
	
	/**
	 * Loads all objects from the data source to the server
	 */
	public void loadAll() {
		
		ArrayList<String> keys = database.gatherDataset(type);
		
		for (String key : keys) {
			loadObject(key);
		}
	}
	
	/**
	 * Deletes all object from the data source and server
	 */
	public void deleteAll() {
		
		for (String key : collection.keySet()) {
			deleteObject(key.toString());
		}
	}
}
