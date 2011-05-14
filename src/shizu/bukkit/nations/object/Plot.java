package shizu.bukkit.nations.object;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;

/**
 * The Nations at War Plot object.
 * 
 * @author Shizukesa
 */
@SuppressWarnings("serial")
public class Plot extends NAWObject implements Chunk {

	private final String locationKey;
	private final int worldX;
	private final int worldZ;
	private String region;
	private String owner;
	private String renter;
	private Boolean forSale;
	private Boolean forRent;
	
	public Plot(World w, int x, int z) {
		
		world = w;
		worldX = w.getBlockAt(x, 0, z).getChunk().getX();
		worldZ = w.getBlockAt(x, 0, z).getChunk().getZ();
		locationKey = worldX + "." + worldZ;
		region = "";
		owner = "";
		renter = "";
		forSale = false;
		forRent = false;
	}
	
	/**
	 * Returns the location key for this Plot (X.Y).
	 * 
	 * @return the location key of this Plot
	 */
	public String getLocationKey() {
		
		return locationKey;
	}

	/**
	 * Returns the chunk x-coordinate of this Plot.
	 * 
	 * @see org.bukkit.Chunk#getX()
	 */
	@Override
	public int getX() {
		
		return worldX;
	}
	
	/**
	 * Returns the chunk z-coordinate of this Plot.
	 * 
	 * @see org.bukkit.Chunk#getZ()
	 */
	@Override
	public int getZ() {
		
		return worldZ;
	}
	
	/**
	 * Returns the Block from the relative Plot coordinates.
	 * 
	 * @see org.bukkit.Chunk#getBlock(int, int, int)
	 */
	@Override
    public Block getBlock(int x, int y, int z) {
		
    	return world.getBlockAt(worldX + x, y, worldZ + z);
    }
    
	@Override
    public Entity[] getEntities() {
		
    	Entity[] ent = new Entity[0];
    	return ent; //PLACEHOLDER
    }
    
	@Override
    public BlockState[] getTileEntities() {
		
    	BlockState[] bs = new BlockState[0];
    	return bs; //PLACEHOLDER
    }
	
	/**
	 * Returns the name of this Plot's region.
	 * 
	 * @return the region name
	 */
	public String getRegion() {
		
		return region;
	}
	
	/**
	 * Returns the name of this Plot's owner.
	 * 
	 * @return the owner name (User/nation)
	 */
	public String getOwner() {
		
		return owner;
	}
	
	/**
	 * Returns the name of the Plot's renter.
	 * 
	 * @return the renter name
	 */
	public String getRenter() {
		
		return renter;
	}
	
	/**
	 * Returns the "for sale" status of this Plot.
	 * 
	 * @return true if the Plot is for sale, false otherwise
	 */
	public Boolean getSaleStatus() {
		
		return forSale;
	}
	
	/**
	 * Returns the "for rent" status of this Plot.
	 * 
	 * @return true if the Plot is for rent, false otherwise
	 */
	public Boolean getRentStatus() {
		
		return forRent;
	}
	
	/**
	 * Returns the owner + region display name of this Plot.
	 * 
	 * @return the plots display name
	 */
	public String getLoctionDescription() {
		
		String prefix = (renter.equals("")) ? owner : owner + ", " + renter;
		String suffix = (region.equals("")) ? "territory" : region;
		return prefix + "'s " + suffix;
	}
	
	/**
	 * Sets the name of this Plot's region.
	 * 
	 * @param newRegion The name of the new region
	 */
	public void setRegion(String newRegion) {
		
		region = newRegion;
	}
	
	/**
	 * Sets the name of this Plot's owner.
	 * 
	 * @param newOwner The name of the new owner
	 */
	public void setOwner(String newOwner) {
		
		owner = newOwner;
	}
	
	/**
	 * Sets the name of this Plot's renter.
	 * 
	 * @param newRenter The name of the new renter
	 */
	public void setRenter(String newRenter) {
		
		renter = newRenter;
	}
	
	/**
	 * Sets the "for sale" status of this Plot.
	 */
	public void toggleSaleStatus() {
		
		forSale = !forSale;
	}
	
	/**
	 * Sets the "for rent" status of this Plot.
	 */
	public void toggleRentStatus() {
		
		forRent = !forRent;
	}
}
