package shizu.bukkit.nations.manager;

import java.util.HashMap;

import org.bukkit.entity.Player;

import shizu.bukkit.nations.Nations;
import shizu.bukkit.nations.object.Group;
import shizu.bukkit.nations.object.NAWObject;

public class GroupManagement extends Management {

	public GroupManagement(Nations instance) {
		
		super(instance);
		collection = new HashMap<String, NAWObject>();
		type = "group";
	}
	
	public void foundNation(Player player, String name) {
		
		// TODO: No case check on nation name
		// TODO: check if the player is already in a nation or owns a nation
		if (!collection.containsKey(name)) {
			
			Group group = new Group(name);
			group.addMember(player.getDisplayName());
			group.setFounder(player.getDisplayName());
			collection.put(name, group);
			this.saveObject(name);
			player.sendMessage("The Nation of: " + name + " has been founded!"); // TODO: broadcast to all?
		} else {
			player.sendMessage("A Nation with that name already exists!");
		}
		
	}
}
