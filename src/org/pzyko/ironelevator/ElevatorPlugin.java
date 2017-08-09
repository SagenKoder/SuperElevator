package org.pzyko.ironelevator;

import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

import org.bukkit.plugin.java.JavaPlugin;

import org.pzyko.ironelevator.NMS.NMS;
import org.pzyko.ironelevator.hooks.GriefPreventionHook;

public class ElevatorPlugin extends JavaPlugin {

	public static ElevatorPlugin INSTANCE;

	public GriefPreventionHook gph = new GriefPreventionHook();

	NMS nmsHandler = null;
	ResourceBundle messages = null;

	public void onEnable() {

		// Load configuration
		if (!new File(getDataFolder(), "config.yml").exists()) {
			saveDefaultConfig();
		}

		// Load languages
		ResourceBundle.clearCache(); // Hopefully fix bugs with reloads
		String[] locale = getConfig().getString("locale").split("_");
		String language = locale[0];
		String country = locale[1];
		Locale currentLocale = new Locale(language, country);
		messages = ResourceBundle.getBundle("org.pzyko.ironelevator.locales.locale", currentLocale);

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
			this.getLogger().severe(messages.getString("error_no_nms_version"));
			this.getLogger().info(messages.getString("ask_dev_for_updates"));
			this.setEnabled(false);
			return;
		}
		this.getLogger().info(messages.getString("loading_supported_version").replace("%a", version));

		INSTANCE = this;
		gph.isPluginEnabled();
		getServer().getPluginManager().registerEvents(new ElevatorListener(), this);
	}

	public void onDisable() {
		INSTANCE = null;
	}

}
