package org.pzyko.ironelevator;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;

import org.pzyko.ironelevator.NMS.NMS;
import org.pzyko.ironelevator.hooks.GriefPreventionHook;
import org.pzyko.ironelevator.language.Messages;

public class ElevatorPlugin extends JavaPlugin {

	public static ElevatorPlugin INSTANCE;

	public GriefPreventionHook gph = new GriefPreventionHook();

	NMS nmsHandler = null;

	public void onEnable() {
		INSTANCE = this;
		
		// Load configuration
		if (!new File(getDataFolder(), "config.yml").exists()) {
			saveDefaultConfig();
		}
		if(!getConfig().isSet("enable_launchpads")) getConfig().set("enable_launchpads", true);
		saveConfig();

		// Load languages
		String locale = getConfig().getString("locale");
		Messages.init(locale);

		// Load NMS classes
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
			this.getLogger().severe(Messages.getString("error_no_nms_version"));
			this.getLogger().severe(Messages.getString("ask_dev_for_updates"));
			this.setEnabled(false);
			return;
		}
		this.getLogger().info(Messages.getString("loading_supported_version").replace("%a", version));

		gph.isPluginEnabled();
		getServer().getPluginManager().registerEvents(new ElevatorListener(), this);
		
		if(getConfig().getBoolean("enable_launchpads")) getServer().getPluginManager().registerEvents(new LaunchPadListener(), this);
	}

	public void onDisable() {
		INSTANCE = null;
	}

}
