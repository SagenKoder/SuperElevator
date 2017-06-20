package org.pzyko.ironelevator;

import org.bukkit.plugin.java.JavaPlugin;

public class IronElevator extends JavaPlugin {

	public static IronElevator ironelevator;
	
	public void onEnable() {
		//ParticleEffect.ParticlePacket.initialize();
		
		getServer().getPluginManager().registerEvents(new IronelevatorEventListener(), this);
		getServer().getPluginManager().registerEvents(new CornerElevatorEventListener(), this);
	}
	
	public void onDisable() {
		ironelevator = null;
	}
	
}
