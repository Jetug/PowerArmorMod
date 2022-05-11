package com.jetug.begining.common.entity.data;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.FloatNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.jetug.begining.common.util.constants.Resources.PLAYER_DATA_LOCATION;

public class PlayerDataProvider implements ICapabilitySerializable<CompoundNBT> {
    @CapabilityInject(IPlayerData.class)
    public static final Capability<IPlayerData> PLAYER_DATA = null;
    private LazyOptional<IPlayerData> instance = LazyOptional.of(PLAYER_DATA::getDefaultInstance);

    public static void register() {
        CapabilityManager.INSTANCE.register(IPlayerData.class, new PlayerDataStorage(), ModPlayerData::new);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return PLAYER_DATA.orEmpty(cap, instance);
    }

    @Override
    public CompoundNBT serializeNBT() {
        return (CompoundNBT) PLAYER_DATA.getStorage().writeNBT(PLAYER_DATA, instance.orElseThrow(() -> new IllegalArgumentException("at serialize")), null);
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        PLAYER_DATA.getStorage().readNBT(PLAYER_DATA, instance.orElseThrow(() -> new IllegalArgumentException("at deserialize")), null, nbt);
    }

    public static void attach(AttachCapabilitiesEvent<Entity> event){
        PlayerDataProvider provider = new PlayerDataProvider();
        event.addCapability(PLAYER_DATA_LOCATION, provider);
        if(provider.instance.isPresent())
            provider.instance.invalidate();
    }
}