package com.jetug.power_armor_mod.common.capability.data;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;

public interface IArmorPartData {
    double getDurability();
    void setDurability(double value);
    double getDefense();
    void setDefense(double value);
    void copyFrom(IArmorPartData source);

    public CompoundNBT serializeNBT();

    public void deserializeNBT(CompoundNBT data);

    public void sync(ServerPlayerEntity player);

    public boolean syncWithAll();
}
