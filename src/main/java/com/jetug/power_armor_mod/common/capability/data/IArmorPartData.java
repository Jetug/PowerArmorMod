package com.jetug.power_armor_mod.common.capability.data;

import com.jetug.power_armor_mod.common.util.enums.BodyPart;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public interface IArmorPartData {

    float[] getDurabilityArray();

//    float[] getDurabilityArray();
//    void setDurabilityArray(float[] array);
    float getDurability(BodyPart part);
    void setDurability(BodyPart part, float value);
    double getDefense();
    void setDefense(double value);
    Entity getEntity();

//    void copyFrom(IArmorPartData source);

    void copyFrom(IArmorPartData source);

    public CompoundTag serializeNBT();

    public void deserializeNBT(CompoundTag data);

    public void syncWithClient(ServerPlayer player);

    public void syncWithServer();

    public void syncFromServer();

    public boolean syncWithAll();
}
