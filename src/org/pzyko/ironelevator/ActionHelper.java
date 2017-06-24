package org.pzyko.ironelevator;

import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import net.minecraft.server.v1_12_R1.ChatMessageType;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_12_R1.PacketPlayOutChat;

public class ActionHelper {

	public static void playoutElevatorUse(Player p, Location to, boolean up) {
		p.teleport(to);
		p.setVelocity(new Vector(0, 0, 0));
		p.setFlying(false);
		p.getWorld().playSound(to, Sound.ENTITY_IRONGOLEM_ATTACK, 1.0F, 0.0F);

		try {
			for(Player o : Bukkit.getOnlinePlayers()) {

				if(o.getWorld() != p.getWorld()) continue;
				if(o.getLocation().distance(p.getLocation()) > 50) continue;

				ParticleEffect.INSTANT_SPELL.sendToPlayer(o, to, 1, 2, 1, 1, 100);

				Location location1 = p.getEyeLocation();
				Location location2 = p.getEyeLocation();
				Location location3 = p.getEyeLocation();
				int particles = 50;
				float radius = 0.3f;
				for (int par = 0; par < particles; par++) {
					double angle, x, z;
					angle = 2 * Math.PI * par / particles;
					x = Math.cos(angle) * radius;
					z = Math.sin(angle) * radius;
					location1.add(x, 0, z);
					location2.add(x, -0.66, z);
					location3.add(x, -1.33, z);
					ParticleEffect.WITCH_MAGIC.sendToPlayer(o, location1, 0, 0, 0, 0, 1);
					ParticleEffect.WITCH_MAGIC.sendToPlayer(o, location2, 0, 0, 0, 0, 1);
					ParticleEffect.WITCH_MAGIC.sendToPlayer(o, location3, 0, 0, 0, 0, 1);
					location1.subtract(x, 0, z);
					location2.subtract(x, -0.66, z);
					location3.subtract(x, -1.33, z);
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		IChatBaseComponent barmsg = ChatSerializer.a("{\"text\": \"" + "§a*** Du tok heisen " + (up ? "OPP" : "NED") + " ***" + "\"}");
		PacketPlayOutChat packetPlayOutChat = new PacketPlayOutChat(barmsg, ChatMessageType.GAME_INFO);
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(packetPlayOutChat);
	}

	public static void playoutElevatorPlace(Player p, Location l) {
		p.getWorld().playSound(l, Sound.BLOCK_END_PORTAL_FRAME_FILL, 1.0F, 0.0F);

		try {
			for(Player o : Bukkit.getOnlinePlayers()) {

				if(o.getWorld() != p.getWorld()) continue;
				if(o.getLocation().distance(p.getLocation()) > 50) continue;
				
				ParticleEffect.ANGRY_VILLAGER.sendToPlayer(o, l, 0, 0, 0, 0, 10);

				l.add(0, 3f, 0);

				Location location = l;
				int particles = 50;
				float radius = 0.3f;
				for (int par = 0; par < particles; par++) {
					double angle, x, z;
					angle = 2 * Math.PI * par / particles;
					x = Math.cos(angle) * radius;
					z = Math.sin(angle) * radius;
					location.add(x, -1.6, z);
					ParticleEffect.HAPPY_VILLAGER.sendToPlayer(o, location, 0, 0, 0, 0, 1);
					location.subtract(x, -1.6, z);
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		IChatBaseComponent barmsg = ChatSerializer.a("{\"text\": \"" + "§a*** Heis laget! ***" + "\"}");
		PacketPlayOutChat packetPlayOutChat = new PacketPlayOutChat(barmsg, ChatMessageType.GAME_INFO);
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(packetPlayOutChat);
	}

	public static void collectNearbyElevators(Block anchor, HashSet<Block> collected){
		if((!CornerElevatorEventListener.isElevator(anchor) && !CornerElevatorEventListener.isSpaceAbove(anchor)
				|| !IronelevatorEventListener.isElevator(anchor))) return;

		if(collected.contains(anchor)) return;
		collected.add(anchor);

		collectNearbyElevators(anchor.getRelative(BlockFace.NORTH), collected);
		collectNearbyElevators(anchor.getRelative(BlockFace.SOUTH), collected);
		collectNearbyElevators(anchor.getRelative(BlockFace.EAST), collected);
		collectNearbyElevators(anchor.getRelative(BlockFace.WEST), collected);
	}
}
