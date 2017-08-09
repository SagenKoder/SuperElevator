package org.pzyko.ironelevator;

import org.bukkit.plugin.java.JavaPlugin;

import org.pzyko.ironelevator.hooks.GriefPreventionHook;

public class ElevatorPlugin extends JavaPlugin {

	public static ElevatorPlugin INSTANCE;
	
	public GriefPreventionHook gph = new GriefPreventionHook();
	
	public void onEnable() {
		INSTANCE = this;
		gph.isPluginEnabled();
		getServer().getPluginManager().registerEvents(new ElevatorListener(), this);
	}
	
	public void onDisable() {
		INSTANCE = null;
	}
	
}
