package com.jetug.power_armor_mod.common.capability.data;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;

public interface IArmorPartData {

//    float[] getDurabilityArray();
//    void setDurabilityArray(float[] array);
    float getDurability();
    void setDurability( float value);
    double getDefense();
    void setDefense(double value);
    Entity getEntity();

//    void copyFrom(IArmorPartData source);

    public CompoundNBT serializeNBT();

    public void deserializeNBT(CompoundNBT data);

    public void syncWithClient(ServerPlayerEntity player);

    public void syncWithServer();

    public boolean syncWithAll();
}
