package org.pzyko.ironelevator;

import org.bukkit.plugin.java.JavaPlugin;

import org.pzyko.ironelevator.NMS.NMS;
import org.pzyko.ironelevator.hooks.GriefPreventionHook;

public class ElevatorPlugin extends JavaPlugin {

	public static ElevatorPlugin INSTANCE;

	public GriefPreventionHook gph = new GriefPreventionHook();

	NMS nmsHandler = null;
	
	public void onEnable() {
		String packageName = this.getServer().getClass().getPackage().getName();
		String version = packageName.substring(packageName.lastIndexOf('.') + 1);

		try {
			final Class<?> clazz = Class.forName("org.pzyko.ironelevator.NMS." + version + ".NMSHandler");
			// Check if we have a NMSHandler class at that location.
			if (NMS.class.isAssignableFrom(clazz)) { // Make sure it actually implements NMS
				this.nmsHandler = (NMS) clazz.getConstructor().newInstance(); // Set our handler
			}
		} catch (final Exception e) {
			e.printStackTrace();
			this.getLogger().severe("Fant ikke NMS support for denne versjonen av CraftBukkit/Minecraft!");
			this.getLogger().info("Spør Utvikler om oppdatert versjon!");
			this.setEnabled(false);
			return;
		}
		this.getLogger().info("Loader støtte for versjon " + version);

		INSTANCE = this;
		gph.isPluginEnabled();
		getServer().getPluginManager().registerEvents(new ElevatorListener(), this);
	}

	public void onDisable() {
		INSTANCE = null;
	}

}
