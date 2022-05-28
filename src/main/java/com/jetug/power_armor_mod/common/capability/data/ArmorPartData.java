package com.jetug.power_armor_mod.common.capability.data;

import com.jetug.power_armor_mod.common.network.PacketHandler;
import com.jetug.power_armor_mod.common.network.packet.ArmorClientUpdatePacket;
import com.jetug.power_armor_mod.common.network.packet.ArmorPartClientPacket;
import com.jetug.power_armor_mod.common.network.packet.ArmorPartPacket;
import com.jetug.power_armor_mod.common.util.enums.BodyPart;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;

public class ArmorPartData implements IArmorPartData {
    public static final String DURABILITY = "durability";
    public static final String DEFENSE = "defense";
    private static final int SIZE = 6;

    private final Entity entity;
    private float[] durabilityArray = new float[SIZE];
    private double defense;

    public ArmorPartData(Entity entity) {
        this.entity = entity;
    }

    @Override
    public float[] getDurabilityArray() {
        return durabilityArray;
    }

    @Override
    public float getDurability(BodyPart part) {
        return durabilityArray[part.getId()];
    }

    @Override
    public void setDurability(BodyPart part, float value) {
        durabilityArray[part.getId()] = value;
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

    @Override
    public void copyFrom(IArmorPartData source) {
        float[] buff = source.getDurabilityArray();
        if (buff != null && buff.length == SIZE)
            durabilityArray = buff;
        defense = source.getDefense();
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();

        for(int i = 0; i < durabilityArray.length; i++)
            nbt.putFloat(DURABILITY + i, durabilityArray[i]);
        nbt.putDouble(DEFENSE, defense);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        for(int i = 0; i < durabilityArray.length; i++)
            durabilityArray[i] = nbt.getFloat(DURABILITY + i);
        defense = nbt.getDouble(ArmorPartData.DEFENSE);
    }

    @Override
    public void syncWithClient(ServerPlayerEntity player) {
        PacketHandler.sendTo(new ArmorPartPacket(this), player);
    }

    @Override
    public void syncWithServer() {
        PacketHandler.sendToServer(new ArmorPartClientPacket(this));
    }

    @Override
    public void syncFromServer() {
        PacketHandler.sendToServer(new ArmorClientUpdatePacket(this));
    }

    @Override
    public boolean syncWithAll() {
        return false;
    }
}