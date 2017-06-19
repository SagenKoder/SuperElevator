package org.pzyko.ironelevator;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.util.Vector;

import net.minecraft.server.v1_12_R1.ChatMessageType;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_12_R1.PacketPlayOutChat;

public class IronelevatorEventListener implements Listener {

	private static final int MAX_ELEVATION = 24;
	private static final int MIN_ELEVATION = 3;

	@EventHandler
	public void downElevator(PlayerToggleSneakEvent e) {
		Player p = e.getPlayer();
		Block b = p.getLocation().getBlock().getRelative(BlockFace.DOWN);
		if ((p.hasPermission("pzyko.action.ironelevator")) && (!p.isSneaking())
				&& (b.getType() == Material.IRON_BLOCK) && !p.isFlying()) {
			b = b.getRelative(BlockFace.DOWN, MIN_ELEVATION);
			int i = MAX_ELEVATION;
			while ((i > 0)
					&& ((b.getType() != Material.IRON_BLOCK) 
							|| (!b.getRelative(BlockFace.UP).getType().equals(Material.CARPET) && !b.getRelative(BlockFace.UP).getType().isTransparent())
							|| (!b.getRelative(BlockFace.UP, 2).getType().isTransparent()))) {
				i--;
				b = b.getRelative(BlockFace.DOWN);
			}
			if (i > 0) {
				Location l = p.getLocation();
				l.setY(l.getY() - MAX_ELEVATION - 3.0D + i);
				p.teleport(l);
				p.setVelocity(new Vector(0, 0, 0));
				p.setFlying(false);
				p.getWorld().playSound(l, Sound.ENTITY_IRONGOLEM_ATTACK, 1.0F, 0.0F);
				try {
					for(Player o : Bukkit.getOnlinePlayers()) {

						if(o.getLocation().distanceSquared(p.getLocation()) > 50*50) continue;

						ParticleEffect.INSTANT_SPELL.sendToPlayer(p, l, 1, 2, 1, 1, 100);

						Location location1 = p.getEyeLocation();
						Location location2 = p.getEyeLocation();
						Location location3 = p.getEyeLocation();
						int particles = 50;
						float radius = 0.7f;
						for (int par = 0; par < particles; par++) {
							double angle, x, z;
							angle = 2 * Math.PI * par / particles;
							x = Math.cos(angle) * radius;
							z = Math.sin(angle) * radius;
							location1.add(x, 0, z);
							location2.add(x, -0.66, z);
							location3.add(x, -1.33, z);
							ParticleEffect.WITCH_MAGIC.sendToPlayer(p, location1, 0, 0, 0, 0, 1);
							ParticleEffect.WITCH_MAGIC.sendToPlayer(p, location2, 0, 0, 0, 0, 1);
							ParticleEffect.WITCH_MAGIC.sendToPlayer(p, location3, 0, 0, 0, 0, 1);
							location1.subtract(x, 0, z);
							location2.subtract(x, -0.66, z);
							location3.subtract(x, -1.33, z);
						}
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}

				IChatBaseComponent barmsg = ChatSerializer.a("{\"text\": \"" + "§a*** whoop ***" + "\"}");
				PacketPlayOutChat packetPlayOutChat = new PacketPlayOutChat(barmsg, ChatMessageType.GAME_INFO);
				((CraftPlayer) p).getHandle().playerConnection.sendPacket(packetPlayOutChat);
			}
		}
	}

	@EventHandler
	public void upElevator(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		Block b = e.getTo().getBlock().getRelative(BlockFace.DOWN);
		if ((p.hasPermission("pzyko.action.ironelevator")) && (e.getFrom().getY() < e.getTo().getY())
				&& (b.getType() == Material.IRON_BLOCK) && !p.isFlying()) {
			b = b.getRelative(BlockFace.UP, MIN_ELEVATION);
			int i = MAX_ELEVATION;
			while ((i > 0)
					&& ((b.getType() != Material.IRON_BLOCK) 
							|| (!b.getRelative(BlockFace.UP).getType().equals(Material.CARPET) && !b.getRelative(BlockFace.UP).getType().isTransparent())
							|| (!b.getRelative(BlockFace.UP, 2).getType().isTransparent()))) {
				i--;
				b = b.getRelative(BlockFace.UP);
			}
			if (i > 0) {
				Location l = p.getLocation();
				l.setY(l.getY() + MAX_ELEVATION + 3.0D - i);
				p.teleport(l);
				p.setVelocity(new Vector(0, 0, 0));
				p.setFlying(false);
				p.getWorld().playSound(l, Sound.ENTITY_IRONGOLEM_ATTACK, 1.0F, 0.0F);
				try {
					for(Player o : Bukkit.getOnlinePlayers()) {

						if(o.getLocation().distanceSquared(p.getLocation()) > 50*50) continue;

						ParticleEffect.INSTANT_SPELL.sendToPlayer(p, l, 1, 2, 1, 1, 100);

						Location location1 = p.getEyeLocation();
						Location location2 = p.getEyeLocation();
						Location location3 = p.getEyeLocation();
						int particles = 50;
						float radius = 0.7f;
						for (int par = 0; par < particles; par++) {
							double angle, x, z;
							angle = 2 * Math.PI * par / particles;
							x = Math.cos(angle) * radius;
							z = Math.sin(angle) * radius;
							location1.add(x, 0, z);
							location2.add(x, -0.66, z);
							location3.add(x, -1.33, z);
							ParticleEffect.WITCH_MAGIC.sendToPlayer(p, location1, 0, 0, 0, 0, 1);
							ParticleEffect.WITCH_MAGIC.sendToPlayer(p, location2, 0, 0, 0, 0, 1);
							ParticleEffect.WITCH_MAGIC.sendToPlayer(p, location3, 0, 0, 0, 0, 1);
							location1.subtract(x, 0, z);
							location2.subtract(x, -0.66, z);
							location3.subtract(x, -1.33, z);
						}
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}


				IChatBaseComponent barmsg = ChatSerializer.a("{\"text\": \"" + "§a*** whoop ***" + "\"}");
				PacketPlayOutChat packetPlayOutChat = new PacketPlayOutChat(barmsg, ChatMessageType.GAME_INFO);
				((CraftPlayer) p).getHandle().playerConnection.sendPacket(packetPlayOutChat);
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onCreateElevator(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		
		Block block = e.getBlockPlaced();
		if(!block.getType().equals(Material.CARPET)) return;
		
		block = block.getRelative(BlockFace.DOWN);
		
		boolean yes = false;

		if (block.getType() == Material.IRON_BLOCK) {
			Block b = block.getRelative(BlockFace.UP, MIN_ELEVATION);
			int i = MAX_ELEVATION;
			while ((i > 0)
					&& ((b.getType() != Material.IRON_BLOCK) 
							|| (!b.getRelative(BlockFace.UP).getType().equals(Material.CARPET) && !b.getRelative(BlockFace.UP).getType().isTransparent())
							|| (!b.getRelative(BlockFace.UP, 2).getType().isTransparent()))) {
				i--;
				b = b.getRelative(BlockFace.UP);
			}
			if (i > 0) {
				yes = true;
			}

			b = block.getRelative(BlockFace.DOWN, MIN_ELEVATION);
			i = MAX_ELEVATION;
			while ((i > 0)
					&& ((b.getType() != Material.IRON_BLOCK) 
							|| (!b.getRelative(BlockFace.UP).getType().equals(Material.CARPET) && !b.getRelative(BlockFace.UP).getType().isTransparent())
							|| (!b.getRelative(BlockFace.UP, 2).getType().isTransparent()))) {
				i--;
				b = b.getRelative(BlockFace.DOWN);
			}
			if (i > 0) {
				yes = true;
			}
		}


		if(yes && (block.getRelative(BlockFace.UP).getType().equals(Material.CARPET) || block.getRelative(BlockFace.UP).getType().isTransparent()) 
				&& block.getRelative(BlockFace.UP, 2).getType().isTransparent()) {
			Location l = block.getLocation().add(0.5f, 0.5f, 0.5f);

			p.getWorld().playSound(l, Sound.BLOCK_END_PORTAL_FRAME_FILL, 1.0F, 0.0F);

			try {
				ParticleEffect.ANGRY_VILLAGER.sendToPlayer(p, l, 0, 0, 0, 0, 10);

				l.add(0, 3f, 0);

				Location location1 = l;
				Location location2 = l;
				Location location3 = l;
				int particles = 25;
				float radius = 0.3f;
				for (int par = 0; par < particles; par++) {
					double angle, x, z;
					angle = 2 * Math.PI * par / particles;
					x = Math.cos(angle) * radius;
					z = Math.sin(angle) * radius;
					location1.add(x, 0, z);
					location2.add(x, -0.66, z);
					location3.add(x, -1.33, z);
					ParticleEffect.HAPPY_VILLAGER.sendToPlayer(p, location1, 0, 0, 0, 0, 1);
					ParticleEffect.HAPPY_VILLAGER.sendToPlayer(p, location2, 0, 0, 0, 0, 1);
					ParticleEffect.HAPPY_VILLAGER.sendToPlayer(p, location3, 0, 0, 0, 0, 1);
					location1.subtract(x, 0, z);
					location2.subtract(x, -0.66, z);
					location3.subtract(x, -1.33, z);
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			IChatBaseComponent barmsg = ChatSerializer.a("{\"text\": \"" + "§a*** Heis laget! ***" + "\"}");
			PacketPlayOutChat packetPlayOutChat = new PacketPlayOutChat(barmsg, ChatMessageType.GAME_INFO);
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(packetPlayOutChat);
		}
	}

}
