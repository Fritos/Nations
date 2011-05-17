package shizu.bukkit.nations.database;

import java.util.ArrayList;

import shizu.bukkit.nations.object.NAWObject;

public interface DataSource {
	
	/**
	 * Saves the object to to the data source
	 * 
	 * @param type The type of object being saved
	 * @param key The indentifier key of the object to save
	 * @param obj The object being saved
	 */
	public void save(String type, String key, NAWObject obj);
	
	/**
	 * Loads the object from the data source
	 * 
	 * @param type The type of object being loaded
	 * @param key The identifier key of the object to load
	 * @return The object to load
	 */
	public NAWObject load(String type, String key);
	
	/**
	 * Deletes the object from the data source
	 * 
	 * @param type The type of object being deleted
	 * @param key The identifier key of the object to delete
	 */
	public void delete(String type, String key);
	
	/**
	 * Gathers and returns the key identifications for each
	 * object in the data set 
	 * 
	 * @param type The type of the data set to gather
	 * @return an array of key identifiers
	 */
	public ArrayList<String> gatherDataset(String type);
}
