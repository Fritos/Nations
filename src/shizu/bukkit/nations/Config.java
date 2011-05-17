package shizu.bukkit.nations;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class Config {

	private Properties properties = new Properties();
	private Nations plugin;
	
	public Config(Nations instance) {
		
		//ADD DEFAULT PROPERTIES FILE VALUES HERE
		properties.put("use_mysql", "false");
		properties.put("mysql_url", "localhost");
		properties.put("mysql_port", "3306");
		properties.put("mysql_user", "");
		properties.put("mysql_pass", "");
		properties.put("mysql_db_name", "mc_bukkit_naw");
		properties.put("location_spam", "true");
		properties.put("auto_registration", "true");
		properties.put("world_name", "world");	
		
		plugin = instance;
		check();
	}
	
	public String get(String key) {
		
		return properties.getProperty(key);
	}
	
	private void check() {
		
		File folder = new File("plugins\\naw\\");
		File file = new File("plugins\\naw\\naw.properties"); 
		
		try {
			if (!folder.exists()) {
			
				folder.mkdirs();
			}
			
			if (!file.exists()) {
				
				file.createNewFile();
				BufferedWriter out = new BufferedWriter(new FileWriter(file));
				
				for (Object key : properties.keySet()) {
					
					out.write(key + " = " + properties.getProperty((String)key));
					out.newLine();
				}
				out.close();
				plugin.sendToLog("naw.properties not found, creating it with default configurations");
				
			} else {
				
				properties.load(new FileInputStream(file));
				plugin.sendToLog("naw.properties file loaded");
			}
			

		} catch (IOException e) {
			plugin.sendToLog(e.getMessage());
		}
	}
}
