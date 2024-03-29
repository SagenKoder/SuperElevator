package com.sagenkoder.ironelevator;

import java.io.File;

import org.inventivetalent.update.spiget.SpigetUpdate;
import org.inventivetalent.update.spiget.UpdateCallback;
import org.inventivetalent.update.spiget.comparator.VersionComparator;

import com.sagenkoder.ironelevator.NMS.NMS;
import com.sagenkoder.ironelevator.hooks.GriefPreventionHook;
import com.sagenkoder.ironelevator.language.Messages;

import org.bukkit.plugin.java.JavaPlugin;

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
		if(!getConfig().isSet("enable_autoupdater")) getConfig().set("enable_autoupdater", true);
		saveConfig();

		// Load languages
		String locale = getConfig().getString("locale");
		Messages.init("com.sagenkoder.ironelevator.locales", locale);

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

		// Auto-update plugin
		getLogger().info("Checking for updates...");
		final SpigetUpdate updater = new SpigetUpdate(this, 45389);
		updater.setVersionComparator(VersionComparator.SEM_VER);
		updater.checkForUpdate(new UpdateCallback() {
			@Override
			public void updateAvailable(String newVersion, String downloadUrl, boolean hasDirectDownload) {
				if (hasDirectDownload) {
					if(getConfig().getBoolean("enable_autoupdater")) {
						getLogger().warning("New update found! Starting download...");
						if (updater.downloadUpdate()) {
							// Update downloaded, will be loaded when the server restarts
							getLogger().info("Update found! Version " + newVersion + " is downloaded and will be used after next reload/restart!");
						} else {
							// Update failed
							getLogger().warning("Update download failed, reason is " + updater.getFailReason());
						}
					} else {
						getLogger().warning("New update found! Version " + newVersion + " is ready to download!");
						getLogger().warning("Autoupdate is disabled in the config!");
						getLogger().warning("Download new version from " + downloadUrl);
					} 
				} else {
				}
			}
			@Override
			public void upToDate() {
				getLogger().info("Plugin is up to date with version " + getDescription().getVersion());
			}
		});

		gph.isPluginEnabled();
		getServer().getPluginManager().registerEvents(new ElevatorListener(), this);
		if(getConfig().getBoolean("enable_launchpads")) getServer().getPluginManager().registerEvents(new LaunchPadListener(), this);

		getCommand("superelevator").setExecutor(new SuperElevatorCommand());
	}

	public void onDisable() {
		INSTANCE = null;
	}

}
