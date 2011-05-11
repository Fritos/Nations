package shizu.bukkit.nations.object;

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
	private transient Player player;
	
	public User(Player n) {
		
		player = n;
		name = n.getDisplayName();
		nationKey = "";
		currentLocation = "";
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
