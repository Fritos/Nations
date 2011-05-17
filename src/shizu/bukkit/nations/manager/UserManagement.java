package shizu.bukkit.nations.manager;

import java.util.HashMap;

import org.bukkit.entity.Player;

import shizu.bukkit.nations.Nations;
import shizu.bukkit.nations.object.NAWObject;
import shizu.bukkit.nations.object.Plot;
import shizu.bukkit.nations.object.User;

import com.iConomy.*;
import com.iConomy.system.Holdings;

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
	 * Fetches the User instance for the provided Player, if it exists
	 * 
	 * @param name The Player name to fetch for
	 * @return the User instance of the provided player, null if no
	 * 		   matching instance exists
	 */
	public User getUser(String name) {
		
		return (exists(name)) ? (User) collection.get(name) : null;
	}
	
	/**
	 * Taxes the user by the percentage of his nation's tax rate.
	 */
	public void taxUser(String name, double amount) {
		Holdings balance = iConomy.getAccount(name).getHoldings();
		Holdings treasury = iConomy.getAccount(getUser(name).getNation()).getHoldings();
		double tax = amount * (plugin.groupManager.getTaxRate(this.getUser(name)) / 100);
		
		balance.add(amount - tax);
		treasury.add(tax);
		
		getUser(name).message("You have received $" + amount + ". ($" + (amount - tax) + " after tax.)");
	}
	
	/**
	 * Checks to see if a User is a leader of their own nation group.
	 * 
	 * @param user The User to check
	 * @return true if the User is a leader of their own nation group,
	 * 		   false otherwise
	 */
	public boolean isLeader(User user) {
		
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
			
			user.message("Welcome to Nations at War!");
			user.message("Type: '/naw help' to see a list of commands.");
			
			if (user.hasInvites()) {
				user.message("You have nation invites!");
				user.message("Type: '/naw invites' to see the nation(s) you've been invited to!");
			}
			plugin.sendToLog("User: " + user.getName() + " sucessfully loaded!");
		} else {
			
			plugin.sendToLog("Error loading user: " + player.getDisplayName());
		}
	}
	
	/**
	 * Accepts an invite to join a particular nation. Upon acceptance, the user
	 * becomes a member of that nation.
	 * 
	 * @param user The commanding user
	 * @param nation The accepted invitation's nation
	 * @return true if the invitation was accepted, false otherwise
	 */
	public boolean acceptInvite(User user, String nation) {
		
		if (!plugin.groupManager.exists(nation)) {
			user.message("That nation does not exist or no longer exists!");
			return false;
		}	
		
		if (user.hasInvite(nation)) {
			
			plugin.groupManager.messageGroup(nation, user.getName() + " has joined " + nation + "!");
			plugin.groupManager.joinNation(user, nation);
			user.clearInvites();
			return true;
		} else {
			user.message("You have not been invited to join that nation!");
			return false;
		}
	}
	
	/**
	 * Detects when a User has moved to a new chunk and updates
	 * their location data.
	 * 
	 * @param user The User to update
	 * @return true if the location data was updated, false otherwise
	 */
	public boolean updateLocation(User user) {
		
		String locKey = plugin.plotManager.getLocationKey(user.getLocation());
		
		if (!user.getLocationKey().equals(locKey)) {
			
			if (Boolean.parseBoolean(plugin.config.get("location_spam"))) {
				updateLocationDescription(user, locKey);
			}

			updateResaleDescription(user, locKey);
			user.setLocationKey(locKey);
			return true;
		}
		
		return false;
	}
	
	//TODO: find a more efficient way
	/**
	 * Updates the location description for all users at the given location key
	 * 
	 * @param locKey The location key
	 */
	public void setLocationDescriptionForAll(String locKey) {
		
		for (String player : collection.keySet()) {
			
			User user = getUser(player);
			
			if (user.getLocationKey().equals(locKey)) {
				
				updateLocationDescription(user, locKey);
			}
		}
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
	
	/**
	 * Notifies a User if the provided Plot is available for rent or sale
	 * 
	 * @param user The user to notify
	 * @param locKey The location key of the Plot in question
	 */
	public void updateResaleDescription(User user, String locKey) {
		
		if (plugin.plotManager.exists(locKey)) {
			
			Plot plot = plugin.plotManager.getPlot(locKey);
			if (plot.getRentStatus()) { user.message("[For Rent] "); } //TODO: Add price
			if (plot.getSaleStatus()) { user.message("[For Sale] "); }
		}
	}
}