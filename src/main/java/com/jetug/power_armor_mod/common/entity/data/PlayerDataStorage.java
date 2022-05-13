package com.jetug.power_armor_mod.common.entity.data;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

import static com.jetug.power_armor_mod.common.entity.data.ModPlayerData.IS_IN_POWER_ARMOR;

public class PlayerDataStorage implements Capability.IStorage<IPlayerData> {
    @Nullable
    @Override
    public INBT writeNBT(Capability<IPlayerData> capability, IPlayerData instance, Direction side) {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putBoolean(IS_IN_POWER_ARMOR, instance.getIsInPowerArmor());
        return nbt;
    }

    @Override
    public void readNBT(Capability<IPlayerData> capability, IPlayerData instance, Direction side, INBT nbt) {
        instance.setIsInPowerArmor(((CompoundNBT)nbt).getBoolean(IS_IN_POWER_ARMOR));
    }
}
