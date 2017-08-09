package org.pzyko.ironelevator.NMS;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface NMS {

	void sendActionMessage(Player p, String msg);
	
	void sendParticleToPlayer(Player player, String particle, Location location, float offsetX, float offsetY, float offsetZ, float speed, int count) throws Exception;

}
