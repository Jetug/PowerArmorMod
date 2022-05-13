package com.jetug.power_armor_mod.common.entity.data;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

import static com.jetug.power_armor_mod.common.entity.data.PowerArmorPartData.*;

public class PowerArmorPartDataStorage  implements Capability.IStorage<IPowerArmorPartData> {
    @Nullable
    @Override
    public INBT writeNBT(Capability<IPowerArmorPartData> capability, IPowerArmorPartData data, Direction side) {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putDouble(DURABILITY, data.getDurability());
        nbt.putDouble(DEFENSE, data.getDefense());
        return nbt;
    }

    @Override
    public void readNBT(Capability<IPowerArmorPartData> capability, IPowerArmorPartData data, Direction side, INBT nbt) {
        data.setDurability(((CompoundNBT)nbt).getDouble(DURABILITY));
        data.setDefense(((CompoundNBT)nbt).getDouble(DEFENSE));
    }
}
