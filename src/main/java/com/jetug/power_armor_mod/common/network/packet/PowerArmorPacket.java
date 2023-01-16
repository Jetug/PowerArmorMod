package com.jetug.power_armor_mod.common.network.packet;

import com.jetug.power_armor_mod.common.util.enums.ActionType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PowerArmorPacket extends Packet{
    ActionType action = null;
    private int actionId = -1;
    private int entityId = -1;

    public PowerArmorPacket(int entityId, final ActionType action) {
        this.entityId = entityId;
        this.action = action;
    }

    public PowerArmorPacket() {}

    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(action.getId());
    }

    public PowerArmorPacket read(FriendlyByteBuf buffer) {
        actionId = buffer.readInt();
        return this;
    }

    public void handle(final Supplier<NetworkEvent.Context> contextSupplier) {
        ServerPlayer player = contextSupplier.get().getSender();
        if (player != null && actionId == ActionType.DISMOUNT.getId()) {
            player.stopRiding();
        }
    }
}