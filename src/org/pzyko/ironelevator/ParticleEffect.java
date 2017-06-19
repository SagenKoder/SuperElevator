package org.pzyko.ironelevator;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_12_R1.EnumParticle;
import net.minecraft.server.v1_12_R1.PacketPlayOutWorldParticles;

public enum ParticleEffect {
	HUGE_EXPLOSION(EnumParticle.EXPLOSION_HUGE),
	LARGE_EXPLODE(EnumParticle.EXPLOSION_LARGE),
	FIREWORKS_SPARK(EnumParticle.FIREWORKS_SPARK),
	BUBBLE(EnumParticle.WATER_BUBBLE),
	SUSPEND(EnumParticle.SUSPENDED),
	DEPTH_SUSPEND(EnumParticle.SUSPENDED_DEPTH),
	TOWN_AURA(EnumParticle.TOWN_AURA),
	CRIT(EnumParticle.CRIT),
	MAGIC_CRIT(EnumParticle.CRIT_MAGIC),
	MOB_SPELL(EnumParticle.SPELL_MOB),
	MOB_SPELL_AMBIENT(EnumParticle.SPELL_MOB_AMBIENT),
	SPELL(EnumParticle.SPELL),
	INSTANT_SPELL(EnumParticle.SPELL_INSTANT),
	WITCH_MAGIC(EnumParticle.SPELL_WITCH),
	NOTE(EnumParticle.NOTE),
	PORTAL(EnumParticle.PORTAL),
	ENCHANTMENT_TABLE(EnumParticle.ENCHANTMENT_TABLE),
	EXPLODE(EnumParticle.EXPLOSION_NORMAL),
	FLAME(EnumParticle.FLAME),
	LAVA(EnumParticle.LAVA),
	FOOTSTEP(EnumParticle.FOOTSTEP),
	SPLASH(EnumParticle.WATER_SPLASH),
	LARGE_SMOKE(EnumParticle.SMOKE_LARGE),
	CLOUD(EnumParticle.CLOUD),
	RED_DUST(EnumParticle.REDSTONE),
	SNOWBALL_POOF(EnumParticle.SNOWBALL),
	DRIP_WATER(EnumParticle.WATER_DROP),
	DRIP_LAVA(EnumParticle.DRIP_LAVA),
	SNOW_SHOVEL(EnumParticle.SNOW_SHOVEL),
	SLIME(EnumParticle.SLIME),
	HEART(EnumParticle.HEART),
	ANGRY_VILLAGER(EnumParticle.VILLAGER_ANGRY),
	HAPPY_VILLAGER(EnumParticle.VILLAGER_HAPPY),
	ICONCRACK(EnumParticle.ITEM_CRACK),
	TILECRACK(EnumParticle.BLOCK_CRACK);

	private EnumParticle particle;

	ParticleEffect(EnumParticle particle) {
		this.particle = particle;
	}

	public void sendToPlayer(Player player, Location location, float offsetX, float offsetY, float offsetZ, float speed, int count) throws Exception {
		PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles();
		ReflectionUtils.setValue(packet, "a", particle);
		ReflectionUtils.setValue(packet, "b", (float) location.getX());
		ReflectionUtils.setValue(packet, "c", (float) location.getY());
		ReflectionUtils.setValue(packet, "d", (float) location.getZ());
		ReflectionUtils.setValue(packet, "e", offsetX);
		ReflectionUtils.setValue(packet, "f", offsetY);
		ReflectionUtils.setValue(packet, "g", offsetZ);
		ReflectionUtils.setValue(packet, "h", speed);
		ReflectionUtils.setValue(packet, "i", count);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}

}