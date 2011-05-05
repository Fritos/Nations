package shizu.bukkit.nations.manager;

import java.util.HashMap;

import org.bukkit.entity.Player;

import shizu.bukkit.nations.Nations;
import shizu.bukkit.nations.object.NAWObject;
import shizu.bukkit.nations.object.Plot;
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

	public void registerUser(Player player) {
		
		String name = player.getDisplayName();
		
		if (!collection.containsKey(name)) {
			User user = new User(player);
			user.setLocationKey(plugin.plotManager.getLocationKey(player.getLocation()));
			collection.put(name, user);
			this.saveObject(name);
			player.sendMessage("You have been registered for Nations at War!");
			plugin.sendToLog("User: " + name + " has been registered!");
		}
	}
	
	public Boolean loadUser(Player player) {
	
		String name = player.getDisplayName();
		
		if (!collection.containsKey(name)) {
			collection.remove(name);
		}
		
		return this.loadObject(name);
	}
	
	// TODO: clean up, oh gawd...
	public Boolean updateLocation(Player player) {
		
		String name = player.getDisplayName();
		
		//if registered user
		if (collection.containsKey(name)) {
			
			User user = (User)collection.get(name);
			String locKey = plugin.plotManager.getLocationKey(player.getLocation());
			
			//if registered user moved to a new chunk
			if (!user.getLocationKey().equals(locKey)) {
				
				if (plugin.plotManager.collection.containsKey(locKey)) {
					
					String locName = ((Plot) plugin.plotManager.collection.get(locKey)).getLoctionName();
					
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