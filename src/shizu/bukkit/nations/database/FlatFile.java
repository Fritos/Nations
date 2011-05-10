package shizu.bukkit.nations.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import shizu.bukkit.nations.Nations;
import shizu.bukkit.nations.object.NAWObject;

/**
 * Saves and loads 'Nations at War' data to and from local flat-files. The directories
 * are pre-defined in the 'paths' HashMap.
 * 
 * @author Shizukesa
 */
public class FlatFile implements DataSource {
	
	private final HashMap<String, String> paths = new HashMap<String, String>();
	
	private static Nations plugin;
	
	public FlatFile(Nations instance) {
		plugin = instance;
		paths.put("plot", "plugins\\naw\\plots\\");
		paths.put("user", "plugins\\naw\\users\\");
		paths.put("group", "plugins\\naw\\groups\\");
		initFileStructure();
	}
	
	/**
	 * Initiates the flat-file directory structure. If the directories do not
	 * exist, they are created.
	 * 
	 * @return true if directories exist or are created, false otherwise
	 */
	private Boolean initFileStructure() {
		
		File folder;
		
		try {
			for (String type : paths.keySet()) {
				folder = new File(paths.get(type));
				
				if (!folder.exists()) { 
					folder.mkdirs(); 
					plugin.sendToLog("Directory " + type + " not found, creating the path");
				}
			}
			
			return true;
		} catch (Exception e) {
			plugin.sendToLog("Flat-file folder structure could not be created!");
		}
		
		return false;
	}
	
	/**
	 * Serializes and stores the data to a flat-file. If the file does
	 * not exist, it is created.
	 * 
	 * @param type Key for the 'paths' HashMap to determine the working directory
	 * @param key The identification key of the data being saved, also the file name
	 * @param obj The data to serialize and save
	 */
	public void save(String type, String key, NAWObject obj) {
		
		File file = new File(paths.get(type) + key); 
		
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
			out.writeObject(obj);
			out.close();
		} catch (IOException e) {
			plugin.sendToLog(e.getMessage());
		}
	}
	
	
	/**
	 * Deserializes and returns data from a flat-file.
	 * 
	 * @param type Key for the 'paths' HashMap to determine the working directory
	 * @param key The name of the file to deserialize
	 * @return the deserialized data, null on error
	 */
	@Override
	public NAWObject load(String type, String key) {
		
		File file = new File(paths.get(type) + key);
		NAWObject obj = null;
		
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
			obj = (NAWObject)in.readObject();
			in.close();
			return obj;
		} catch (IOException e) {
			plugin.sendToLog(e.getMessage());
		} catch (ClassNotFoundException e) {
			plugin.sendToLog(e.getMessage());
		}
		
		return null;
	}
	
	/**
	 * Deletes the flat-file.
	 * 
	 * @param type Key for the 'paths' HashMap to determine the working directory
	 * @param key The name of the file to delete
	 */
	@Override
	public void delete(String type, String key) {
		
		File file = new File(paths.get(type) + key);
		
		if (file.exists()) {
			file.delete();
			plugin.sendToLog(key + " deleted!");
		}
	}

	/**
	 * Gathers the names of all flat-files from a given directory.
	 * 
	 * @param type Key for the 'paths' HashMap to determine the working directory
	 * @return the names of all flat-files in that directory
	 */
	@Override
	public ArrayList<String> gatherDataset(String type) {
		
		File folder = new File(paths.get(type));
		ArrayList<String> keys = new ArrayList<String>();

		for (File obj : folder.listFiles()) {
			if (obj.isFile()) {
				keys.add(obj.getName());
			}
		}
		
		return keys;
	}
}
