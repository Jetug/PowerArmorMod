package com.jetug.power_armor_mod.common.capability.data;

import com.jetug.power_armor_mod.common.network.PacketHandler;
import com.jetug.power_armor_mod.common.network.packet.ArmorPartClientPacket;
import com.jetug.power_armor_mod.common.network.packet.ArmorPartPacket;
import com.jetug.power_armor_mod.common.util.enums.BodyPart;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;

import static com.jetug.power_armor_mod.common.util.extensions.Array.arrayFloatToInt;
import static com.jetug.power_armor_mod.common.util.extensions.Array.arrayIntToFloat;

public class ArmorPartData implements IArmorPartData {
    public static final String DURABILITY = "durability";
    public static final String DEFENSE = "defense";
    private static final int size = 6;

    private final Entity entity;
    private float durability;
    private double defense;

    public ArmorPartData(Entity entity) {
        this.entity = entity;
    }

//    @Override
//    public float[] getDurabilityArray() {
//        return durability;
//    }
//
//    @Override
//    public void setDurabilityArray(float[] array) {
//        if(array != null && array.length == size)
//            durability = array;
//    }

    @Override
    public float getDurability() {
        return durability;
    }

    @Override
    public void setDurability(float value) {
        durability = value;
    }

    @Override
    public double getDefense() {
        return defense;
    }

    @Override
    public void setDefense(double value) {
        defense = value;
    }

    @Override
    public Entity getEntity() {
        return entity;
    }

//    @Override
//    public void copyFrom(IArmorPartData source) {
//        durability = source.getDurabilityArray();
//        defense = source.getDefense();
//    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
//        nbt.putDouble(DURABILITY, durability);
//        nbt.putDouble(DEFENSE, defense);

        nbt.putFloat(DURABILITY, durability);
        nbt.putDouble(DEFENSE, defense);

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        durability = nbt.getFloat(ArmorPartData.DURABILITY);
        defense = nbt.getDouble(ArmorPartData.DEFENSE);
//
//        durability = data.getDouble(DURABILITY);
//        defense = data.getDouble(DEFENSE);
    }

    @Override
    public void syncWithClient(ServerPlayerEntity player) {
        PacketHandler.sendTo(new ArmorPartPacket(this), player);

//        boolean isClientSide = player.level.isClientSide;
//        PacketHandler.sendToAllPlayers(new ArmorPartPacket(this), player.server);
//        ArmorPartPacket packet = new ArmorPartPacket(this);
//        PacketHandler.sendTo(packet, player);
    }

    @Override
    public void syncWithServer() {
        PacketHandler.sendToServer(new ArmorPartClientPacket(this));
    }

    @Override
    public boolean syncWithAll() {
        return false;
    }
}