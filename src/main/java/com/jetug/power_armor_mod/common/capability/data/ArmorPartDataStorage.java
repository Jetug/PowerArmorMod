package com.jetug.power_armor_mod.common.capability.data;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

import static com.jetug.power_armor_mod.common.util.extensions.Array.*;

public class ArmorPartDataStorage implements Capability.IStorage<IArmorPartData> {
    @Nullable
    @Override
    public INBT writeNBT(Capability<IArmorPartData> capability, IArmorPartData data, Direction side) {
        CompoundNBT nbt = new CompoundNBT();
        nbt = data.serializeNBT();
//        nbt.putIntArray(ArmorPartData.DURABILITY, arrayFloatToInt(data.getDurabilityArray()));
//        nbt.putDouble(ArmorPartData.DEFENSE, data.getDefense());
        return nbt;
    }

    @Override
    public void readNBT(Capability<IArmorPartData> capability, IArmorPartData data, Direction side, INBT nbt) {
        data.deserializeNBT((CompoundNBT)nbt);
//        data.setDurabilityArray(arrayIntToFloat(((CompoundNBT)nbt).getIntArray(ArmorPartData.DURABILITY)));
//        data.setDefense(((CompoundNBT)nbt).getDouble(ArmorPartData.DEFENSE));
    }

}
