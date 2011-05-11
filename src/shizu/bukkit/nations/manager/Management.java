package shizu.bukkit.nations.manager;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Chunk;
import org.bukkit.Location;

import shizu.bukkit.nations.Nations;
import shizu.bukkit.nations.database.DataSource;
import shizu.bukkit.nations.database.FlatFile;
import shizu.bukkit.nations.database.Sql;
import shizu.bukkit.nations.object.*;

/**
 * Abstract class to handle data interaction between the server
 * and the data source. The type of data that is being handled is
 * determined by the value of the child's 'type' variable.
 * 
 * @author Shizukesa
 */
public abstract class Management {
	
	// TODO Config: pull from config or something
	private final Boolean useSql = false;
	
	public HashMap<String, NAWObject> collection;
	protected String type; 
	protected DataSource database;
	protected Nations plugin;
	
	public Management(Nations instance) {
		
		plugin = instance;
		database = (DataSource) (useSql ? new Sql(plugin) : new FlatFile(plugin));
	}
	
	/**
	 * Finds whether an object exists in 'collection'.
	 * 
	 * @param key The 'collection' HashMap key of the object to find
	 * @return true if the object exists, false otherwise
	 */
	public Boolean exists(String key) {
		
		return (collection.containsKey(key)) ? true : false;
	}
	
	/**
	 * Fetches the "location identification key" for a given location. 
	 * This key is the world 'x' and 'z' coordinates of the chunk that 
	 * the given location falls within. The chunk coordinates are the
	 * world coordinates of the top-left most block in the chunk, divided
	 * by 16. The key is returned in the following format: "x.z" (ex: "-7.13")
	 * 
	 * @param loc The location of the chunk 
	 * @return the location identification key
	 */
	public String getLocationKey(Location loc) {
		
		 Chunk chunk = loc.getBlock().getChunk();
		 int x = chunk.getX();
		 int z = chunk.getZ();
		 return x + "." + z;
	}
	
	/**
	 * Sends an object from the 'collection' to the data source for saving.
	 * 
	 * @param key The 'collection' HashMap key of the object to save
	 */
	public void saveObject(String key) {
		
		NAWObject obj = (NAWObject) collection.get(key);
		database.save(type, key, obj);
	}
	
	/**
	 * Fetches an object from the data source and loads it into 
	 * 'collection'.
	 * 
	 * @param key The 'collection' HashMap key of the object to load
	 * @return true if an object was loaded, false if the object is null
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
	 * Deletes an object from the data source and removes it from 
	 * 'collection'.
	 * 
	 * @param key The 'collection' HashMap key of the object to delete
	 */
	public void deleteObject(String key) {
		database.delete(type, key);
		collection.remove(key);
	}
	
	/**
	 * Sends all objects from 'collection' to the data source for saving.
	 */
	public void saveAll() {
		
		for (String key : collection.keySet()) {
			saveObject(key.toString());
		}
		
		plugin.sendToLog(String.valueOf(collection.size()) +  " " + type + "s saved!"); 
	}
	
	/**
	 * Fetches all objects from the data source and loads them into 'collection'.
	 */
	public void loadAll() {
		
		ArrayList<String> keys = database.gatherDataset(type);
		
		for (String key : keys) {
			loadObject(key);
		}
		
		plugin.sendToLog(String.valueOf(collection.size()) +  " " + type + "s loaded!");
	}
	
	/**
	 * Deletes all objects from the data source and removes them from 'collection'.
	 */
	public void deleteAll() {
		
		for (String key : collection.keySet()) {
			deleteObject(key.toString());
		}
	}
}
