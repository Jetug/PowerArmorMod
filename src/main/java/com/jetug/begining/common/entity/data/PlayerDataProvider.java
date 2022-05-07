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
    //private IPlayerData instance = PLAYER_DATA.getDefaultInstance();
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

//public class PlayerDataProvider implements ICapabilityProvider, INBTSerializable
//{
//    @CapabilityInject(ModPlayerData.class)
//    public static Capability<ModPlayerData> PLAYER_DATA = null;
//
//    private ModPlayerData playerData = null;
//    private final LazyOptional<ModPlayerData> opt = LazyOptional.of(this::createPlayerData);
//
//    @Nonnull
//    private ModPlayerData createPlayerData(){
//        if(playerData == null){
//            playerData = new ModPlayerData();
//        }
//        return playerData;
//    }
//
//
//    @Override
//    public INBT serializeNBT() {
//        return null;
//    }
//
//    @Override
//    public void deserializeNBT(INBT nbt) {
//
//    }
//
//    @Nonnull
//    @Override
//    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
//        if(cap == PLAYER_DATA){
//            return opt.cast();
//        }
//        return LazyOptional;
//    }
//
//    @Nonnull
//    @Override
//    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
//        return ICapabilityProvider.super.getCapability(cap);
//    }
//}
