package com.jetug.begining.common.entity.data;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.FloatNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PlayerDataProvider implements ICapabilitySerializable<CompoundNBT> {

    @CapabilityInject(ModPlayerData.class)
    public static final Capability<IPlayerData> PLAYER_DATA = null;
    private LazyOptional<IPlayerData> instance = LazyOptional.of(PLAYER_DATA::getDefaultInstance);

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
}