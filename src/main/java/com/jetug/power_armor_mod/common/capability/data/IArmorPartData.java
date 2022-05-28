package com.jetug.power_armor_mod.common.capability.data;

import com.jetug.power_armor_mod.common.util.enums.BodyPart;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;

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

    public CompoundNBT serializeNBT();

    public void deserializeNBT(CompoundNBT data);

    public void syncWithClient(ServerPlayerEntity player);

    public void syncWithServer();

    public void syncFromServer();

    public boolean syncWithAll();
}
