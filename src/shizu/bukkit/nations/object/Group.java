package shizu.bukkit.nations.object;

import java.util.ArrayList;

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
	 * Adds a user to this group/nation
	 * @param name the name of the user to add
	 */
	public void addMember(String name) {
		
		if (!hasMember(name)) {
			members.add(name);
		}
	}
	
	public void addLeader(String name) {
		
		if (hasMember(name) && !hasLeader(name)) {
			leaders.add(name);
		}
	}
	
	/**
	 * Adds a plot to this group/nation
	 * @param key the location key of the plot to add
	 */
	public void addPlot(String key) {
		
		if (!hasPlot(key)) {
			plots.add(key);
		}
	}
	
	/**
	 * Checks to see if a user is associated with this group/nation
	 * @param name the name of the user to check for
	 * @return true if the user was found, false otherwise
	 */
	public Boolean hasMember(String name) {
		
		return (members.contains(name)) ? true : false;
	}
	
	public Boolean hasLeader(String name) {
		
		return (leaders.contains(name)) ? true : false;
	}
	
	/**
	 * Checks to see if a plot is associated with this group/nation
	 * @param key the location key of the plot to check for
	 * @return true if the plot was found, false otherwise
	 */
	public Boolean hasPlot(String key) {
		
		return (plots.contains(key)) ? true : false;
	}
	
	/**
	 * Returns the key of this group/nation (group name)
	 * @return the group/nation name
	 */
	public String getKey() {
		
		return name;
	}
	
	/**
	 * Returns all members associated with this group
	 * @return member names
	 */
	public ArrayList<String> getMembers() {
		
		return members;
	}
	
	public ArrayList<String> getLeaders() {
		
		return leaders;
	}
	
	/**
	 * Returns all plots associated with this group
	 * @return plot location keys
	 */
	public ArrayList<String> getPlots() {
		
		return plots;
	}
	
	/**
	 * Removes a member from this group/nation
	 * @param name the name of the member to remove
	 */
	public void removeMember(String name) {
		
		if (hasMember(name)) {
			members.remove(members.indexOf(name));
		}
	}
	
	public void removeLeader(String name) {
		
		if (hasLeader(name)) {
			leaders.remove(leaders.indexOf(name));
		}
	}
	
	/**
	 * Removes a plot from this group/nation
	 * @param key the location key of the plot to remove
	 */
	public void removePlot(String key) {
		
		if (hasPlot(key)) {
			plots.remove(plots.indexOf(key));
		}
	}
}
