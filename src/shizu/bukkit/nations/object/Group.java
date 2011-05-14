package shizu.bukkit.nations.object;

import java.util.ArrayList;

/**
 * The Nations at War Group object.
 * 
 * @author Shizukesa
 */
@SuppressWarnings("serial")
public class Group extends NAWObject {

	private String name;
	private ArrayList<String> members;
	private ArrayList<String> leaders;
	private ArrayList<String> plots;
	
	// TODO: tie in with Permissions groups (String[] getGroups() ? )
	
	public Group(String n) {

		name = n;
		members = new ArrayList<String>();
		leaders = new ArrayList<String>();
		plots = new ArrayList<String>();
	}
	
	/**
	 * Adds a user to this group/nation.
	 * 
	 * @param name The name of the user to add
	 */
	public void addMember(String name) {
		
		if (!hasMember(name)) {
			members.add(name);
		}
	}
	
	/**
	 * Add a user to the leaders list of this
	 * group/nation.
	 * 
	 * @param name The name of the User to add
	 */
	public void addLeader(String name) {
		
		if (hasMember(name) && !hasLeader(name)) {
			leaders.add(name);
		}
	}
	
	/**
	 * Adds a Plot to the Plot ownership list of
	 * this group/nation
	 * 
	 * @param key The location key of the plot to add
	 */
	public void addPlot(String key) {
		
		if (!hasPlot(key)) {
			plots.add(key);
		}
	}
	
	/**
	 * Checks to see if a User is associated with this group/nation.
	 * 
	 * @param name The name of the User to check
	 * @return true if the User was found, false otherwise
	 */
	public Boolean hasMember(String name) {
		
		return (members.contains(name)) ? true : false;
	}
	
	/**
	 * Checks to see if a User is a leader of this group/nation.
	 * 
	 * @param name The name of the User to check
	 * @return true if the User was found, false otherwise
	 */
	public Boolean hasLeader(String name) {
		
		return (leaders.contains(name)) ? true : false;
	}
	
	/**
	 * Checks to see if a Plot is owned by this group/nation.
	 * 
	 * @param key The location key of the Plot to check for
	 * @return true if the Plot was found, false otherwise
	 */
	public Boolean hasPlot(String key) {
		
		return (plots.contains(key)) ? true : false;
	}
	
	/**
	 * Returns the name of this group/nation.
	 * 
	 * @return the group/nation name
	 */
	public String getName() {
		
		return name;
	}
	
	/**
	 * Returns all members associated with this group.
	 * 
	 * @return member's User names
	 */
	public ArrayList<String> getMembers() {
		
		return members;
	}
	
	/**
	 * Returns all leaders associated with this group.
	 * 
	 * @return leader's User names
	 */
	public ArrayList<String> getLeaders() {
		
		return leaders;
	}
	
	/**
	 * Returns all Plots associated with this group.
	 * 
	 * @return Plot location keys
	 */
	public ArrayList<String> getPlots() {
		
		return plots;
	}
	
	/**
	 * Removes a User from this group/nation's
	 * member list.
	 * 
	 * @param name The name of the User to remove
	 */
	public void removeMember(String name) {
		
		if (hasMember(name)) {
			members.remove(members.indexOf(name));
		}
	}
	
	/**
	 * Removes a User from this group/nation's
	 * leadership list.
	 * 
	 * @param name The name of the User to remove
	 */
	public void removeLeader(String name) {
		
		if (hasLeader(name)) {
			leaders.remove(leaders.indexOf(name));
		}
	}
	
	/**
	 * Removes a Plot from this group/nation's
	 * Plot ownership list.
	 * 
	 * @param key The location key of the Plot to remove
	 */
	public void removePlot(String key) {
		
		if (hasPlot(key)) {
			plots.remove(plots.indexOf(key));
		}
	}
}
