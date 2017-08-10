package org.pzyko.ironelevator;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;

public class ActionHelper {

	public static void playoutElevatorEffect(Player p, Location from, Location to) {
		if(isVanished(p)) return;
		
		p.getWorld().playSound(from, Sound.ENTITY_IRONGOLEM_ATTACK, 1.0F, 0.0F);
		p.getWorld().playSound(to, Sound.ENTITY_IRONGOLEM_ATTACK, 1.0F, 0.0F);

		try {
			for(Player o : Bukkit.getOnlinePlayers()) {

				if(o.getWorld() != p.getWorld()) continue;
				if(o.getLocation().distance(p.getLocation()) > 50) continue;

				ParticleEffect.SPELL_INSTANT.sendToPlayer(o, from, 1, 2, 1, 1, 100);
				ParticleEffect.SPELL_INSTANT.sendToPlayer(o, to, 1, 2, 1, 1, 100);

				int particles = 50;
				float radius = 0.3f;

				Location location1 = p.getEyeLocation().clone();
				Location location2 = p.getEyeLocation().clone();
				Location location3 = p.getEyeLocation().clone();

				for (int par = 0; par < particles; par++) {
					double angle, x, z;
					angle = 2 * Math.PI * par / particles;
					x = Math.cos(angle) * radius;
					z = Math.sin(angle) * radius;
					location1.add(x, 0, z);
					location2.add(x, -0.66, z);
					location3.add(x, -1.33, z);
					ParticleEffect.SPELL_WITCH.sendToPlayer(o, location1, 0, 0, 0, 0, 1);
					ParticleEffect.SPELL_WITCH.sendToPlayer(o, location2, 0, 0, 0, 0, 1);
					ParticleEffect.SPELL_WITCH.sendToPlayer(o, location3, 0, 0, 0, 0, 1);
					location1.subtract(x, 0, z);
					location2.subtract(x, -0.66, z);
					location3.subtract(x, -1.33, z);
				}

				from.add(0, 1.62, 0); // Eye position
				location1 = from.clone();
				location2 = from.clone();
				location3 = from.clone();

				for (int par = 0; par < particles; par++) {
					double angle, x, z;
					angle = 2 * Math.PI * par / particles;
					x = Math.cos(angle) * radius;
					z = Math.sin(angle) * radius;
					location1.add(x, 0, z);
					location2.add(x, -0.66, z);
					location3.add(x, -1.33, z);
					ParticleEffect.SPELL_WITCH.sendToPlayer(o, location1, 0, 0, 0, 0, 1);
					ParticleEffect.SPELL_WITCH.sendToPlayer(o, location2, 0, 0, 0, 0, 1);
					ParticleEffect.SPELL_WITCH.sendToPlayer(o, location3, 0, 0, 0, 0, 1);
					location1.subtract(x, 0, z);
					location2.subtract(x, -0.66, z);
					location3.subtract(x, -1.33, z);
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public static void playoutPlaceElevatorEffect(Player p, Block b) {
		if(isVanished(p)) return;

		Location l = b.getRelative(BlockFace.UP).getLocation();
		l.add(.5, 0, .5);

		p.getWorld().playSound(l, Sound.BLOCK_END_PORTAL_FRAME_FILL, 1.0F, 0.0F);

		l.add(0, 0, 0);

		try {
			for(Player o : Bukkit.getOnlinePlayers()) {

				if(o.getWorld() != p.getWorld()) continue;
				if(o.getLocation().distance(p.getLocation()) > 50) continue;

				ParticleEffect.VILLAGER_ANGRY.sendToPlayer(o, l, 0, 0, 0, 0, 10);

				Location location = l.clone();
				int particles = 50;
				float radius = 0.3f;
				for (int par = 0; par < particles; par++) {
					double angle, x, z;
					angle = 2 * Math.PI * par / particles;
					x = Math.cos(angle) * radius;
					z = Math.sin(angle) * radius;
					location.add(x, .2, z);
					ParticleEffect.VILLAGER_HAPPY.sendToPlayer(o, location, 0, 0, 0, 0, 1);
					location.subtract(x, .2, z);
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}


	private static boolean isVanished(Player player) {
		for (MetadataValue meta : player.getMetadata("vanished")) {
			if (meta.asBoolean()) return true;
		}
		return false;
	}

}
