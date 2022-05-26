package com.jetug.power_armor_mod.common.capability.data;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class ArmorPartDataStorage implements Capability.IStorage<IArmorPartData> {
    @Nullable
    @Override
    public INBT writeNBT(Capability<IArmorPartData> capability, IArmorPartData data, Direction side) {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putDouble(ArmorPartData.DURABILITY, data.getDurability());
        nbt.putDouble(ArmorPartData.DEFENSE, data.getDefense());
        return nbt;
    }

    @Override
    public void readNBT(Capability<IArmorPartData> capability, IArmorPartData data, Direction side, INBT nbt) {
        data.setDurability(((CompoundNBT)nbt).getDouble(ArmorPartData.DURABILITY));
        data.setDefense(((CompoundNBT)nbt).getDouble(ArmorPartData.DEFENSE));
    }
}
