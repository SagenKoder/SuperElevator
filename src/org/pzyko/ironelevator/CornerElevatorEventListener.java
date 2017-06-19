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
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.util.Vector;

import net.minecraft.server.v1_12_R1.ChatMessageType;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_12_R1.PacketPlayOutChat;

public class CornerElevatorEventListener implements Listener {

	private static final int MAX_ELEVATION = 24;
	private static final int MIN_ELEVATION = 3;

	public boolean isElevator(Block block) {
		if(block.getType().isTransparent()) return false;

		if(block.getRelative(BlockFace.NORTH).getType().equals(Material.IRON_BLOCK) &&
				(block.getRelative(BlockFace.NORTH, 2).getType().equals(Material.REDSTONE_TORCH_ON))) return true;
		if(block.getRelative(BlockFace.SOUTH).getType().equals(Material.IRON_BLOCK) &&
				(block.getRelative(BlockFace.SOUTH, 2).getType().equals(Material.REDSTONE_TORCH_ON))) return true;
		if(block.getRelative(BlockFace.EAST).getType().equals(Material.IRON_BLOCK) &&
				(block.getRelative(BlockFace.EAST, 2).getType().equals(Material.REDSTONE_TORCH_ON))) return true;
		if(block.getRelative(BlockFace.WEST).getType().equals(Material.IRON_BLOCK) &&
				(block.getRelative(BlockFace.WEST, 2).getType().equals(Material.REDSTONE_TORCH_ON))) return true;

		return false;
	}

	public boolean isSpaceAbove(Block block) {
		return (block.getRelative(BlockFace.UP).getType().isTransparent()) 
				&& block.getRelative(BlockFace.UP, 2).getType().isTransparent();
	}

	@EventHandler
	public void downElevator(PlayerToggleSneakEvent e) {
		Player p = e.getPlayer();
		Block block = p.getLocation().getBlock().getRelative(BlockFace.DOWN);

		if ((p.hasPermission("pzyko.action.ironelevator")) && (!p.isSneaking() && !p.isFlying())) {

			if(!isElevator(block) || !isSpaceAbove(block)) return;

			Block b = block.getRelative(BlockFace.DOWN, MIN_ELEVATION);
			int i = MAX_ELEVATION;
			while ((i > 0) && (!isElevator(b) || !isSpaceAbove(b))) {
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
		Block block = p.getLocation().getBlock().getRelative(BlockFace.DOWN);

		if(!isElevator(block) || !isSpaceAbove(block)) return;

		if ((p.hasPermission("pzyko.action.ironelevator")) && (e.getFrom().getY() < e.getTo().getY())
				&& !p.isFlying()) {

			Block b = block.getRelative(BlockFace.UP, MIN_ELEVATION);

			int i = MAX_ELEVATION;

			while ((i > 0) && (!isElevator(b) || !isSpaceAbove(b))) {
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

}
