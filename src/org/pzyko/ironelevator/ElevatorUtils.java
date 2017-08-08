package org.pzyko.ironelevator;

import java.util.Optional;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class ElevatorUtils {

	private static final int MAX_ELEVATION = 24;
	private static final int MIN_ELEVATION = 3;

	public static Optional<ElevatorBlock> getNextElevator(Block b, boolean up) {
		// UP
		b = b.getRelative(up ? BlockFace.UP : BlockFace.DOWN, MIN_ELEVATION);
		int i = MAX_ELEVATION;
		Optional<ElevatorBlock> elevator = Optional.empty();
		while ((i > 0) && !(elevator = getElevator(b)).isPresent()) {
			i--;
			b = b.getRelative(up ? BlockFace.UP : BlockFace.DOWN);
		}
		if (i > 0) return Optional.of(new ElevatorBlock(b, elevator.get().elevator));
		return Optional.empty();
	}

	public static Optional<ElevatorBlock> getElevator(Block b) {
		for(Elevator elevator : Elevator.ELEVATORS) {
			if(elevator.isElevatorBlock(b) 
					&& elevator.isSafeTeleportTo(b.getRelative(BlockFace.UP).getLocation())) { 
				return Optional.of(new ElevatorBlock(b, elevator));
			}
		}
		return Optional.empty();
	}

	public static Optional<ElevatorBlock> isPartOfElevator(Block b) {
		for(Elevator elevator : Elevator.ELEVATORS) {
			Optional<Block> block;
			if((block = elevator.isPartOfElevator(b)).isPresent()
					&& elevator.isSafeTeleportTo(b.getRelative(BlockFace.UP).getLocation())) { 
				return Optional.of(new ElevatorBlock(block.get(), elevator));
			}
		}
		return Optional.empty();
	}

}
