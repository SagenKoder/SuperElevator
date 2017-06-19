package org.pzyko.ironelevator;

import org.bukkit.plugin.java.JavaPlugin;

public class IronElevator extends JavaPlugin {

	public void onEnable() {
		//ParticleEffect.ParticlePacket.initialize();
		
		getServer().getPluginManager().registerEvents(new IronelevatorEventListener(), this);
		getServer().getPluginManager().registerEvents(new CornerElevatorEventListener(), this);
	}
	
}
