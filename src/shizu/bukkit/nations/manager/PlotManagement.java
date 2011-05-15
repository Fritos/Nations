package shizu.bukkit.nations.manager;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.World;

import shizu.bukkit.nations.Nations;
import shizu.bukkit.nations.object.NAWObject;
import shizu.bukkit.nations.object.Plot;
import shizu.bukkit.nations.object.User;

/**
 * Manages instances of the Plot class and their interactions
 * between the server and data source.
 * 
 * @author Shizukesa
 */
public class PlotManagement extends Management {
	
	//TODO: check if new claim is an 'outpost'
	
	public PlotManagement(Nations instance) {
		
		super(instance);
		collection = new HashMap<String, NAWObject>();
		type = "plot";
	}
	
	/**
	 * Fetches the Plot for the provided location key, if it exists.
	 * 
	 * @param key The location key of the Plot to get
	 * @return the Plot at the provided location key, 
	 * 		   null if no matching instance exists
	 */
	public Plot getPlot(String key) {
		
		return (exists(key)) ? (Plot) collection.get(key) : null;
	}
	
	/**
	 * Fetches the Plot from a specific location
	 * 
	 * @param loc The location of the Plot
	 * @return the Plot at the given location, null
	 * 		   if no Plot was found
	 */
	public Plot getPlotAtLocation(Location loc) {
	
		String locKey = getLocationKey(loc);
		Plot plot = getPlot(locKey);
		//if (plot == null) { plugin.sendToLog("No plot found at location: " + locKey); } //Usefull for debugging
		return plot;
	}
	
	/**
	 * Fetches the Plot from a User's location
	 * 
	 * @param user The User at the Plot
	 * @return the Plot at the User's location, null
	 * 		   if no Plot was found
	 */
	public Plot getPlotAtUser(User user) {
		
		Plot plot = getPlot(user.getLocationKey());
		if (plot == null) { plugin.sendToLog("No plot found at " + user.getName() + "'s location"); }
		return plot;
	}
	
	/**
	 * Creates a Plot (if none exists), gives ownership to the 
	 * commanding User/Nation, then loads the Plot into 'collection'
	 * and saves it to the data source.
	 * 
	 * @param user The User claiming the Plot
	 * @return true if the Plot was created, false otherwise
	 */
	public Boolean claimPlot(User user) {
		
		String locKey = user.getLocationKey();

		if (exists(locKey)) { 
			
			user.message("This plot has already been claimed!");
			return false; 
		}
		
		if (plugin.userManager.isLeader(user)) {
			
			Location loc = user.getLocation();
			Plot plot = new Plot(plugin.getWorld(), (int) loc.getX(), (int) loc.getZ());
			plot.setOwner(user.getNation());
			collection.put(locKey, plot);
			plugin.userManager.setLocationDescriptionForAll(locKey);
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
	
	/**
	 * Purchases or rents a Plot for the commanding User
	 * 
	 * @param user The user purchasing the Plot
	 * @return true if the Plot was purchased, false otherwise
	 */
	public Boolean buyPlot(User user) {
		
		String locKey = user.getLocationKey();
		Plot plot = getPlotAtUser(user);
		
		if (!exists(locKey)) {
			
			user.message("No plot exists here to buy!");
			return false;
		}
			
		if (plot.getRentStatus()) {
			
			if (plot.getOwner().equals(user.getNation())) {
				
				plot.setRenter(user.getName());
				saveObject(plot.getLocationKey());
				plot.toggleRentStatus();
				showBoundaries(plot);
				plugin.userManager.setLocationDescriptionForAll(locKey);
				user.message("Plot at " + locKey + " is now being rented!");
				//TODO: Alert the plot's owner
				return true;
			} else {
				
				user.message("You can only rent plots offered from your own nation!");
				return false;
			}
		}
		
		if (plot.getSaleStatus()) {
			
			if (plot.getOwner().equals(user.getNation())) {
			
				user.message("Your nation already owns this land!");
				return false;
			}
			
			plot.setOwner(user.getNation());
			plot.setRenter("");
			plot.setRegion("");
			saveObject(plot.getLocationKey());
			plot.toggleSaleStatus();
			showBoundaries(plot);
			plugin.userManager.setLocationDescriptionForAll(locKey);
			user.message("Plot at " + locKey + " has been purchased!");
			//TODO: Alert the plot's previous owner
			return true;
		}
		
		user.message("This plot is not available for purchase!");
		return false;
	}
	
	/**
	 * Destroys the Plot and ownership settings for the commanding 
	 * User/Nation.
	 * 
	 * @param user The User destroying the Plot
	 * @return true if the Plot was destroyed, false otherwise
	 */
	public Boolean razePlot(User user) {
		
		String locKey = user.getLocationKey();
		Plot plot = getPlotAtUser(user);
		
		if (!exists(locKey)) {
			
			user.message("No plot exists here to raze!");
			return false;
		}
		
		if (plugin.userManager.isLeader(user) && plot.getOwner().equals(user.getNation())) {
			
			collection.remove(locKey);
			deleteObject(locKey);
			plugin.userManager.setLocationDescriptionForAll(locKey);
			user.setCurrentLocationDescription("");
			user.message("Plot at " + locKey + " razed!");
			return true;
			
		} else {
			
			user.message("You must be the leader of this nation to raze this plot!");
			return false;
		}
	}
	
	/**
	 * Flags a Plot as being available for resale by the commanding
	 * User/Nation.
	 * 
	 * @param user The User reselling the Plot
	 * @return true if the Plot resale status was toggled, false
	 * 		   otherwise
	 */
	public Boolean resellPlot(User user) {
		
		String locKey = user.getLocationKey();
		Plot plot = getPlotAtUser(user);
		
		if (!exists(locKey)) {
			
			user.message("No plot exists here to resell!");
			return false;
		}
		
		if (plugin.userManager.isLeader(user) && plot.getOwner().equals(user.getNation())) {
			
			plot.toggleSaleStatus();
			showBoundaries(plot);
			if (plot.getRentStatus()) { plot.toggleRentStatus(); }
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
	
	/**
	 * Flags a Plot as being available for rent by the commanding
	 * User/Nation.
	 * 
	 * @param user The User renting out the Plot
	 * @return true if the Plot rent status was toggled, false
	 * 		   otherwise
	 */
	public Boolean rentPlot(User user) {
		
		String locKey = user.getLocationKey();
		Plot plot = getPlotAtUser(user);
		
		if (!exists(locKey)) {
			
			user.message("No plot exists here to rent!");
			return false;
		}
		
		if (plugin.userManager.isLeader(user) && plot.getOwner().equals(user.getNation())) {
			
			plot.toggleRentStatus();
			showBoundaries(plot);
			if (plot.getSaleStatus()) { plot.toggleSaleStatus(); }
			if (plot.getRentStatus()) {
				user.message("Plot at " + locKey + " is now available for rent!");
			} else {
				user.message("Plot at " + locKey + " is no longer available for rent!");
			}
			
			return true;
		} else {
			
			user.message("You must be the leader of this nation to rent this plot!");
			return false;
		}
	}
	
	/**
	 * Renames the regional description of the Plot for the commanding
	 * User/Nation.
	 * 
	 * @param user The user renaming the Plot's region
	 * @param region The new region description
	 * @return true if the Plot's region was renamed, false otherwise
	 */
	public Boolean setRegion(User user, String region) {
		
		String locKey = user.getLocationKey();
		Plot plot = getPlotAtUser(user);
		
		if (!exists(locKey)) {
			
			user.message("No plot exists here to set the region name of!");
			return false;
		}
		
		if (plugin.userManager.isLeader(user) && plot.getOwner().equals(user.getNation())) {
			
			plot.setRegion(region);
			saveObject(locKey);
			plugin.userManager.setLocationDescriptionForAll(locKey);
			user.message("Plot at " + locKey + " is now in region: " + region);
			return true;
		} else {
			
			user.message("You must be the leader of this nation to name this region!");
			return false;
		}
	}
	
	/**
	 * Creates torches at the plot's corners to signify its boundaries. If the Plot
	 * is for sale or rent, redstone torches are created instead.
	 * 
	 * @param plot The Plot who's boundaries will be created/shown
	 */
	public void showBoundaries(Plot plot) {
		
		World world = plot.getWorld();
		int x = plot.getX();
		int z = plot.getZ();
		int id = 50;
		
		if (plot.getSaleStatus() || plot.getRentStatus()) {
			
			id = 76; //redstone torch lit
		}
		
		world.getBlockAt(x * 16, world.getHighestBlockYAt(x * 16, z * 16), z * 16).setTypeId(id);
		world.getBlockAt((x * 16) + 15, world.getHighestBlockYAt((x * 16) + 15, z * 16), z * 16).setTypeId(id);
		world.getBlockAt(x * 16, world.getHighestBlockYAt(x * 16, (z * 16) + 15), (z * 16) + 15).setTypeId(id);
		world.getBlockAt((x * 16) + 15, world.getHighestBlockYAt((x * 16) + 15, (z * 16) + 15), (z * 16) + 15).setTypeId(id);
	}
}
