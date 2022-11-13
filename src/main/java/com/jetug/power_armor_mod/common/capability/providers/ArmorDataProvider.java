package com.jetug.power_armor_mod.common.capability.providers;

import com.jetug.power_armor_mod.common.capability.SerializableCapabilityProvider;
import com.jetug.power_armor_mod.common.capability.data.ArmorPartData;
import com.jetug.power_armor_mod.common.capability.storages.ArmorPartDataStorage;
import com.jetug.power_armor_mod.common.capability.data.IArmorPartData;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.jetug.power_armor_mod.common.util.constants.Resources.POWER_ARMOR_PART_DATA_LOCATION;

public class ArmorDataProvider implements ICapabilitySerializable<CompoundNBT> {

    @CapabilityInject(IArmorPartData.class)
    public static final Capability<IArmorPartData> POWER_ARMOR_PART_DATA = null;
    private LazyOptional<IArmorPartData> instance = LazyOptional.of(POWER_ARMOR_PART_DATA::getDefaultInstance);

    public static void register() {
        CapabilityManager.INSTANCE.register(IArmorPartData.class, new ArmorPartDataStorage(), () -> new ArmorPartData(null));
    }

    public static void attach(AttachCapabilitiesEvent<Entity> event) {
        final ArmorPartData data = new ArmorPartData(event.getObject());
        event.addCapability(POWER_ARMOR_PART_DATA_LOCATION, createProvider(data));
    }

    private static ICapabilityProvider createProvider(ArmorPartData data) {
        return new SerializableCapabilityProvider<>(POWER_ARMOR_PART_DATA, null, data);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return POWER_ARMOR_PART_DATA.orEmpty(cap, instance);
    }

    @Override
    public CompoundNBT serializeNBT() {
        return (CompoundNBT) POWER_ARMOR_PART_DATA.getStorage().writeNBT(POWER_ARMOR_PART_DATA, instance.orElseThrow(() ->
                new IllegalArgumentException("at serialize")), null);
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        POWER_ARMOR_PART_DATA.getStorage().readNBT(POWER_ARMOR_PART_DATA, instance.orElseThrow(() ->
                new IllegalArgumentException("at deserialize")), null, nbt);
    }
}