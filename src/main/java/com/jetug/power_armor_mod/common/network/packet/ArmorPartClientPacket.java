package com.jetug.power_armor_mod.common.network.packet;

import com.jetug.power_armor_mod.common.capability.data.ArmorDataProvider;
import com.jetug.power_armor_mod.common.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ArmorPartClientPacket{
    private final CompoundNBT nbt;

    public ArmorPartClientPacket(CompoundNBT nbt) {
        this.nbt = nbt;
    }

    public static void encode(ArmorPartClientPacket msg, PacketBuffer buf) {
        buf.writeNbt(msg.nbt);
    }

    public static ArmorPartClientPacket decode(PacketBuffer buf) {
        return new ArmorPartClientPacket(buf.readNbt());
    }

    public static class Handler{
        public static void handle(final ArmorPartClientPacket msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {

                ServerPlayerEntity player = ctx.get().getSender();

                player.getCapability(ArmorDataProvider.POWER_ARMOR_PART_DATA).ifPresent(cap -> cap.deserializeNBT(msg.nbt));
                PacketHandler.sendTo(new ArmorPartClientPacket(msg.nbt), player);
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
