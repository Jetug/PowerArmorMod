package com.jetug.power_armor_mod.common.capability.storages;

import com.jetug.power_armor_mod.common.capability.data.IArmorPartData;
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
        return data.serializeNBT();
    }

    @Override
    public void readNBT(Capability<IArmorPartData> capability, IArmorPartData data, Direction side, INBT nbt) {
        data.deserializeNBT((CompoundNBT)nbt);
    }

}
