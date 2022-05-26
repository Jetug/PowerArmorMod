package com.jetug.power_armor_mod.common.capability.data;

import com.jetug.power_armor_mod.common.network.PacketHandler;
import com.jetug.power_armor_mod.common.network.packet.ArmorPartClientPacket;
import com.jetug.power_armor_mod.common.network.packet.ArmorPartPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class ArmorPartData implements IArmorPartData {
    public static final String DURABILITY = "durability";
    public static final String DEFENSE = "defense";

    private final Entity entity;
    private double durability;
    private double defense;

    public ArmorPartData(Entity entity) {
        this.entity = entity;
    }

    @Override
    public double getDurability() {
        return durability;
    }

    @Override
    public void setDurability(double value) {
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
    public void copyFrom(IArmorPartData source) {
        durability = source.getDurability();
        defense = source.getDefense();
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT data = new CompoundNBT();
        data.putDouble("dur", durability);
        data.putDouble("def", defense);
        return data;
    }

    @Override
    public void deserializeNBT(CompoundNBT data) {
        durability = data.getDouble("dur");
        defense = data.getDouble("def");
    }

    @Override
    public void sync(ServerPlayerEntity player) {
        boolean isClientSide = player.level.isClientSide;
        PacketHandler.sendToAllPlayers(new ArmorPartPacket(serializeNBT()), player.server);
//        ArmorPartPacket packet = new ArmorPartPacket(serializeNBT());
//        PacketHandler.sendTo(packet, player);
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