package com.jetug.power_armor_mod.common.capability.TEST;

import com.jetug.power_armor_mod.common.capability.data.IArmorPartData;
import com.jetug.power_armor_mod.common.capability.data.IPlayerData;
import com.jetug.power_armor_mod.common.util.enums.BodyPart;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public interface IDifficulty {

	float[] getDurabilityArray();

	float getDurability(BodyPart part);

	void setDurability(BodyPart part, float value);

	double getDefense();

	void setDefense(double value);

	Entity getEntity();

	void copyFrom(IArmorPartData source);

	CompoundTag serializeNBT();

	void deserializeNBT(CompoundTag data);

	void syncWithClient(ServerPlayer player);

	void syncWithServer();

	void syncFromServer();

	boolean syncWithAll();
}

//	int getSpawnedWithers();
//	void setSpawnedWithers(int spawnedWithers);
//	int getKilledDragons();
//	void setKilledDragons(int killedDragons);
//	byte getFirstDragon();
//	void setFirstDragon(byte firstDragon);
//
//	void addSpawnedWithers(int amount);
//	void addKilledDragons(int amount);
//}
