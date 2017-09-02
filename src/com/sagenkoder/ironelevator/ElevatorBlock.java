package com.sagenkoder.ironelevator;

import org.bukkit.block.Block;

public class ElevatorBlock {

	public Elevator elevator;
	public Block block;
	
	public ElevatorBlock(Block block, Elevator elevator) {
		this.block = block;
		this.elevator = elevator;
	}
	
}
