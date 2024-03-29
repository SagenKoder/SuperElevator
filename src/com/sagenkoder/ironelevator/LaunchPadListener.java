package com.sagenkoder.ironelevator;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class LaunchPadListener implements Listener {

	HashMap<UUID, Long> launchedPlayers = new HashMap<UUID, Long>();
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player p = event.getPlayer();
		Location l = p.getLocation();
		
		// remove from list if on ground
		if(event.getFrom().getY() >= event.getTo().getY() 
				&& p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().isSolid() 
				&& launchedPlayers.containsKey(p.getUniqueId())
				&& launchedPlayers.get(p.getUniqueId()) < System.currentTimeMillis() - 500) {
			
			new BukkitRunnable() {
				@Override
				public void run() {
					launchedPlayers.remove(p.getUniqueId());
				}
			}.runTaskLater(ElevatorPlugin.INSTANCE, 1);
			
		}
		
		// launch
		Material plateBlock = Material.IRON_PLATE;
		Material bottomBlock = Material.REDSTONE_BLOCK;

		Material top = l.getWorld().getBlockAt(l).getType();
		Material bottom = l.getWorld().getBlockAt(l).getRelative(0, -1, 0).getType();

		int speed = 5;

		if (plateBlock == top && bottomBlock == bottom) {
			p.setVelocity(p.getLocation().getDirection().multiply(speed));
			p.setVelocity(new Vector(p.getVelocity().getX(), 1.0D, p.getVelocity().getZ()));
			p.playSound(l, Sound.ENTITY_BAT_TAKEOFF, 1.0F, 1.0F);
			l.getWorld().playEffect(l, Effect.ENDER_SIGNAL, 4);

			launchedPlayers.put(p.getUniqueId(), System.currentTimeMillis());
		}
	}

	@EventHandler
	public void onPlayerDamage(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Player)) return;
		Player p = (Player) event.getEntity();
		if (!launchedPlayers.containsKey(p.getUniqueId())) return;
		if(event.getCause().equals(DamageCause.FALL))
		event.setCancelled(true);
	}
	
}
