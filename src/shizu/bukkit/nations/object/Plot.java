package shizu.bukkit.nations.object;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;

/**
 * The Nations at War Plot object. Stores vital Plot information
 * for use by the server
 * 
 * @author Shizukesa
 */
@SuppressWarnings("serial") //Read up on this!
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
	 * Returns the location key for this plot (X.Y)
	 * @return the location key of this plot
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Returns the chunk x-coordinate of this plot
	 * @see org.bukkit.Chunk#getX()
	 */
	@Override
	public int getX() {
		return worldX;
	}
	
	/**
	 * Returns the chunk z-coordinate of this plot
	 * @see org.bukkit.Chunk#getZ()
	 */
	@Override
	public int getZ() {
		return worldZ;
	}
	
	/**
	 * Returns the world the plot resides in
	 * @see org.bukkit.Chunk#getWorld()
	 */ 
	@Override
	public World getWorld() {
		return world;
	}
	
	/**
	 * Returns the block from the relative plot coordinates
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
	 * Returns the name of the region the plot is in
	 * @return the region name
	 */
	public String getRegion() {
		return region;
	}
	
	/**
	 * Returns the name of the owner of the plot
	 * @return the owner name (user/nation)
	 */
	public String getOwner() {
		return owner;
	}
	
	/**
	 * Returns the "for sale" status of the plot
	 * @return true if the plot is for sale, false otherwise
	 */
	public Boolean getSaleStatus() {
		return forSale;
	}
	
	/**
	 * Returns the owner + region display name of this plot
	 * @return the plots display name
	 */
	public String getLoctionName() {
		
		String territory = (region == "") ? "territory" : region;
		return owner + "'s " + territory;
	}
	
	/**
	 * Sets the region the plot resides in
	 */
	public void setRegion(String newRegion) {
		region = newRegion;
	}
	
	/**
	 * Sets the owners name
	 */
	public void setOwner(String newOwner) {
		owner = newOwner;
	}
	
	/**
	 * Sets the "for sale" status
	 */
	public void setSaleStatus(Boolean saleStatus) {
		forSale = saleStatus;
	}
}
