package shizu.bukkit.nations.object;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * The Nations at War User object.
 * 
 *  @author Shizukesa
 */
@SuppressWarnings("serial")
public class User extends NAWObject {

	private String name;
	private String nationKey;
	private String locationKey;
	private String currentLocation;
	private ArrayList<String> nationInvites;
	private transient Player player;
	
	public User(Player n) {
		
		player = n;
		name = n.getDisplayName();
		nationKey = "";
		currentLocation = "";
		nationInvites = new ArrayList<String>();
	}
	
	/**
	 * Add an invite to the User's invite collection.
	 * 
	 * @param nation The inviting Nation
	 */
	public void addInvite(String nation) {
		
		nationInvites.add(nation);
		message("You have been invited to join " + nation + "!");
		message("Type: '/naw invites accept " + nation + "' to join");
	}
	
	/**
	 * Clears all invites from the User's invite collection.
	 */
	public void clearInvites() {
		
		nationInvites.clear();
	}
	
	//TODO: bug, does not work
	/**
	 * View all invites in the invite collection
	 */
	public void viewInvites() {
		
		if (hasInvites()) {
			String invites = "";
			
			for (String nation : nationInvites) {
				
				invites += nation + " ";
			}
			
			message("You have been invited to join: " + invites);
			message("Type: '/naw invites accept <nation name>' to join that nation!");
		} else {
			message("You have no invites!");
		}
	}
	
	/**
	 * Checks to see if the User has an invite from a particular Nation.
	 * 
	 * @param nation The Nation to check for
	 * @return true if the invitation exists, false otherwise
	 */
	public Boolean hasInvite(String nation) {
		
		return nationInvites.contains(nation);
	}
	
	/**
	 * Checks to see if the User has any invites.
	 * 
	 * @return true if any invitations exist, false otherwise
	 */
	public Boolean hasInvites() {
		
		return !nationInvites.isEmpty();
	}
	
	/**
	 * Sends a message to the User.
	 * 
	 * @param message The message to send
	 */
	public void message(String message) {
		
		player.sendMessage(message);
	}
	
	/**
	 * Matches this User object with a Player.
	 * 
	 * @param p The Player to match this User to
	 */
	public void setPlayer(Player p) {
		
		player = p;
	}
	
	/**
	 * Returns the location of the User.
	 * 
	 * @return the location of the User
	 */
	public Location getLocation() {
		
		return player.getLocation();
	}
	
	/**
	 * Fetches the User's name.
	 * 
	 * @return the key
	 */
	public String getName() {
		
		return name;
	}
	
	/**
	 * Fetches the nation that the User belongs to.
	 * 
	 * @return the nation
	 */
	public String getNation() {
		
		return nationKey;
	}
	
	/**
	 * Fetches the location key of the Plot the User is currently in.
	 * 
	 * @return the location key
	 */
	public String getLocationKey() {
		
		return locationKey;
	}
	
	/**
	 * Fetches the territory title of the User's current location.
	 * 
	 * @return the teritory title
	 */
	public String getCurrentLocationDescription() {
		
		return currentLocation;
	}
	
	/**
	 * Sets the Nation the User belongs to.
	 * 
	 * @param n the new nation
	 */
	public void setNation(String n) {
		
		nationKey = n;
	}
	
	/**
	 * Sets the location key of the Plot the User is currently in.
	 * 
	 * @param l the location key
	 */
	public void setLocationKey(String l) {
		
		locationKey = l;
	}
	
	/**
	 * Sets the territory title of the User's current location.
	 * 
	 * @param c the territory title
	 */
	public void setCurrentLocationDescription(String c) {
		
		currentLocation = c;
	}
}
