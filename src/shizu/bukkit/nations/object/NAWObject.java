package shizu.bukkit.nations.object;

import java.io.Serializable;

import org.bukkit.World;

/**
 * Generic Nation at War object super class
 * 
 * @author Shizukesa
 */
@SuppressWarnings("serial") //Read up on this!
public class NAWObject implements Serializable {
	
	protected transient World world;
	
	public NAWObject() {
		
	}
	
	/**
	 * Sets the world that this object belongs to
	 * 
	 * @param w The world to attach to this object
	 */
	public void setWorld(World w) {
		this.world = w;
	}
}
