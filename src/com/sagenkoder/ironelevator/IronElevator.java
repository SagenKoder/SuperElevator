package com.sagenkoder.ironelevator;

import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class IronElevator extends Elevator {

	@Override
	public String getElevatorName() {
		return "Jern Heis";
	}
	
	@Override
	public boolean isElevatorBlock(Block b) {
		return b.getType().equals(Material.IRON_BLOCK);
	}

	@Override
	public Optional<Block> isPartOfElevator(Block b) {
		if(isElevatorBlock(b)) return Optional.of(b);
		
		return Optional.empty();
	}

}
