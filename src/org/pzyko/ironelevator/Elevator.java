package org.pzyko.ironelevator;

import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public abstract class Elevator {

	public static Elevator[] ELEVATORS = new Elevator[]{new HiddenElevator(), new org.pzyko.ironelevator.IronElevator()};
	
	public abstract String getElevatorName();
	
	public abstract boolean isElevatorBlock(Block b);

	public abstract Optional<Block> isPartOfElevator(Block b);

	public boolean isSafeTeleportTo(Location to) {
		Block b = to.getBlock();

		boolean safe = true;
		
		// fix for sign is solid bug
		if(b.getRelative(BlockFace.DOWN).getType().equals(Material.WALL_SIGN)) safe = false;
		
		for(int i = 0; i <= 1; i++) {
			Material m = b.getType();

			if(m.isTransparent() 
					|| m.equals(Material.CARPET) 
					|| m.equals(Material.SIGN_POST) 
					|| m.equals(Material.WALL_SIGN) 
					|| m.equals(Material.SIGN)) {
			} else { safe = false; }
			
			b = b.getRelative(BlockFace.UP);
		}
		
		return safe;
	}
	
	public Location getTeleportLocation(Block b) {
		return b.getRelative(BlockFace.UP).getLocation().add(0.5, 0, 0.5);
	}

}
