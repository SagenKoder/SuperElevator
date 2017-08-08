package org.pzyko.ironelevator;

import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.util.Vector;

public class ElevatorListener implements Listener {

	@EventHandler
	public void downElevator(PlayerToggleSneakEvent e) {
		Player p = e.getPlayer();
		Block b = p.getLocation().getBlock().getRelative(BlockFace.DOWN);
		if(!p.hasPermission("pzyko.action.ironelevator")) return;
		if(p.isSneaking()) return;
		if(p.isFlying()) return;

		Optional<ElevatorBlock> currentElevator = ElevatorUtils.getElevator(b);
		if(!currentElevator.isPresent()) return;
		
		Optional<ElevatorBlock> nextElevator = ElevatorUtils.getNextElevator(b, false);
		if(!nextElevator.isPresent()) return;

		Block nextElevatorBlock = nextElevator.get().block;
		Elevator elevator = nextElevator.get().elevator;
		
		Location l = elevator.getTeleportLocation(nextElevatorBlock);
		l.setYaw(p.getLocation().getYaw());
		l.setPitch(p.getLocation().getPitch());
		
		
		p.teleport(l);
		p.setVelocity(new Vector(0, 0, 0));
		p.setFlying(false);
		ActionHelper.sendActionMessage(p, "§aDu tok heisen NED");
		ActionHelper.playoutElevatorEffect(p, currentElevator.get().elevator.getTeleportLocation(b), l);
	}

	@EventHandler
	public void upElevator(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		Block b = p.getLocation().getBlock().getRelative(BlockFace.DOWN);
		if(!p.hasPermission("pzyko.action.ironelevator")) return;
		if(p.isSneaking()) return; // Hindre heisen i å virke ved sneak (snike og hoppe)
		if(p.isFlying()) return;
		if(e.getFrom().getY() >= e.getTo().getY()) return;
		
		// Fix for halfblocks
		if(e.getFrom().getY() - ((int)e.getFrom().getY()) < .5) return;

		Optional<ElevatorBlock> currentElevator = ElevatorUtils.getElevator(b);
		if(!currentElevator.isPresent()) return;
		
		Optional<ElevatorBlock> nextElevator = ElevatorUtils.getNextElevator(b, true);
		if(!nextElevator.isPresent()) return;

		Block nextElevatorBlock = nextElevator.get().block;
		Elevator elevator = nextElevator.get().elevator;
		
		Location l = elevator.getTeleportLocation(nextElevatorBlock);
		l.setYaw(p.getLocation().getYaw());
		l.setPitch(p.getLocation().getPitch());
		
		p.teleport(l);
		p.setVelocity(new Vector(0, 0, 0));
		p.setFlying(false);
		ActionHelper.sendActionMessage(p, "§aDu tok heisen OPP");
		ActionHelper.playoutElevatorEffect(p, currentElevator.get().elevator.getTeleportLocation(b), l);
	}
	
	@EventHandler
	public void elevatorPlace(BlockPlaceEvent e) {
//		Player p = e.getPlayer();
//		Block b = e.getBlockPlaced();
//		
//		Optional<ElevatorBlock> elevatorBlock = Optional.empty();
//		if(!(elevatorBlock = ElevatorUtils.isPartOfElevator(b)).isPresent()) return;
//		
//		// TODO:
	}
}
