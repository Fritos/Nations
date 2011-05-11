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

	//TODO Add to config
	private final Boolean LOCATION_NOTIFICATION = true;
	
	public UserManagement(Nations instance) {
		
		super(instance);
		collection = new HashMap<String, NAWObject>();
		type = "user";
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
		return (exists(name)) ? (User) collection.get(name) : null;
	}
	
	/**
	 * Checks to see if a User is a leader of their own nation group.
	 * 
	 * @param user The User to check
	 * @return true if the User is a leader of their own nation group,
	 * 		   false otherwise
	 */
	public Boolean isLeader(User user) {
		
		if (plugin.groupManager.exists(user.getNation())) {
			return (plugin.groupManager.getGroup(user.getNation()).hasLeader(user.getName())) ? true : false;
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
		
		if (!exists(name)) {
			
			User user = new User(player);
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
			String locKey = getLocationKey(player.getLocation());
			String locDesc = (plugin.plotManager.exists(locKey)) ? plugin.plotManager.getPlot(locKey).getLoctionDescription() : "";
			user.setWorld(plugin.getWorld());
			user.setPlayer(player);
			user.setLocationKey(locKey);
			user.setCurrentLocationDescription(locDesc); 
			collection.put(user.getName(), user);
			saveObject(user.getName());
			plugin.sendToLog("User: " + user.getName() + " sucessfully loaded!");
		} else {
			
			plugin.sendToLog("Error loading user: " + player.getDisplayName());
		}
	}
	
	/**
	 * Detects when a User has moved to a new chunk and updates
	 * their location data.
	 * 
	 * @param user The User to update
	 * @return true if the location data was updated, false otherwise
	 */
	public Boolean updateLocation(User user) {
		
		String locKey = plugin.plotManager.getLocationKey(user.getLocation());
		
		if (!user.getLocationKey().equals(locKey)) {
			
			if (LOCATION_NOTIFICATION) {
				updateLocationDescription(user, locKey);
			}
			
			user.setLocationKey(locKey);
			return true;
		}
		
		return false;
	}
	
	/**
	 * Detects when a User has moved into a new region. Then updates
	 * and notifies the User of their current regional location.
	 * 
	 * @param user The User to update
	 * @param locKey The location key of the User's current plot
	 */
	public void updateLocationDescription(User user, String locKey) {
		
		if (plugin.plotManager.exists(locKey)) {
			
			String locName = plugin.plotManager.getPlot(locKey).getLoctionDescription();
			
			if (!user.getCurrentLocationDescription().equals(locName)) {
				
				user.setCurrentLocationDescription(locName);
				user.message("[Entering] " + locName);
			}
		} else {
			if (user.getCurrentLocationDescription() != "") {
				
				user.message("[Leaving] " + user.getCurrentLocationDescription());
				user.setCurrentLocationDescription("");
			}
		}
	}
}