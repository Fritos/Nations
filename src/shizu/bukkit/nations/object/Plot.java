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

	private final String key; //Key used as hash map reference
	private final int worldX;
	private final int worldZ;
	private String region;
	private String owner;
	private Boolean forSale;
	
	public Plot(World w, int x, int z) {
		
		world = w;
		worldX = w.getBlockAt(x, 0, z).getChunk().getX();
		worldZ = w.getBlockAt(x, 0, z).getChunk().getZ();
		key = worldX + "." + worldZ;
		region = "";
		owner = "";
		forSale = false;
	}
	
	/**
	 * Returns the location key for this Plot (X.Y)
	 * 
	 * @return the location key of this Plot
	 */
	public String getKey() {
		
		return key;
	}

	/**
	 * Returns the chunk x-coordinate of this Plot
	 * 
	 * @see org.bukkit.Chunk#getX()
	 */
	@Override
	public int getX() {
		
		return worldX;
	}
	
	/**
	 * Returns the chunk z-coordinate of this Plot
	 * 
	 * @see org.bukkit.Chunk#getZ()
	 */
	@Override
	public int getZ() {
		
		return worldZ;
	}
	
	/**
	 * Returns the Block from the relative Plot coordinates
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
	 * Returns the name of this Plot's region
	 * 
	 * @return the region name
	 */
	public String getRegion() {
		
		return region;
	}
	
	/**
	 * Returns the name of this Plot's owner
	 * 
	 * @return the owner name (User/nation)
	 */
	public String getOwner() {
		
		return owner;
	}
	
	/**
	 * Returns the "for sale" status of this Plot
	 * 
	 * @return true if the Plot is for sale, false otherwise
	 */
	public Boolean getSaleStatus() {
		
		return forSale;
	}
	
	/**
	 * Returns the owner + region display name of this Plot
	 * 
	 * @return the plots display name
	 */
	public String getLoctionDescription() {
		
		String territory = (region == "") ? "territory" : region;
		return owner + "'s " + territory;
	}
	
	/**
	 * Sets the name of this Plot's region
	 */
	public void setRegion(String newRegion) {
		
		region = newRegion;
	}
	
	/**
	 * Sets the name of this Plot's owner
	 */
	public void setOwner(String newOwner) {
		
		owner = newOwner;
	}
	
	/**
	 * Sets the "for sale" status of this Plot
	 */
	public void setSaleStatus(Boolean saleStatus) {
		
		forSale = saleStatus;
	}
}
