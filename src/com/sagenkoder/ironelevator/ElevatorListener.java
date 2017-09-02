package com.sagenkoder.ironelevator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Optional;

import com.sagenkoder.ironelevator.language.Messages;

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

		if(!ElevatorPlugin.INSTANCE.gph.playerCanAccessBlock(p, b)) return;

		p.teleport(l);
		p.setVelocity(new Vector(0, 0, 0));
		p.setFlying(false);
		ElevatorPlugin.INSTANCE.nmsHandler.sendActionMessage(p, Messages.getString("elevator_down"));
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
		if((e.getFrom().getY() - ((int)e.getFrom().getY()) < .5) 
				&& !p.getLocation().getBlock().getRelative(BlockFace.UP, 2).getType().isSolid()) return;

		Optional<ElevatorBlock> currentElevator = ElevatorUtils.getElevator(b);
		if(!currentElevator.isPresent()) return;

		Optional<ElevatorBlock> nextElevator = ElevatorUtils.getNextElevator(b, true);
		if(!nextElevator.isPresent()) return;

		Block nextElevatorBlock = nextElevator.get().block;
		Elevator elevator = nextElevator.get().elevator;

		Location l = elevator.getTeleportLocation(nextElevatorBlock);
		l.setYaw(p.getLocation().getYaw());
		l.setPitch(p.getLocation().getPitch());

		if(!ElevatorPlugin.INSTANCE.gph.playerCanAccessBlock(p, b)) return; 

		p.teleport(l);
		p.setVelocity(new Vector(0, 0, 0));
		p.setFlying(false);
		ElevatorPlugin.INSTANCE.nmsHandler.sendActionMessage(p, Messages.getString("elevator_up"));
		ActionHelper.playoutElevatorEffect(p, currentElevator.get().elevator.getTeleportLocation(b), l);
	}

	@EventHandler
	public void elevatorPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		Block b = e.getBlockPlaced();
		
		// Sjekk for launchpads
		if(b.getType().equals(Material.IRON_PLATE) && b.getRelative(BlockFace.DOWN).getType().equals(Material.REDSTONE_BLOCK)
				|| b.getType().equals(Material.REDSTONE_BLOCK) && b.getRelative(BlockFace.UP).getType().equals(Material.IRON_PLATE)) {
			ElevatorPlugin.INSTANCE.nmsHandler.sendActionMessage(p, "§aDu laget en launchpad!");
			ActionHelper.playoutPlaceElevatorEffect(p, b.getType().equals(Material.IRON_PLATE) ? b.getRelative(BlockFace.DOWN) : b);
		}

		Optional<ElevatorBlock> elevatorBlock = Optional.empty();
		if(!(elevatorBlock = ElevatorUtils.isPartOfElevator(b)).isPresent()) return;
		if(!elevatorBlock.get().elevator.isSafeTeleportTo(elevatorBlock.get().elevator.getTeleportLocation(elevatorBlock.get().block))) return;

		ArrayList<Block> storiesFound = new ArrayList<Block>();
		storiesFound.add(elevatorBlock.get().block);

		Block lastElevator = elevatorBlock.get().block;
		while(lastElevator != null) {
			Optional<ElevatorBlock> ele = ElevatorUtils.getNextElevator(lastElevator, true);
			if(ele.isPresent()) {
				storiesFound.add(ele.get().block);
				lastElevator = ele.get().block;
			} else {
				lastElevator = null;
			}
		}

		lastElevator = elevatorBlock.get().block;
		while(lastElevator != null) {
			Optional<ElevatorBlock> ele = ElevatorUtils.getNextElevator(lastElevator, false);
			if(ele.isPresent()) {
				storiesFound.add(ele.get().block);
				lastElevator = ele.get().block;
			} else {
				lastElevator = null;
			}
		}

		if(storiesFound.size() > 1) {
			for(Block elevatorStory : storiesFound)
				ActionHelper.playoutPlaceElevatorEffect(p, elevatorStory);
			ElevatorPlugin.INSTANCE.nmsHandler.sendActionMessage(p, "§aDu laget en heis med " + storiesFound.size() + " etasjer!");
		}
	}
}
