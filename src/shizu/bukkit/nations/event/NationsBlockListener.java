package shizu.bukkit.nations.event;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

import shizu.bukkit.nations.Nations;
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
	
	@Override
	public void onBlockBreak(BlockBreakEvent event) {
		
		User user = plugin.userManager.getUser(event.getPlayer());
		Plot plot = plugin.plotManager.getPlotAtLocation(event.getBlock().getLocation());
		
		
		if (plot != null && !plot.getOwner().equals(user.getNation())) {
			event.setCancelled(true);
			plugin.sendToLog("BlockBreak Canceled!");
		}
	}
	
	@Override
	public void onBlockDamage(BlockDamageEvent event) {

		User user = plugin.userManager.getUser(event.getPlayer());
		Plot plot = plugin.plotManager.getPlotAtLocation(event.getBlock().getLocation());
		
		
		if (plot != null && !plot.getOwner().equals(user.getNation())) {
			event.setCancelled(true);
			plugin.sendToLog("BlockDamage Canceled!");
		}
	}
	
	@Override
	public void onBlockPlace(BlockPlaceEvent event) {
		
		User user = plugin.userManager.getUser(event.getPlayer());
		Plot plot = plugin.plotManager.getPlotAtLocation(event.getBlock().getLocation());
		
		
		if (plot != null && !plot.getOwner().equals(user.getNation())) {
			event.setCancelled(true);
			plugin.sendToLog("BlockPlace Canceled!");
		}
	}
}
