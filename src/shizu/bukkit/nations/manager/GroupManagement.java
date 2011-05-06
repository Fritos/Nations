package shizu.bukkit.nations.manager;

import java.util.HashMap;

import shizu.bukkit.nations.Nations;
import shizu.bukkit.nations.object.Group;
import shizu.bukkit.nations.object.NAWObject;
import shizu.bukkit.nations.object.User;

public class GroupManagement extends Management {

	public GroupManagement(Nations instance) {
		
		super(instance);
		collection = new HashMap<String, NAWObject>();
		type = "group";
	}
	
	public Boolean groupExists(String key) {

		return (collection.containsKey(key)) ? true : false;
	}
	
	public Group getGroup(String key) {

		return (groupExists(key)) ? (Group) collection.get(key) : null;
	}
	
	public void foundNation(User user, String name) {
		
		// TODO: No case check on nation name
		// TODO: check if the player is already in a nation or owns a nation
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
				user.message("You are already a member of a nation. You must leave that nation before you can found your own!");
			}
		} else {
			user.message("A Nation with that name already exists!");
		}
		
	}
}
