package shizu.bukkit.nations.event;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

import shizu.bukkit.nations.Nations;
import shizu.bukkit.nations.object.Group;
import shizu.bukkit.nations.object.Plot;
import shizu.bukkit.nations.object.User;

/**
 * Listener class for all block activity relating to 'Nations at War'
 * 
 * @author Shizukesa
 *
 */
public class NationsBlockListener extends BlockListener {
	
	private static Nations plugin;

	public NationsBlockListener(Nations instance) {
		
		plugin = instance;
	}
	
	
	public Boolean canBuild(Plot plot, User user) {
			
		//TODO: add check for ally, enemy, war, etc.
		if (plot != null) {
		
			if (plot.getOwner().equals(user.getNation())) {
				
				if (plot.getRenter().equals("")) {
					
					return true;
				} else {
					
					Group group = plugin.groupManager.getGroup(plot.getOwner());
					
					if (plot.getRenter().equals(user.getName()) || 
						group.hasLeader(user.getName())) {
						
						return true;
					}
				}
				
			}
		} else {
			return true;
		}
		return false;
	}
	
	@Override
	public void onBlockBreak(BlockBreakEvent event) {
		
		User user = plugin.userManager.getUser(event.getPlayer());
		Plot plot = plugin.plotManager.getPlotAtLocation(event.getBlock().getLocation());

		if (!canBuild(plot, user)) {
			event.setCancelled(true);
			plugin.sendToLog("BlockBreak Canceled! " + user.getName() + " in " + plot.getLocationKey());
		}
	}
	
	@Override
	public void onBlockDamage(BlockDamageEvent event) {

		User user = plugin.userManager.getUser(event.getPlayer());
		Plot plot = plugin.plotManager.getPlotAtLocation(event.getBlock().getLocation());
		
		if (!canBuild(plot, user)) {
			event.setCancelled(true);
			plugin.sendToLog("BlockDamage Canceled! " + user.getName() + " in " + plot.getLocationKey());
		}
	}
	
	@Override
	public void onBlockPlace(BlockPlaceEvent event) {
		
		User user = plugin.userManager.getUser(event.getPlayer());
		Plot plot = plugin.plotManager.getPlotAtLocation(event.getBlock().getLocation());
		
		if (!canBuild(plot, user)) {
			event.setCancelled(true);
			plugin.sendToLog("BlockPlace Canceled! " + user.getName() + " in " + plot.getLocationKey());
		}
	}
}
