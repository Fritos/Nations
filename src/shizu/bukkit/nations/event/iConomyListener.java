package shizu.bukkit.nations.event;

import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;
import org.bukkit.plugin.Plugin;

import com.iConomy.iConomy;

import shizu.bukkit.nations.Nations;

public class iConomyListener extends ServerListener {

	private Nations plugin;

	public iConomyListener(Nations instance) {

		plugin = instance;
	}

	@Override
	public void onPluginDisable(PluginDisableEvent event) {
		
		if (plugin.money != null) {
			
			if (event.getPlugin().getDescription().getName().equals("iConomy")) {
				
				plugin.money = null;
				plugin.sendToLog("un-hooked from iConomy.");
			}
		}
	}

	@Override
	public void onPluginEnable(PluginEnableEvent event) {
		
		if (plugin.money == null) {
			
			Plugin iConomy = plugin.getServer().getPluginManager().getPlugin("iConomy");

			if (iConomy != null) {
				
				if (iConomy.isEnabled() && iConomy.getClass().getName().equals("com.iConomy.iConomy")) {
					
					plugin.money = (iConomy)iConomy;
					plugin.sendToLog("hooked into iConomy.");
				}
			}
		}
	}
}

