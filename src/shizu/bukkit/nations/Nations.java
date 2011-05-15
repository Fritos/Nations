package shizu.bukkit.nations;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import shizu.bukkit.nations.event.NationsBlockListener;
import shizu.bukkit.nations.event.NationsUserListener;
import shizu.bukkit.nations.manager.GroupManagement;
import shizu.bukkit.nations.manager.PlotManagement;
import shizu.bukkit.nations.manager.UserManagement;
import shizu.bukkit.nations.object.User;

/**
 * Nations At War plugin class
 * 
 * @author Shizukesa
 */
public class Nations extends JavaPlugin {
	
	// TODO: Add color to player notifications

	private static final Logger log = Logger.getLogger("Minecraft");
	
	public Properties properties = new Properties();
	public PlotManagement plotManager = new PlotManagement(this);
	public UserManagement userManager = new UserManagement(this);
	public GroupManagement groupManager = new GroupManagement(this);
	public NationsBlockListener blockListener = new NationsBlockListener(this);
	public NationsUserListener userListener = new NationsUserListener(this);
    
	public void onEnable() {
		
		//TODO: create wrapper class for Properties; defaults, detect and create, etc.
		try {
		    properties.load(new FileInputStream("plugins\\naw.properties"));
		    sendToLog("Config file loaded");
		} catch (IOException e) {
			sendToLog("Unable to load config file!");
		}
		
		PluginManager pm = getServer().getPluginManager();

		pm.registerEvent(Event.Type.BLOCK_DAMAGE, blockListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.BLOCK_BREAK, blockListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.BLOCK_PLACE, blockListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_JOIN, userListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_QUIT, userListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_KICK, userListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_MOVE, userListener, Event.Priority.High, this);

		plotManager.loadAll();
		groupManager.loadAll();
		userManager.loadAll();
		sendToLog("Nations At War Plugin Loaded");
	}

	public void onDisable() {
		
		plotManager.saveAll();
		groupManager.saveAll();
		userManager.saveAll();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		
		String name = ((Player) sender).getDisplayName();
		User user = (userManager.exists(name)) ? userManager.getUser((Player) sender) : null;
		
		ChatColor yellow = ChatColor.getByCode(14);
		ChatColor white = ChatColor.getByCode(15);
		
		if (user != null) {
			
			if (commandLabel.equalsIgnoreCase("naw")) {
				
				if (args[0].equalsIgnoreCase("help")) {
					user.message(yellow + "Main command list: invites, plot, nation, diplomacy");
					user.message(yellow + "For more information, type /naw <command> help");
				}
				
				if (args[0].equalsIgnoreCase("invites")) {
					
					if (args[1].equalsIgnoreCase("help")) {
						user.message(yellow + "To list your invites, just type /naw invites");
						user.message(yellow + "Sub-command list");
						user.message(yellow + "accept <nation>" + white + " - Adds you to the <nation> that invited you.");
						user.message(yellow + "clear" + white + " - Clears all invites from your invite list.");
					}
					
					if (args[1].equalsIgnoreCase("")) {
						user.viewInvites();
					}
					
					if (args[1].equalsIgnoreCase("accept")) {
						userManager.acceptInvite(user, args[2]);
					}
					
					if (args[1].equalsIgnoreCase("clear")) {
						user.clearInvites();
					}
				}
				
				if (args[0].equalsIgnoreCase("plot")) {
					
					if (args[1].equalsIgnoreCase("help")) {
						user.message(yellow + "Sub-command list");
						user.message(yellow + "claim" + white + " - Claims your nation's ownership on the land you're standing on.");
						user.message(yellow + "raze" + white + " - Removes your nation's claim on the land you're in.");
						user.message(yellow + "sell" + white + " - Allows other nation's to purchase the land you're in.");
						user.message(yellow + "rent" + white + " - Rents the land you're in if it's for sell.");
						user.message(yellow + "buy" + white + " - Buys the land you're in if it's for sell.");
						user.message(yellow + "region <name>" + white + " - Names the plot you're in. (Changes enter/leave message)");
					}
					
					if (args[1].equalsIgnoreCase("claim")) {
						plotManager.claimPlot(user);
					}
					
					if (args[1].equalsIgnoreCase("raze")) {
						plotManager.razePlot(user);
					}
					
					if (args[1].equalsIgnoreCase("sell")) {
						plotManager.resellPlot(user);
					}
					
					if (args[1].equalsIgnoreCase("rent")) {
						plotManager.rentPlot(user);
					}
					
					if (args[1].equalsIgnoreCase("buy")) {
						plotManager.buyPlot(user);
					}
					
					if (args[1].equalsIgnoreCase("region")) {
						plotManager.setRegion(user, args[2]);
					}
				}
				
				if (args[0].equalsIgnoreCase("nation")) {
					
					if (args[1].equalsIgnoreCase("help")) {
						user.message(yellow + "Sub-command list");
						user.message(yellow + "found <nation name>" + white + " - Creates a new nation.");
						user.message(yellow + "invite <user>" + white + " - Invites the <user> to join your nation.");
						user.message(yellow + "kick <member>" + white + " - Kicks the member from the nation.");
						user.message(yellow + "promote <member>" + white + " - Promotes the member to leader status.");
						user.message(yellow + "demote <member>" + white + " - Removes the member's leader status.");
						user.message(yellow + "leave" + white + " - Leaves your current nation.");
						user.message(yellow + "disband" + white + " - Destroys your nation and all the plots it has claimed.");
						user.message(yellow + "rename <nation name>" + white + " - Renames your nation.");
						user.message(yellow + "tax <rate>" + white + " - Sets the percentage of all member income to be stored in treasury.");
					}
					
					if (args[1].equalsIgnoreCase("found")) {
						groupManager.foundNation(user, args[2]);
					}
					
					if (args[1].equalsIgnoreCase("invite")) {
						groupManager.inviteUserToNation(user, args[2]);
					}
					
					if (args[1].equalsIgnoreCase("kick")) {
						groupManager.kickUserFromNation(user, args[2]);
					}
					
					if (args[1].equalsIgnoreCase("promote")) {
						groupManager.promoteUser(user, args[2]);
					}
					
					if (args[1].equalsIgnoreCase("demote")) {
						groupManager.demoteUser(user, args[2]);
					}
					
					if (args[1].equalsIgnoreCase("leave")) {
						groupManager.leaveNation(user);
					}
					
					if (args[1].equalsIgnoreCase("disband")) {
						//PLACEHOLDER - disbands the nation; kick all members, raze all plots, delete object
					}
					
					if (args[1].equalsIgnoreCase("rename")) {
						//PLACEHOLDER - renames the nation
					}
					
					if (args[1].equalsIgnoreCase("tax")) {
						//PLACEHOLDER - sets the tax rate for the nation; used for renting/buying plots, maybe more?
					}
				}
				
				/* Diplomacy Section
				 * 
				 * Info - Lists your nation's allies and enemies
				 * Ally - Allies the subsequent nation
				 * */
				if (args[0].equalsIgnoreCase("diplomacy")) {
					
					if (args[1].equalsIgnoreCase("help")) {
						user.message(yellow + "Sub-command list");
						user.message(yellow + "status <nation> <status>" + white + " - Changes diplomatic relationship with <nation>");
						user.message(white + "          - <status> can either be 'ally', 'neutral', or 'enemy'");
					}
					
					if (args[1].equalsIgnoreCase("info")) {
						ArrayList<String> allies = groupManager.getGroup(user.getNation()).getAllies();
						ArrayList<String> enemies = groupManager.getGroup(user.getNation()).getEnemies();
						String allyList = "";
						String enemyList = "";
						
						user.message(ChatColor.getByCode(5) + "YOUR NATION: " + (groupManager.exists(user.getNation()) ? user.getNation() : "No Nation"));
						
						if (allies.size() > 0) {
							for(int i=0;i<allies.size();i++)
							{
								allyList = allyList + allies.get(i) + ", ";
							}
							user.message(ChatColor.getByCode(2) + "Allies: " + allyList.substring(0, allyList.length() - 2) + ".");
						}
						else {
							user.message(ChatColor.getByCode(2) + "Allies: None");
						}
						
						if (enemies.size() > 0) {
							for(int i=0;i<enemies.size();i++)
							{
								enemyList = enemyList + enemies.get(i) + ", ";
							}
							user.message(ChatColor.getByCode(12) + "Enemies: " + enemyList.substring(0, enemyList.length() - 2) + ".");
						}
						else {
							user.message(ChatColor.getByCode(12) + "Enemies: None");
						}
					}
					
					if (args[1].equalsIgnoreCase("status") && userManager.isLeader(user) == true) {
						groupManager.changeStatus(user, args[2], args[3]);
					}
				}
				
			}
			
			return true;
		}
		
		((Player) sender).sendMessage("You must be registered in NAW to use this functionality");
		return false;
	}
	
	public void sendToLog(String message) {
		log.info("[NationsAtWar]: " + message);
	}
	
	public void messageAll(String message) {
		this.getServer().broadcastMessage("[NationsAtWar]: " + message);
	}
	
	public World getWorld() {
		return this.getServer().getWorld(properties.getProperty("world_name"));
	}
}
