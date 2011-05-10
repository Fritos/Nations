package shizu.bukkit.nations.manager;

import java.util.HashMap;

import org.bukkit.entity.Player;

import shizu.bukkit.nations.Nations;
import shizu.bukkit.nations.object.NAWObject;
import shizu.bukkit.nations.object.User;

/**
 * Manages instances of the User class and their interactions
 * between the server and data source.
 * 
 * @author Shizukesa
 */
public class UserManagement extends Management {

	public UserManagement(Nations instance) {
		
		super(instance);
		collection = new HashMap<String, NAWObject>();
		type = "user";
	}
	
	//TODO: Try moving this method to the parent for abstraction
	public Boolean userExists(String key) {

		return (collection.containsKey(key)) ? true : false;
	}
	
	/**
	 * Fetches the User instance for the provided Player, if it exists
	 * 
	 * @param player The Player to fetch for
	 * @return the User instance of the provided player, null if no
	 * 		   matching instance exists
	 */
	public User getUser(Player player) {
		
		String name = player.getDisplayName();
		return (userExists(name)) ? (User) collection.get(name) : null;
	}
	
	/**
	 * Checks to see if a User is a leader of their own nation group.
	 * 
	 * @param user The User to check
	 * @return true if the User is a leader of their own nation group,
	 * 		   false otherwise
	 */
	public Boolean isLeader(User user) {
		
		if (plugin.groupManager.groupExists(user.getNation())) {
			return (plugin.groupManager.getGroup(user.getNation()).hasLeader(user.getKey())) ? true : false;
		} 
		
		return false;
	}
	
	/**
	 * Creates a User instance for the provided Player; loads that
	 * instance into 'collection' and saves it to the data source.
	 * 
	 * @param player The Player to register
	 */
	public void registerUser(Player player) {
		
		String name = player.getDisplayName();
		
		if (!userExists(name)) {
			
			User user = new User(player);
			user.setWorld(plugin.getWorld());
			user.setLocationKey(getLocationKey(player.getLocation()));
			collection.put(name, user);
			saveObject(name);
			user.message("You have been registered for Nations at War!");
			plugin.sendToLog("User: " + name + " has been registered!");
		} else {
			plugin.sendToLog("User: " + name + " already exists!");
		}
	}
	
	/**
	 * Connects a matching User instance with the provided Player, if one 
	 * exists. Then sets the User up for functionality in the current World.
	 * 
	 * @param player The player to match
	 */
	public void setupUser(Player player) {
	
		User user = getUser(player);
		
		if (user != null) {
			user.setWorld(plugin.getWorld());
			user.setPlayer(player);
			user.setLocationKey(getLocationKey(player.getLocation()));
			saveObject(user.getKey());
			plugin.sendToLog("User: " + user.getKey() + " sucessfully loaded!");
		} else {
			
			plugin.sendToLog("Error loading user: " + player.getDisplayName());
		}
	}
	
	//TODO: Change to User parameter, try to make notifications their own method
	/**
	 * 
	 * @param player
	 * @return
	 */
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
						//TODO: Fix "leaving" bug when connecting to the server
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