package shizu.bukkit.nations.manager;

import java.util.HashMap;

import shizu.bukkit.nations.Nations;
import shizu.bukkit.nations.object.Group;
import shizu.bukkit.nations.object.NAWObject;
import shizu.bukkit.nations.object.User;

/**
 * Manages instances of the Group class and their interactions
 * between the server and data source.
 * 
 * @author Shizukesa
 */
public class GroupManagement extends Management {

	public GroupManagement(Nations instance) {
		
		super(instance);
		collection = new HashMap<String, NAWObject>();
		type = "group";
	}
	
	//TODO: Try moving this method to the parent for abstraction
	public Boolean groupExists(String key) {

		return (collection.containsKey(key)) ? true : false;
	}
	
	/**
	 * Fetches the Group for the provided group name, if it exists.
	 * 
	 * @param key The group name of the Group to get
	 * @return the Group with a matching name
	 */
	public Group getGroup(String key) {

		return (groupExists(key)) ? (Group) collection.get(key) : null;
	}
	
	/**
	 * Creates a nation Group for the commanding User and sets them
	 * in a position of leadership for that Group.
	 * 
	 * @param user The User creating the nation Group
	 * @param name The name of the new nation Group
	 */
	public void foundNation(User user, String name) {
		
		// TODO: Case sensitive check on nation name
		if (!groupExists(name)) {
			
			if (user.getNation().equals("")) {
				
				Group group = new Group(name);
				group.addMember(user.getKey());
				group.addLeader(user.getKey());
				user.setNation(name);
				collection.put(name, group);
				saveObject(name);
				user.message("The Nation of '" + name + "' has been founded!"); // TODO: broadcast to all?
			} else {
				user.message("You are already a member of a nation. You must leave that nation before you can found a new one!");
			}
		} else {
			user.message("A Nation with that name already exists!");
		}
		
	}
}
