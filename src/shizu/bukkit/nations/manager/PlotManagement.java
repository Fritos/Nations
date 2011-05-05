package shizu.bukkit.nations.manager;

import java.util.HashMap;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import shizu.bukkit.nations.Nations;
import shizu.bukkit.nations.object.NAWObject;
import shizu.bukkit.nations.object.Plot;

/**
 * Manages Plot objects and their interaction between the server
 * and data sources.
 * 
 * @author Shizukesa
 */
public class PlotManagement extends Management {
	
	public PlotManagement(Nations instance) {
		
		super(instance);
		collection = new HashMap<String, NAWObject>();
		type = "plot";
	}
	
	// TODO PlotManagement: buy plot, check if new claim is an outpost
	
	/**
	 * Returns the Plot identification key for the given location
	 * 
	 * @param loc The Location to create a key for
	 * @return the newly created identification key
	 */
	public String getLocationKey(Location loc) {
		
		 Chunk chunk = loc.getBlock().getChunk();
		 int x = chunk.getX();
		 int z = chunk.getZ();
		 return x + "." + z;
	}
	
	/**
	 * Returns the Plot from a specific location
	 * 
	 * @param loc The location containing the plot
	 * @return The Plot at the given location
	 */
	public Plot getPlotAtLocation(Location loc) {
		
		Plot plot = null;
		String key = getLocationKey(loc);
		plot = (Plot)collection.get(key);
		if (plot == null) { plugin.sendToLog("No plot found at location: " + key); }
		
		return plot;
	}
	
	/**
	 * Returns the Plot at the given players location
	 * 
	 * @param player The player to get Plot location information from
	 * @return the Plot the player is currently in
	 */
	public Plot getPlotAtPlayer(Player player) {
		
		String key = getLocationKey(player.getLocation());
		return (Plot)collection.get(key);
	}
	
	/**
	 * Claims a new plot of land for the player
	 * 
	 * @param player The player who is claiming the Plot
	 */
	public void claimPlot(Player player) {
		
		Location loc = player.getLocation();
		String key = getLocationKey(loc);
		
		if (collection.containsKey(key)) {
			player.sendMessage("This plot has already been claimed!");
		} else {
			Plot plot = new Plot(player.getWorld(), (int)loc.getX(), (int)loc.getZ());
			plot.setOwner(player.getDisplayName());
			// TODO: Check player registration, nation membership and set owner appropriatly
			collection.put(key, plot);
			this.saveObject(key);
			player.sendMessage("Plot at " + key + " claimed!");
			showBoundaries(plot);
		}
	}
	
	/**
	 * Removes the plot of land from the players possession
	 * 
	 * @param player
	 */
	public void razePlot(Player player) {
		
		Location loc = player.getLocation();
		String key = getLocationKey(loc);
		
		if (collection.containsKey(key)) {
			Plot plot = (Plot)collection.get(key);
			
			// TODO PlotManagement: Check for group / nation ownership also
			if (plot.getOwner().equals(player.getDisplayName())) {
				this.deleteObject(key);
				player.sendMessage("Plot at " + key + " razed!");
			} else {
				player.sendMessage("You do not have permission to raze this plot!");
			}
		} else {
			player.sendMessage("No plot to raze!");
		}
	}
	
	/**
	 * Sets the plot up for resale
	 * 
	 * @param player The player who is reselling the plot
	 */
	// TODO Resale: add resale value (after iconomy is added)
	public void resellPlot(Player player) {
		
		Location loc = player.getLocation();
		String key = getLocationKey(loc);
		
		if (collection.containsKey(key)) {
			Plot plot = (Plot)collection.get(key);

			if (plot.getOwner().equals(player.getDisplayName())) {
				plot.setSaleStatus(!plot.getSaleStatus());
				showBoundaries(plot);
				
				if (plot.getSaleStatus()) {
					player.sendMessage("Plot at " + key + " is now for sale!");
				} else {
					player.sendMessage("Plot at " + key + " is no longer for sale!");
				}
			} else {
				player.sendMessage("You do not have permission to resell this plot!");
			}
		} else {
			player.sendMessage("No plot to resell!");
		}
	}
	
	public void setRegion(Player player, String region) {
		
		Location loc = player.getLocation();
		String key = getLocationKey(loc);
		
		if (collection.containsKey(key)) {
			Plot plot = (Plot)collection.get(key);
			
			if (plot.getOwner().equals(player.getDisplayName())) {
				plot.setRegion(region);
				this.saveObject(key);
				player.sendMessage("Plot at " + key + " is now in region: " + region);
			} else {
				player.sendMessage("You do not have permission to name the region of this plot!");
			}
		} else {
			player.sendMessage("No plot to set region!");
		}
	}
	
	/**
	 * Creates torches at the plot's corners to signify its boundaries. If the Plot
	 * is for sale, redstone torches are created at the corners.
	 * 
	 * @param plot The Plot who's boundaries will be created/shown
	 */
	public void showBoundaries(Plot plot) {
		
		World world = plot.getWorld();
		int x = plot.getX();
		int z = plot.getZ();
		int type;
		
		if (plot.getSaleStatus() == false) {
			type = 50; //torch
		} else {
			type = 76; //redstone torch
		}
		
		world.getBlockAt(x * 16, world.getHighestBlockYAt(x * 16, z * 16), z * 16).setTypeId(type);
		world.getBlockAt((x * 16) + 15, world.getHighestBlockYAt((x * 16) + 15, z * 16), z * 16).setTypeId(type);
		world.getBlockAt(x * 16, world.getHighestBlockYAt(x * 16, (z * 16) + 15), (z * 16) + 15).setTypeId(type);
		world.getBlockAt((x * 16) + 15, world.getHighestBlockYAt((x * 16) + 15, (z * 16) + 15), (z * 16) + 15).setTypeId(type);
	}
}
