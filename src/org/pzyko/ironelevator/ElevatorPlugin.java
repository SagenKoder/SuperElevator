package org.pzyko.ironelevator;

import org.bukkit.plugin.java.JavaPlugin;

public class ElevatorPlugin extends JavaPlugin {

	public static IronElevator INSTANCE;
	
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new ElevatorListener(), this);
	}
	
	public void onDisable() {
		INSTANCE = null;
	}
	
}
