package com.jetug.power_armor_mod.common.capability.providers;

import com.jetug.power_armor_mod.common.capability.data.IPlayerData;
import com.jetug.power_armor_mod.common.capability.data.ModPlayerData;
import com.jetug.power_armor_mod.common.capability.storages.PlayerDataStorage;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.jetug.power_armor_mod.common.util.constants.Resources.PLAYER_DATA_LOCATION;

public class PlayerDataProvider implements ICapabilitySerializable<CompoundTag> {

    public static final Capability<IPlayerData> PLAYER_DATA = CapabilityManager.get(new CapabilityToken<>(){});;
    private LazyOptional<IPlayerData> instance = LazyOptional.of(PLAYER_DATA::getDefaultInstance);

    public static void register() {
        CapabilityManager.INSTANCE.register(IPlayerData.class, new PlayerDataStorage(), ModPlayerData::new);
    }

    public static void attach(AttachCapabilitiesEvent<Entity> event) {
        event.addCapability(PLAYER_DATA_LOCATION, new PlayerDataProvider());
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return PLAYER_DATA.orEmpty(cap, instance);
    }

    @Override
    public CompoundTag serializeNBT() {
        return (CompoundTag) PLAYER_DATA.getStorage().writeNBT(PLAYER_DATA, instance.orElseThrow(() ->
                new IllegalArgumentException("at serialize")), null);
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        PLAYER_DATA.getStorage().readNBT(PLAYER_DATA, instance.orElseThrow(() ->
                new IllegalArgumentException("at deserialize")), null, nbt);
    }
}