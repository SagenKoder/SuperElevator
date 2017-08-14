package org.pzyko.ironelevator;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SuperElevatorCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		sender.sendMessage(ChatColor.GOLD + "=============== SuperElevator v" + ElevatorPlugin.INSTANCE.getDescription().getVersion() + " ===============");
		sender.sendMessage(ChatColor.GOLD + "Created by l0lkj");
		sender.sendMessage(ChatColor.GOLD + "" +  ChatColor.ITALIC + "https://www.spigotmc.org/resources/superelevator.45389/");
		return false;
	}
}
