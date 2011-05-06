package shizu.bukkit.nations.manager;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.World;

import shizu.bukkit.nations.Nations;
import shizu.bukkit.nations.object.NAWObject;
import shizu.bukkit.nations.object.Plot;
import shizu.bukkit.nations.object.User;


/**
 * Manages Plot objects and their interaction between the server
 * and data sources.
 * 
 * @author Shizukesa
 */
public class PlotManagement extends Management {
	
	//TODO: Add config toggle to allow players not associated with a nation to claim/edit plots
	
	public PlotManagement(Nations instance) {
		
		super(instance);
		collection = new HashMap<String, NAWObject>();
		type = "plot";
	}
	
	// TODO PlotManagement: buy plot, check if new claim is an 'outpost'
	
	public Boolean plotExists(String key) {

		return (collection.containsKey(key)) ? true : false;
	}
	
	public Plot getPlot(String key) {
		
		return (plotExists(key)) ? (Plot) collection.get(key) : null;
	}
	
	/**
	 * Returns the Plot from a specific location
	 * 
	 * @param loc The location containing the plot
	 * @return The Plot at the given location
	 */
	public Plot getPlotAtLocation(Location loc) {
	
		String locKey = getLocationKey(loc);
		Plot plot = getPlot(locKey);
		if (plot == null) { plugin.sendToLog("No plot found at location: " + locKey); }
		return plot;
	}
	
	public Plot getPlotAtUser(User user) {
		
		Plot plot = getPlot(user.getLocationKey());
		if (plot == null) { plugin.sendToLog("No plot found at " + user.getKey() + "'s location"); }
		return plot;
	}
	
	public Boolean claimPlot(User user) {
		
		String locKey = user.getLocationKey();

		if (plotExists(locKey)) { 
			
			user.message("This plot has already been claimed!");
			return false; 
		}
		
		if (plugin.userManager.isLeader(user)) {
			
			Location loc = user.getLocation();
			Plot plot = new Plot(plugin.getWorld(), (int) loc.getX(), (int) loc.getZ());
			plot.setOwner(user.getNation());
			collection.put(locKey, plot);
			saveObject(locKey);
			plugin.groupManager.getGroup(user.getNation()).addPlot(locKey);
			showBoundaries(plot);
			user.message("Plot at " + locKey + " claimed!");
			return true;
			
		} else {
			
			user.message("You must be the leader of a nation to claim a plot!");
			return false;
		}
	}
	
	public Boolean razePlot(User user) {
		
		String locKey = user.getLocationKey();
		Plot plot = getPlotAtUser(user);
		
		if (!plotExists(locKey)) {
			
			user.message("No plot exists here to raze!");
			return false;
		}
		
		if (plugin.userManager.isLeader(user) && plot.getOwner().equals(user.getNation())) {
			
			collection.remove(locKey);
			deleteObject(locKey);
			plugin.groupManager.getGroup(user.getNation()).removePlot(locKey);
			user.setCurrentLocationName("");
			user.message("Plot at " + locKey + " razed!");
			return true;
			
		} else {
			
			user.message("You must be the leader of this nation to raze this plot!");
			return false;
		}
	}
	
	public Boolean resellPlot(User user) {
		
		String locKey = user.getLocationKey();
		Plot plot = getPlotAtUser(user);
		
		if (!plotExists(locKey)) {
			
			user.message("No plot exists here to resell!");
			return false;
		}
		
		if (plugin.userManager.isLeader(user) && plot.getOwner().equals(user.getNation())) {
			
			plot.setSaleStatus(!plot.getSaleStatus());
			showBoundaries(plot);
			
			if (plot.getSaleStatus()) {
				user.message("Plot at " + locKey + " is now for sale!");
			} else {
				user.message("Plot at " + locKey + " is no longer for sale!");
			}
			
			return true;
			
		} else {
			
			user.message("You must be the leader of this nation to resell this plot!");
			return false;
		}
	}
	
	public Boolean setRegion(User user, String region) {
		
		String locKey = user.getLocationKey();
		Plot plot = getPlotAtUser(user);
		
		if (!plotExists(locKey)) {
			
			user.message("No plot exists here to set the region name of!");
			return false;
		}
		
		if (plugin.userManager.isLeader(user) && plot.getOwner() == user.getNation()) {
			
			plot.setRegion(region);
			saveObject(locKey);
			user.setCurrentLocationName(plot.getLoctionName());
			user.message("Plot at " + locKey + " is now in region: " + region);
			return true;
		} else {
			
			user.message("You must be the leader of this nation to name this region!");
			return false;
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
		int id;
		
		if (plot.getSaleStatus() == false) {
			id = 50; //torch
		} else {
			id = 76; //redstone torch
		}
		
		world.getBlockAt(x * 16, world.getHighestBlockYAt(x * 16, z * 16), z * 16).setTypeId(id);
		world.getBlockAt((x * 16) + 15, world.getHighestBlockYAt((x * 16) + 15, z * 16), z * 16).setTypeId(id);
		world.getBlockAt(x * 16, world.getHighestBlockYAt(x * 16, (z * 16) + 15), (z * 16) + 15).setTypeId(id);
		world.getBlockAt((x * 16) + 15, world.getHighestBlockYAt((x * 16) + 15, (z * 16) + 15), (z * 16) + 15).setTypeId(id);
	}
}
