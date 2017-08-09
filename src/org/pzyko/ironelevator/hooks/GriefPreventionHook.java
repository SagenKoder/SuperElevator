package org.pzyko.ironelevator.hooks;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import org.pzyko.ironelevator.ElevatorPlugin;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import net.md_5.bungee.api.ChatColor;

public class GriefPreventionHook {

	GriefPrevention gp = null;

	ArrayList<String> msgCoolDowns = new ArrayList<String>();

	public GriefPreventionHook() {

		if(Bukkit.getPluginManager().isPluginEnabled("GriefPrevention")) return;

		gp = (GriefPrevention) Bukkit.getPluginManager().getPlugin("GriefPrevention");

	}

	public boolean isPluginEnabled() {
		if(!Bukkit.getPluginManager().isPluginEnabled("GriefPrevention")) {
			gp = null;
			return false;
		}
		if(gp == null) gp = (GriefPrevention) Bukkit.getPluginManager().getPlugin("GriefPrevention");
		return true;
	}

	public boolean playerCanAccessBlock(Player p, Block b) {
		if(!isPluginEnabled()) return true;

		Claim claim = gp.dataStore.getClaimAt(b.getLocation(), true, null);

		if(claim == null) {
			return true;
		}

		String noAccessReason = claim.allowAccess(p);
		if(noAccessReason != null) {

			if(!msgCoolDowns.contains(p.getUniqueId().toString())) {
				p.sendMessage(ChatColor.RED + noAccessReason);

				msgCoolDowns.add(p.getUniqueId().toString());
				new BukkitRunnable() {
					@Override
					public void run() {
						msgCoolDowns.remove(p.getUniqueId().toString());
					}

				}.runTaskLater(ElevatorPlugin.INSTANCE, 20);
			}
			return false;
		}
		return true;
	}

}
