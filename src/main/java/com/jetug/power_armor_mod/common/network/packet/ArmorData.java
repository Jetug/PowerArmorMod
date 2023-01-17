package com.jetug.power_armor_mod.common.network.packet;
import com.jetug.power_armor_mod.common.capability.armordata.IArmorData;
import com.jetug.power_armor_mod.common.network.PacketHandler;
import com.jetug.power_armor_mod.common.util.enums.BodyPart;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;

import static com.jetug.power_armor_mod.common.util.enums.BodyPart.*;
import static com.jetug.power_armor_mod.common.util.enums.BodyPart.RIGHT_LEG;

public class ArmorData {
    private static final int SIZE = 6;
    public static final String DURABILITY = "durability";
    public static final String DEFENSE = "defense";
    public int entityId = -1;

    //private float[] durability = new float[SIZE];
    private double defense;

    public HashMap<BodyPart, Integer> durability = new HashMap<>() {{
        put(HEAD, 0);
        put(BODY, 0);
        put(LEFT_ARM, 0);
        put(RIGHT_ARM, 0);
        put(LEFT_LEG, 0);
        put(RIGHT_LEG, 0);
    }};

    public ArmorData() {}

    public ArmorData(int entityId) {
        this.entityId = entityId;
    }

//    public float[] getDurability() {
//        return durability;
//    }

//    public float getDurability(BodyPart part) {
//        return durability.get(part);
//    }
//
//    public void setDurability(BodyPart part, float value) {
//        durability[part.getId()] = value;
//    }

    public double getDefense() {
        return defense;
    }

    public void setDefense(double value) {
        defense = value;
    }

//    public void copyFrom(IArmorData source) {
//        float[] buff = source.getDurabilityArray();
//        if (buff != null && buff.length == SIZE)
//            durability = buff;
//        defense = source.getDefense();
//    }

    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();

        nbt.putInt("ID", entityId);
        durability.forEach((part, value) ->
                nbt.putInt(DURABILITY + part.getId(), value));
        nbt.putDouble(DEFENSE, defense);
        return nbt;
    }

    public void deserializeNBT(CompoundTag nbt) {
        entityId = nbt.getInt("ID");
        for(int i = 0; i < durability.size(); i++)
            durability.put(BodyPart.getById(i) , nbt.getInt(DURABILITY + i));
        defense = nbt.getDouble(DEFENSE);
    }

    public void sentToClientPlayer(ServerPlayer player) {
        PacketHandler.sendTo(new PowerArmorPacket(this), player);
    }

    public void sentToServer() {
        PacketHandler.sendToServer(new PowerArmorPacket(this));
    }

    public void sentToClient() {
        PacketHandler.sendToAllPlayers(new PowerArmorPacket(this));
    }

    public boolean syncWithAll() {
        return false;
    }
}
