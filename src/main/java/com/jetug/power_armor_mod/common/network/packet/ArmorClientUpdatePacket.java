package com.jetug.power_armor_mod.common.network.packet;

import com.jetug.power_armor_mod.common.capability.armordata.IArmorData;
import com.jetug.power_armor_mod.common.network.PacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static com.jetug.power_armor_mod.common.capability.constants.Capabilities.ARMOR_DATA;

public class ArmorClientUpdatePacket{
    IArmorData capability = null;
    private int entityID = -1;

    public ArmorClientUpdatePacket(final IArmorData capability) {
        this.capability = capability;
    }

    public ArmorClientUpdatePacket() {}

    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(capability.getEntity().getId());
    }

    public ArmorClientUpdatePacket read(FriendlyByteBuf buffer) {
        entityID = buffer.readInt();
        return this;
    }

    public void handle(final Supplier<NetworkEvent.Context> contextSupplier) {
        ServerPlayer player = contextSupplier.get().getSender();
        Entity entity = player.level.getEntity(entityID);

        if (entity != null) {
            IArmorData cap = entity.getCapability(ARMOR_DATA).orElse(null);
            PacketHandler.sendTo(new ArmorPartPacket(cap), player);
        }

        contextSupplier.get().setPacketHandled(true);
    }
}