package com.jetug.begining.common.entity.data;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PowerArmorDataProvider  implements ICapabilitySerializable<CompoundNBT>
{
    @CapabilityInject(IPowerArmorPartData.class)
    public static final Capability<IPowerArmorPartData> POWER_ARMOR_PART_DATA = null;
    private LazyOptional<IPowerArmorPartData> instance = LazyOptional.of(POWER_ARMOR_PART_DATA::getDefaultInstance);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return POWER_ARMOR_PART_DATA.orEmpty(cap, instance);
    }

    @Override
    public CompoundNBT serializeNBT() {
        return (CompoundNBT) POWER_ARMOR_PART_DATA.getStorage().writeNBT(POWER_ARMOR_PART_DATA, instance.orElseThrow(() -> new IllegalArgumentException("at serialize")), null);
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        POWER_ARMOR_PART_DATA.getStorage().readNBT(POWER_ARMOR_PART_DATA, instance.orElseThrow(() -> new IllegalArgumentException("at deserialize")), null, nbt);
    }
}
