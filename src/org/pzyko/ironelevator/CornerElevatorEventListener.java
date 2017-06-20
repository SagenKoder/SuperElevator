package org.pzyko.ironelevator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class CornerElevatorEventListener implements Listener {

	private static final int MAX_ELEVATION = 24;
	private static final int MIN_ELEVATION = 3;

	public static boolean isElevator(Block block) {
		if(block.getType().isTransparent() || !isSpaceAbove(block)) return false;

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

	public static boolean isSpaceAbove(Block block) {
		return (block.getRelative(BlockFace.UP).getType().isTransparent()) 
				&& block.getRelative(BlockFace.UP, 2).getType().isTransparent();
	}

	@EventHandler
	public void downElevator(PlayerToggleSneakEvent e) {
		Player p = e.getPlayer();
		Block block = p.getLocation().getBlock().getRelative(BlockFace.DOWN);

		if ((p.hasPermission("pzyko.action.ironelevator")) && (!p.isSneaking() && !p.isFlying())) {

			if(!isElevator(block)) return;

			Block b = block.getRelative(BlockFace.DOWN, MIN_ELEVATION);
			int i = MAX_ELEVATION;
			while ((i > 0) && (!isElevator(b))) {
				i--;
				b = b.getRelative(BlockFace.DOWN);
			}
			if (i > 0) {
				Location l = p.getLocation();
				l.setY(l.getY() - MAX_ELEVATION - 3.0D + i);
				ActionHelper.playoutElevatorUse(p, l, false);
			}
		}

	}

	@EventHandler
	public void upElevator(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		Block block = p.getLocation().getBlock().getRelative(BlockFace.DOWN);

		if(!isElevator(block)) return;

		if ((p.hasPermission("pzyko.action.ironelevator")) && (e.getFrom().getY() < e.getTo().getY())
				&& !p.isFlying()) {

			Block b = block.getRelative(BlockFace.UP, MIN_ELEVATION);

			int i = MAX_ELEVATION;

			while ((i > 0) && (!isElevator(b))) {
				i--;
				b = b.getRelative(BlockFace.UP);
			}
			if (i > 0) {
				Location l = p.getLocation();
				l.setY(l.getY() + MAX_ELEVATION + 3.0D - i);
				ActionHelper.playoutElevatorUse(p, l, true);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onCreateElevator(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		
		Block block = e.getBlockPlaced();
		
		boolean yes = false;

		if (isElevator(block)) {
			Block b = block.getRelative(BlockFace.UP, MIN_ELEVATION);
			int i = MAX_ELEVATION;
			while ((i > 0 && !yes) && (!isElevator(block))) {
				i--;
				b = b.getRelative(BlockFace.UP);
			}
			if (i > 0) {
				yes = true;
			}

			b = block.getRelative(BlockFace.DOWN, MIN_ELEVATION);
			i = MAX_ELEVATION;
			while ((i > 0 && !yes) && (!isElevator(block))) {
				i--;
				b = b.getRelative(BlockFace.DOWN);
			}
			if (i > 0) {
				yes = true;
			}
		}


		if(yes) {
			Location l = block.getLocation().add(0.5f, 0.5f, 0.5f);

			ActionHelper.playoutElevatorPlace(p, l);
		}
	}

}
