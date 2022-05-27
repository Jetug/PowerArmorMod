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
    private float[] durability = new float[size];
    private double defense;

    public ArmorPartData(Entity entity) {
        this.entity = entity;
    }

    @Override
    public float[] getDurabilityArray() {
        return durability;
    }

    @Override
    public void setDurabilityArray(float[] array) {
        if(array != null && array.length == size)
            durability = array;
    }

    @Override
    public float getDurability(BodyPart part) {
        if (durability == null || durability.length != size)
            durability = new float[size];
        return durability[part.getId()];
    }

    @Override
    public void setDurability(BodyPart part, float value) {
        durability[part.getId()] = value;
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
        durability = source.getDurabilityArray();
        defense = source.getDefense();
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
//        nbt.putDouble(DURABILITY, durability);
//        nbt.putDouble(DEFENSE, defense);

        nbt.putIntArray(ArmorPartData.DURABILITY, arrayFloatToInt(getDurabilityArray()));
        nbt.putDouble(ArmorPartData.DEFENSE, getDefense());

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        durability = arrayIntToFloat((nbt).getIntArray(ArmorPartData.DURABILITY));
        defense = (nbt).getDouble(ArmorPartData.DEFENSE);
//
//        durability = data.getDouble(DURABILITY);
//        defense = data.getDouble(DEFENSE);
    }

    @Override
    public void sync(ServerPlayerEntity player) {
//        boolean isClientSide = player.level.isClientSide;
//        PacketHandler.sendToAllPlayers(new ArmorPartPacket(this), player.server);
        ArmorPartPacket packet = new ArmorPartPacket(this);
        PacketHandler.sendTo(packet, player);
    }

    @Override
    public boolean syncWithAll() {
//        World world = Minecraft.getInstance().level;
//        if(world instanceof ServerWorld) {
//            PacketHandler.sendToAllPlayers(new ArmorPartPacket(serializeNBT()), world.getServer());
//            return true;
//        }
       //PacketHandler.sendToAllPlayers(new ArmorPartPacket(serializeNBT()), player.server);

        PacketHandler.sendToServer(new ArmorPartClientPacket(serializeNBT()));

        return false;
    }
}