package com.jetug.power_armor_mod.common.network.packet;

import com.jetug.power_armor_mod.common.capability.data.ArmorDataProvider;
import com.jetug.power_armor_mod.common.capability.data.IArmorPartData;
import com.jetug.power_armor_mod.common.network.PacketHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ArmorClientUpdatePacket{
    IArmorPartData capability = null;

    private int entityID = -1;

    public ArmorClientUpdatePacket(final IArmorPartData capability) {
        this.capability = capability;
    }

    public ArmorClientUpdatePacket() {}

    public void write(PacketBuffer buffer) {
        buffer.writeInt(capability.getEntity().getId());
    }

    public ArmorClientUpdatePacket read(PacketBuffer buffer) {
        entityID = buffer.readInt();
        return this;
    }

    public void handle(final Supplier<NetworkEvent.Context> contextSupplier) {
        ServerPlayerEntity player = contextSupplier.get().getSender();
        Entity entity = player.level.getEntity(entityID);

        if (entity != null) {
            IArmorPartData cap = entity.getCapability(ArmorDataProvider.POWER_ARMOR_PART_DATA).orElse(null);
            PacketHandler.sendTo(new ArmorPartPacket(cap), player);
        }

        contextSupplier.get().setPacketHandled(true);
    }
}