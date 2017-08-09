package org.pzyko.ironelevator.NMS.v1_12_R1;

import java.lang.reflect.Field;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import org.pzyko.ironelevator.NMS.NMS;

import net.minecraft.server.v1_12_R1.ChatMessageType;
import net.minecraft.server.v1_12_R1.EnumParticle;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_12_R1.PacketPlayOutChat;
import net.minecraft.server.v1_12_R1.PacketPlayOutWorldParticles;

public class NMSHandler implements NMS {

	public void sendActionMessage(Player p, String msg) {
		IChatBaseComponent barmsg = ChatSerializer.a("{\"text\": \"" + msg + "\"}");
		PacketPlayOutChat packetPlayOutChat = new PacketPlayOutChat(barmsg, ChatMessageType.GAME_INFO);
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(packetPlayOutChat);
	}
	
	public void sendParticleToPlayer(Player player, String particle, Location location, float offsetX, float offsetY, float offsetZ, float speed, int count) throws Exception {
		PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles();
		setValue(packet, "a", EnumParticle.valueOf(particle));
		setValue(packet, "b", (float) location.getX());
		setValue(packet, "c", (float) location.getY());
		setValue(packet, "d", (float) location.getZ());
		setValue(packet, "e", offsetX);
		setValue(packet, "f", offsetY);
		setValue(packet, "g", offsetZ);
		setValue(packet, "h", speed);
		setValue(packet, "i", count);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}
	
	private void setValue(Object instance, String fieldName, Object value) throws Exception {
		Field field = instance.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(instance, value);
	}

}
