package shizu.bukkit.nations.object;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class Group extends NAWObject {

	private String name;
	private ArrayList<String> members;
	private ArrayList<String> plots;
	private String founder;
	
	// TODO: tie in with Permissions groups (String[] getGroups() ? )
	
	public Group(String n) {

		name = n;
		members = new ArrayList<String>();
		plots = new ArrayList<String>();
		founder = "";
	}
	
	/**
	 * Adds a user to this group/nation
	 * @param name the name of the user to add
	 */
	public void addMember(String name) {
		
		if (!members.contains(name)) {
			members.add(name);
		}
	}
	
	/**
	 * Adds a plot to this group/nation
	 * @param key the location key of the plot to add
	 */
	public void addPlot(String key) {
		
		if (!plots.contains(key)) {
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
	
	/**
	 * Checks to see if a plot is associated with this group/nation
	 * @param key the location key of the plot to check for
	 * @return true if the plot was found, false otherwise
	 */
	public Boolean hasPlot(String key) {
		return (plots.contains(key)) ? true : false;
	}
	
	/**
	 * Returns the name of the founder of this group/nation
	 * @return the name of the founder
	 */
	public String getFounder() {
		return founder;
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
		
		if (members.contains(name)) {
			members.remove(members.indexOf(name));
		}
	}
	
	/**
	 * Removes a plot from this group/nation
	 * @param key the location key of the plot to remove
	 */
	public void removePlot(String key) {
		
		if (plots.contains(key)) {
			plots.remove(plots.indexOf(key));
		}
	}
	
	/**
	 * Sets the founder/leader of this group/nation
	 * @param name the name of the user to become founder
	 */
	public void setFounder(String name) {
		founder = name;
	}
}
