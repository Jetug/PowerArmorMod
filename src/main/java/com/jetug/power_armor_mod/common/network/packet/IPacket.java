package com.jetug.power_armor_mod.common.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public interface IPacket{
    public void write(FriendlyByteBuf buffer);

    public IPacket read(FriendlyByteBuf buffer);

    public void handle(final Supplier<NetworkEvent.Context> contextSupplier);
}
