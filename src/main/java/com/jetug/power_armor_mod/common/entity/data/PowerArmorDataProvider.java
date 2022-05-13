package com.jetug.power_armor_mod.common.entity.data;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.jetug.power_armor_mod.common.util.constants.Resources.POWER_ARMOR_PART_DATA_LOCATION;

public class PowerArmorDataProvider  implements ICapabilitySerializable<CompoundNBT>
{
    @CapabilityInject(IPowerArmorPartData.class)
    public static final Capability<IPowerArmorPartData> POWER_ARMOR_PART_DATA = null;
    private LazyOptional<IPowerArmorPartData> instance = LazyOptional.of(POWER_ARMOR_PART_DATA::getDefaultInstance);

    public static void register() {
        CapabilityManager.INSTANCE.register(IPowerArmorPartData.class, new PowerArmorPartDataStorage(), PowerArmorPartData::new);
    }

    public static void attach(AttachCapabilitiesEvent<Entity> event) {
        event.addCapability(POWER_ARMOR_PART_DATA_LOCATION, new PowerArmorDataProvider());
    }

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
