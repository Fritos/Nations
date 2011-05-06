package shizu.bukkit.nations.manager;

import java.util.HashMap;

import org.bukkit.entity.Player;

import shizu.bukkit.nations.Nations;
import shizu.bukkit.nations.object.NAWObject;
import shizu.bukkit.nations.object.User;

/**
 * Manages users and their interactions with server objects
 * 
 * @author Shizukesa
 */
public class UserManagement extends Management {

	public UserManagement(Nations instance) {
		
		super(instance);
		collection = new HashMap<String, NAWObject>();
		type = "user";
	}
	
	public Boolean userExists(String key) {

		return (collection.containsKey(key)) ? true : false;
	}
	
	public User getUser(Player player) {
		
		String name = player.getDisplayName();
		return (userExists(name)) ? (User) collection.get(name) : null;
	}
	
	public Boolean isLeader(User user) {
		
		if (plugin.groupManager.groupExists(user.getNation())) {
			return (plugin.groupManager.getGroup(user.getNation()).hasLeader(user.getKey())) ? true : false;
		} 
		
		return false;
	}
	
	public void registerUser(Player player) {
		
		String name = player.getDisplayName();
		
		if (!userExists(name)) {
			
			User user = new User(player);
			user.setWorld(plugin.getWorld());
			user.setLocationKey(getLocationKey(player.getLocation()));
			collection.put(name, user);
			saveObject(name);
			loadObject(name);
			user.message("You have been registered for Nations at War!");
			plugin.sendToLog("User: " + name + " has been registered!");
		} else {
			plugin.sendToLog("User: " + name + " already exists!");
		}
	}
	
	public void loadUser(Player player) {
	
		User user = getUser(player);
		
		if (user != null) {
			user.setWorld(plugin.getWorld());
			user.setPlayer(player);
			user.setLocationKey(getLocationKey(player.getLocation()));
			plugin.userManager.saveObject(user.getKey());
			plugin.sendToLog("User: " + user.getKey() + " sucessfully loaded!");
		} else {
			
			plugin.sendToLog("Error loading user: " + player.getDisplayName());
		}
	}
	
	public Boolean updateLocation(Player player) {
		
		String name = player.getDisplayName();
		
		//if registered user
		if (userExists(name)) {
			
			User user = getUser(player);
			String locKey = plugin.plotManager.getLocationKey(player.getLocation());
			
			//if registered user moved to a new chunk
			if (!user.getLocationKey().equals(locKey)) {
				
				if (plugin.plotManager.plotExists(locKey)) {
					
					String locName = plugin.plotManager.getPlot(locKey).getLoctionName();
					
					if (!user.getCurrentLocationName().equals(locName)) {
						
						user.setCurrentLocationName(locName);
						player.sendMessage("[Entering] " + locName);
					}
				} else {
					if (user.getCurrentLocationName() != "") {
						
						player.sendMessage("[Leaving] " + user.getCurrentLocationName());
						user.setCurrentLocationName("");
					}
				}
				
				user.setLocationKey(locKey);
				return true;
			}
		}
		
		return false;
	}
}