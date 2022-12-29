package com.jetug.power_armor_mod.common.capability.armordata;

import com.jetug.power_armor_mod.common.network.PacketHandler;
import com.jetug.power_armor_mod.common.network.packet.ArmorClientUpdatePacket;
import com.jetug.power_armor_mod.common.network.packet.ArmorPartClientPacket;
import com.jetug.power_armor_mod.common.network.packet.ArmorPartPacket;
import com.jetug.power_armor_mod.common.util.enums.BodyPart;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public class ArmorData implements IArmorData {

	public static final String DURABILITY = "durability";
	public static final String DEFENSE = "defense";
	private static final int SIZE = 6;

	private final Entity entity;
	private float[] durabilityArray = new float[SIZE];
	private double defense;

	public ArmorData(Entity entity) {
		this.entity = entity;
	}

	@Override
	public float[] getDurabilityArray() {
		return durabilityArray;
	}

	@Override
	public float getDurability(BodyPart part) {
		return durabilityArray[part.getId()];
	}

	@Override
	public void setDurability(BodyPart part, float value) {
		durabilityArray[part.getId()] = value;
	}

	@Override
	public double getDefense() {
		return defense;
	}

	@Override
	public void setDefense(double value) {
		defense = value;
	}

	@Override
	public Entity getEntity() {
		return entity;
	}

	@Override
	public void copyFrom(IArmorData source) {
		float[] buff = source.getDurabilityArray();
		if (buff != null && buff.length == SIZE)
			durabilityArray = buff;
		defense = source.getDefense();
	}

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag nbt = new CompoundTag();

		for(int i = 0; i < durabilityArray.length; i++)
			nbt.putFloat(DURABILITY + i, durabilityArray[i]);
		nbt.putDouble(DEFENSE, defense);
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		for(int i = 0; i < durabilityArray.length; i++)
			durabilityArray[i] = nbt.getFloat(DURABILITY + i);
		defense = nbt.getDouble(DEFENSE);
	}

	@Override
	public void syncWithClient(ServerPlayer player) {
		PacketHandler.sendTo(new ArmorPartPacket(this), player);
	}

	@Override
	public void syncWithServer() {
		PacketHandler.sendToServer(new ArmorPartClientPacket(this));
	}

	@Override
	public void syncFromServer() {
		PacketHandler.sendToServer(new ArmorClientUpdatePacket(this));
	}

	@Override
	public boolean syncWithAll() {
		return false;
	}


//	private int spawnedWithers;
//	private int killedDragons;
//	// 0 = just spawned, 1 = first dragon, 2 = first dragon spawned
//	private byte firstDragon;
//
//	@Override
//	public int getSpawnedWithers() {
//		return this.spawnedWithers;
//	}
//
//	@Override
//	public void setSpawnedWithers(int spawnedWithers) {
//		this.spawnedWithers = Mth.clamp(spawnedWithers, 0, Integer.MAX_VALUE);
//	}
//
//	@Override
//	public int getKilledDragons() {
//		return this.killedDragons;
//	}
//
//	@Override
//	public void setKilledDragons(int killedDragons) {
//		this.killedDragons = Mth.clamp(killedDragons, 0, Integer.MAX_VALUE);
//	}
//
//	@Override
//	public byte getFirstDragon() {
//		return this.firstDragon;
//	}
//
//	@Override
//	public void setFirstDragon(byte firstDragon) {
//		this.firstDragon = firstDragon;
//	}
//
//
//	@Override
//	public void addSpawnedWithers(int amount) {
//		this.setSpawnedWithers(this.getSpawnedWithers() + amount);
//	}
//
//	@Override
//	public void addKilledDragons(int amount) {
//		this.setKilledDragons(this.getKilledDragons() + amount);
//	}
}
