package shizu.bukkit.nations.object;

import org.bukkit.entity.Player;

/**
 * The Nations at War User object. Stores vital User information
 * for use by the server
 * 
 *  @author Shizukesa
 */
@SuppressWarnings("serial") //Read up on this!
public class User extends NAWObject {

	private String key;
	private String nationKey;
	private String locationKey;
	private String currentLocation;
	
	public User(Player n) {
		key = n.getDisplayName();
	}
	
	/**
	 * Fetches the key (user name)
	 * @return the key
	 */
	public String getKey() {
		return key;
	}
	
	/**
	 * Fetches the nation that the user belongs to
	 * @return the nation
	 */
	public String getNation() {
		return nationKey;
	}
	
	/**
	 * Fetches the Plot key id of the plot the user is currently in,
	 * the value is "" if the user is not in a plot
	 * @return
	 */
	public String getLocationKey() {
		return locationKey;
	}
	
	public String getCurrentLocationName() {
		return currentLocation;
	}
	
	/**
	 * Sets the Nation the user belongs to
	 * @param n
	 */
	public void setNation(String n) {
		nationKey = n;
	}
	
	/**
	 * Sets the Plot key id of the plot the user is currently in,
	 * the value is "" if the user is not in a plot
	 */
	public void setLocationKey(String l) {
		locationKey = l;
	}
	
	public void setCurrentLocationName(String c) {
		currentLocation = c;
	}
}
