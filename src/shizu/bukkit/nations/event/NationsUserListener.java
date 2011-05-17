package shizu.bukkit.nations.event;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import shizu.bukkit.nations.Nations;
import shizu.bukkit.nations.object.User;

public class NationsUserListener extends PlayerListener {
	
	private Nations plugin;
	
	public NationsUserListener(Nations instance) {
		
		plugin = instance;
	}
	
	@Override
	public void onPlayerJoin(PlayerJoinEvent event) {
		
		Player player = event.getPlayer();
		String name = player.getDisplayName();
		
		if (plugin.userManager.exists(name)) {
			
			plugin.userManager.setupUser(player);
			
		} else {
			
			if (Boolean.parseBoolean(plugin.config.get("auto_registration"))) {
				
				plugin.userManager.registerUser(player);
				plugin.userManager.setupUser(player);
			} else {
				player.sendMessage("*** You are not yet registered on the server ***");
				player.sendMessage("*** NAW functionality will be disabled until you are registered! ***");
			}
		}
	}
	
	@Override
	public void onPlayerQuit(PlayerQuitEvent event) {
		
		String name = event.getPlayer().getDisplayName();
		plugin.userManager.saveObject(name);
	}

	@Override
	public void onPlayerKick(PlayerKickEvent event) {

		String name = event.getPlayer().getDisplayName();
		plugin.userManager.saveObject(name);
	}
	
	@Override
	public void onPlayerMove(PlayerMoveEvent event) {
		
		Player player = event.getPlayer();
		
		if (plugin.userManager.exists(player.getDisplayName())) {
		
			User user = plugin.userManager.getUser(player);
			plugin.userManager.updateLocation(user);
		}
	}
}
