package com.jetug.power_armor_mod.common.network.packet;

import com.jetug.power_armor_mod.common.util.enums.ActionType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class Packet {
    public Packet(final ActionType action) {
    }

    public Packet() {}

    public void write(FriendlyByteBuf buffer) {}

    public Packet read(FriendlyByteBuf buffer) {
        return this;
    }

    public void handle(final Supplier<NetworkEvent.Context> contextSupplier) {
    }

    public void write(Packet packet, FriendlyByteBuf friendlyByteBuf) {
    }

    public void handle(Packet packet, Supplier<NetworkEvent.Context> contextSupplier) {
    }
}
