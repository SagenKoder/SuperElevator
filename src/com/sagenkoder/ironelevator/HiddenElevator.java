package com.sagenkoder.ironelevator;

import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class HiddenElevator extends Elevator {

	@Override
	public String getElevatorName() {
		return "Skjult Heis";
	}

	@Override
	public boolean isElevatorBlock(Block b) {
		if(!b.getType().isSolid() || !isSafeTeleportTo(b.getRelative(BlockFace.UP).getLocation())) return false;

		if(b.getRelative(BlockFace.NORTH).getType().equals(Material.IRON_BLOCK) &&
				(b.getRelative(BlockFace.NORTH, 2).getType().equals(Material.REDSTONE_TORCH_ON))) return true;
		if(b.getRelative(BlockFace.SOUTH).getType().equals(Material.IRON_BLOCK) &&
				(b.getRelative(BlockFace.SOUTH, 2).getType().equals(Material.REDSTONE_TORCH_ON))) return true;
		if(b.getRelative(BlockFace.EAST).getType().equals(Material.IRON_BLOCK) &&
				(b.getRelative(BlockFace.EAST, 2).getType().equals(Material.REDSTONE_TORCH_ON))) return true;
		if(b.getRelative(BlockFace.WEST).getType().equals(Material.IRON_BLOCK) &&
				(b.getRelative(BlockFace.WEST, 2).getType().equals(Material.REDSTONE_TORCH_ON))) return true;

		return false;
	}

	@Override
	public Optional<Block> isPartOfElevator(Block b) {

		if(b.getType().equals(Material.REDSTONE_TORCH_ON) || b.getType().equals(Material.REDSTONE_TORCH_OFF)) {
			if(b.getRelative(BlockFace.NORTH).getType().equals(Material.IRON_BLOCK) &&
					(b.getRelative(BlockFace.NORTH, 2).getType().isSolid())) return Optional.of(b.getRelative(BlockFace.NORTH, 2));
			if(b.getRelative(BlockFace.SOUTH).getType().equals(Material.IRON_BLOCK) &&
					(b.getRelative(BlockFace.SOUTH, 2).getType().isSolid())) return Optional.of(b.getRelative(BlockFace.SOUTH, 2));
			if(b.getRelative(BlockFace.EAST).getType().equals(Material.IRON_BLOCK) &&
					(b.getRelative(BlockFace.EAST, 2).getType().isSolid())) return Optional.of(b.getRelative(BlockFace.EAST, 2));
			if(b.getRelative(BlockFace.WEST).getType().equals(Material.IRON_BLOCK) &&
					(b.getRelative(BlockFace.WEST, 2).getType().isSolid())) return Optional.of(b.getRelative(BlockFace.WEST, 2));
		}

		else if(b.getType().equals(Material.IRON_BLOCK)) {
			if(b.getRelative(BlockFace.NORTH).getType().isSolid()) {
				Block block = b.getRelative(BlockFace.SOUTH);
				if(block.getType().equals(Material.REDSTONE_TORCH_ON) || 
						block.getType().equals(Material.REDSTONE_TORCH_OFF)) {
					return Optional.of(b.getRelative(BlockFace.NORTH));
				}
			}
			if(b.getRelative(BlockFace.SOUTH).getType().isSolid()) {
				Block block = b.getRelative(BlockFace.NORTH);
				if(block.getType().equals(Material.REDSTONE_TORCH_ON) || 
						block.getType().equals(Material.REDSTONE_TORCH_OFF)) {
					return Optional.of(b.getRelative(BlockFace.SOUTH));
				}
			}
			if(b.getRelative(BlockFace.EAST).getType().isSolid()) {
				Block block = b.getRelative(BlockFace.WEST);
				if(block.getType().equals(Material.REDSTONE_TORCH_ON) || 
						block.getType().equals(Material.REDSTONE_TORCH_OFF)) {
					return Optional.of(b.getRelative(BlockFace.EAST));
				}
			}
			if(b.getRelative(BlockFace.WEST).getType().isSolid()) {
				Block block = b.getRelative(BlockFace.EAST);
				if(block.getType().equals(Material.REDSTONE_TORCH_ON) || 
						block.getType().equals(Material.REDSTONE_TORCH_OFF)) {
					return Optional.of(b.getRelative(BlockFace.WEST));
				}
			}
		}

		else if(b.getType().isSolid()) {
			if(b.getRelative(BlockFace.NORTH).getType().equals(Material.IRON_BLOCK) &&
					(b.getRelative(BlockFace.NORTH, 2).getType().equals(Material.REDSTONE_TORCH_ON) ||
							b.getRelative(BlockFace.WEST, 2).getType().equals(Material.REDSTONE_TORCH_OFF))) return Optional.of(b);
			if(b.getRelative(BlockFace.SOUTH).getType().equals(Material.IRON_BLOCK) &&
					(b.getRelative(BlockFace.SOUTH, 2).getType().equals(Material.REDSTONE_TORCH_ON) ||
							b.getRelative(BlockFace.WEST, 2).getType().equals(Material.REDSTONE_TORCH_OFF))) return Optional.of(b);
			if(b.getRelative(BlockFace.EAST).getType().equals(Material.IRON_BLOCK) &&
					(b.getRelative(BlockFace.EAST, 2).getType().equals(Material.REDSTONE_TORCH_ON) ||
							b.getRelative(BlockFace.WEST, 2).getType().equals(Material.REDSTONE_TORCH_OFF))) return Optional.of(b);
			if(b.getRelative(BlockFace.WEST).getType().equals(Material.IRON_BLOCK) &&
					(b.getRelative(BlockFace.WEST, 2).getType().equals(Material.REDSTONE_TORCH_ON) ||
							b.getRelative(BlockFace.WEST, 2).getType().equals(Material.REDSTONE_TORCH_OFF))) return Optional.of(b);
		}

		return Optional.empty();
	}

}
