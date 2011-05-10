package shizu.bukkit.nations.event;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

import shizu.bukkit.nations.Nations;
import shizu.bukkit.nations.object.Plot;

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
	
	// TODO Permissions: Add single function group/nation permission check, also resale protection, PRIORITY!!!!!!!!!!!!!
	
	@Override
	public void onBlockBreak(BlockBreakEvent event) {
		
		Player player = event.getPlayer();
		Plot plot = plugin.plotManager.getPlotAtLocation(event.getBlock().getLocation());
		
		
		if (plot != null && !plot.getOwner().equals(player.getDisplayName())) {
			event.setCancelled(true);
			plugin.sendToLog("BlockBreak Canceled!");
		}
	}
	
	@Override
	public void onBlockDamage(BlockDamageEvent event) {

		Player player = event.getPlayer();
		Plot plot = plugin.plotManager.getPlotAtLocation(event.getBlock().getLocation());
		
		// TODO Permissions: Add group/nation permission check
		if (plot != null && !plot.getOwner().equals(player.getDisplayName())) {
			event.setCancelled(true);
			plugin.sendToLog("BlockDamage Canceled!");
		}
	}
	
	@Override
	public void onBlockPlace(BlockPlaceEvent event) {
		
		Player player = event.getPlayer();
		Plot plot = plugin.plotManager.getPlotAtLocation(event.getBlock().getLocation());
		
		// TODO Permissions: Add group/nation permission check
		if (plot != null && !plot.getOwner().equals(player.getDisplayName())) {
			event.setCancelled(true);
			plugin.sendToLog("BlockPlace Canceled!");
		}
	}
}
