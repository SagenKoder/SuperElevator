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

public class IronelevatorEventListener implements Listener {

	private static final int MAX_ELEVATION = 24;
	private static final int MIN_ELEVATION = 3;

	public static boolean isElevator(Block block) {
		return !((block.getType() != Material.IRON_BLOCK) 
				|| (!block.getRelative(BlockFace.UP).getType().isTransparent())
				|| (!block.getRelative(BlockFace.UP, 2).getType().isTransparent()));
	}
	
	@EventHandler
	public void downElevator(PlayerToggleSneakEvent e) {
		Player p = e.getPlayer();
		Block b = p.getLocation().getBlock().getRelative(BlockFace.DOWN);
		if ((p.hasPermission("pzyko.action.ironelevator")) && (!p.isSneaking())
				&& (b.getType() == Material.IRON_BLOCK) && !p.isFlying()) {
			b = b.getRelative(BlockFace.DOWN, MIN_ELEVATION);
			int i = MAX_ELEVATION;
			while ((i > 0) && !isElevator(b)) {
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
		Block b = e.getTo().getBlock().getRelative(BlockFace.DOWN);
		if ((p.hasPermission("pzyko.action.ironelevator")) && (e.getFrom().getY() < e.getTo().getY()) && isElevator(b) && !p.isFlying()) {
			b = b.getRelative(BlockFace.UP, MIN_ELEVATION);
			int i = MAX_ELEVATION;
			while ((i > 0) && !isElevator(b)) {
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
		
		boolean isOtherElevator = false;

		if (block.getType() == Material.IRON_BLOCK) {
			Block b = block.getRelative(BlockFace.UP, MIN_ELEVATION);
			int i = MAX_ELEVATION;
			while ((i > 0) && !isOtherElevator && !isElevator(b)) {
				i--;
				b = b.getRelative(BlockFace.UP);
			}
			if (i > 0) {
				isOtherElevator = true;
			}

			b = block.getRelative(BlockFace.DOWN, MIN_ELEVATION);
			i = MAX_ELEVATION;
			while ((i > 0) && !isOtherElevator && !isElevator(b)) {
				i--;
				b = b.getRelative(BlockFace.DOWN);
			}
			if (i > 0) {
				isOtherElevator = true;
			}
		}


		if(isOtherElevator) {
			Location l = block.getLocation().add(0.5f, 0.5f, 0.5f);
			
			ActionHelper.playoutElevatorPlace(p, l);
		}
	}

}
