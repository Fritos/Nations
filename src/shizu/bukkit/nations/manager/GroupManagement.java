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
	
	/**
	 * Fetches the Group for the provided group name, if it exists.
	 * 
	 * @param key The group name of the Group to get
	 * @return the Group with a matching name
	 */
	public Group getGroup(String key) {

		return (exists(key)) ? (Group) collection.get(key) : null;
	}
	
	/**
	 * Creates a nation Group for the commanding User and sets them
	 * in a position of leadership for that Group.
	 * 
	 * @param user The User creating the nation Group
	 * @param name The name of the new nation Group
	 */
	public void foundNation(User user, String name) {
		
		// TODO: Case insensitive check on nation name
		if (!exists(name)) {
			
			if (user.getNation().equals("")) {
				
				Group group = new Group(name);
				group.addMember(user.getName());
				group.addLeader(user.getName());
				user.setNation(name);
				collection.put(name, group);
				saveObject(name);
				plugin.messageAll("The Nation of '" + name + "' has been founded!");
			} else {
				user.message("You are already a member of a nation. You must leave that nation before you can found a new one!");
			}
		} else {
			user.message("A Nation with that name already exists!");
		}
		
	}
	
	/**
	 * Changes diplomatic relationship with another nation.
	 * 
	 * @param user The User changing the diplomacy of his nation
	 * @param nation The Nation the user wants to change his diplomacy with
	 * @param status The status of the diplomatic relationship (ally, neutral, enemy)
	 */
	public void changeStatus(User user, String nation, String status) {
		Group group = getGroup(user.getNation());
		
		if (this.exists(nation) == true) {
			
			if (status.equalsIgnoreCase("ally")) {
				group.addAlly(nation);
				user.message(nation + " is now your ally.");
			}
			else if (status.equalsIgnoreCase("neutral")) {
				group.addNeutral(nation);
				user.message(nation + " is now neutral.");
			}
			else if (status.equalsIgnoreCase("enemy")) {
				group.addEnemy(nation);
				user.message(nation + " is now your enemy.");
			}
			else {
				user.message("Status parameters: 'ally', 'neutral', and 'enemy'");
				user.message("For example, '/naw diplomacy status Kentucky enemy'");
			}
		}
		
		else {
			user.message(nation + " does not exist.");
		}
	}
	
	public Boolean inviteUserToNation(User user, String invited) {
		
		if (!plugin.userManager.exists(invited)) {
			
			user.message("That user does not exist or is not registered!");
			return false;
		}
		
		User invitee = plugin.userManager.getUser(invited);
		
		if (plugin.userManager.isLeader(user)) {
			
			if (invitee.getNation().equals("")) {
				
				invitee.addInvite(user.getNation());
				user.message("Invitation sent!");
				return true;
			} else {
				user.message("This user already belongs to a nation!");
				return false;
			}
		} else {
			user.message("You must be a leader to invite a user to the nation!");
			return false;
		}
	}
	
	public Boolean kickUserFromNation(User user, String kicked) {
		
		if (!plugin.userManager.exists(kicked)) {
			
			user.message("That user does not exist or is not registered!");
			return false;
		}
		
		User kickee = plugin.userManager.getUser(kicked);
		
		if (plugin.userManager.isLeader(user)) {
			
			if (kickee.getNation().equals(user.getNation())) {
				
				Group group = getGroup(user.getNation());
				kickee.message("You have been kicked from " + user.getNation() + "!");
				kickee.setNation("");
				group.removeMember(kickee.getName());
				group.removeLeader(kickee.getName());
				plugin.groupManager.messageGroup(user.getNation(), kicked + " has been kicked from the nation!");
				return true;
			} else {
				user.message("This user does not belong to your nation, and cannot be kicked!");
				return false;
			}
		} else {
			user.message("You must be a leader to kick a user from the nation!");
			return false;
		}
	}
	
	//TODO: test	
	/**
	 * Allows leaders to promote other members of the same nation to leader status.
	 * 
	 * @param user The User that is promoting the member.
	 * @param promoted The User that is being promoted
	 */
	public Boolean promoteUser(User user, String promoted) {

		User member = plugin.userManager.getUser(promoted);
		Group nation = this.getGroup(user.getNation());
		
		if (user.getNation() == member.getNation() && plugin.userManager.isLeader(member) == false) {
			nation.addLeader(promoted);
		}
		else {
			user.message("That is not a valid member to promote.");
		}
		return true;
	}
	
	//TODO: test
	/**
	 * Allows leaders to demote other members of the same nation from leader status.
	 * 
	 * @param user The User that is demoting the leader.
	 * @param promoted The User that is being demoted
	 */
	public Boolean demoteUser(User user, String demoted) {

		User member = plugin.userManager.getUser(demoted);
		Group nation = this.getGroup(user.getNation());
		
		if (user.getNation() == member.getNation() && plugin.userManager.isLeader(member) == true) {
			nation.removeLeader(demoted);
		}
		else {
			user.message("That is not a valid member to demote.");
		}
		return true;
	}
	
	public Boolean joinNation(User user, String nation) {
	
		if (!exists(nation)) {
			
			user.message("No nation with that name exists!");
			return false;
		}
		
		if (user.getNation().equals("")) {
			
			Group group = getGroup(nation);
			user.setNation(nation);
			group.addMember(user.getName());	
			user.message("You have joined " + nation + "!");
			return true;
		} else {
			user.message("You must first leave your nation before you can join another!");
		}
		
		return false;
	}
	
	public Boolean leaveNation(User user) {
	
		if (!user.getNation().equals("")) {
			
			Group group = getGroup(user.getNation());
			user.message("You have left " + user.getNation() + "!");
			user.setNation("");
			group.removeMember(user.getName());
			group.removeLeader(user.getName());
			return true;
		} else {
			user.message("You cannot leave a nation you are not a part of!");
		}
		
		return false;
	}
	
	public void disbandNation() {
		//PLACEHOLDER for JERRIK
	}
	
	/**
	 * Sends a message to all members in the provided Group.
	 * 
	 * @param key The name of the Group
	 * @param message The message to send
	 */
	public void messageGroup(String key, String message) {
		
		Group group = getGroup(key);
		
		for (String member : group.getMembers()) {
			
			User user = plugin.userManager.getUser(member);
			user.message("[" + key + "]: " + message);
		}
	}
	
	public void createGroup() {
		//PLACEHOLDER
	}
	
	public void deleteGroup() {
		//PLACEHOLDER
	}
}
