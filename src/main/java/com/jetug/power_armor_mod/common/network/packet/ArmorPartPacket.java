package com.jetug.power_armor_mod.common.network.packet;

import com.jetug.power_armor_mod.common.capability.data.ArmorDataProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ArmorPartPacket {

	private final CompoundNBT nbt;
	
	public ArmorPartPacket(CompoundNBT nbt) {
		this.nbt = nbt;
	}
	
	public static void encode(ArmorPartPacket msg, PacketBuffer buf) {
		buf.writeNbt(msg.nbt);
	}
	
	public static ArmorPartPacket decode(PacketBuffer buf) {
		return new ArmorPartPacket(buf.readNbt());
	}
	
	public static class Handler{
		public static void handle(final ArmorPartPacket msg, Supplier<NetworkEvent.Context> ctx) {
		
			Minecraft mc = Minecraft.getInstance();
			
			ctx.get().enqueueWork(() ->
					mc.player.getCapability(ArmorDataProvider.POWER_ARMOR_PART_DATA)
							.ifPresent(cap -> cap.deserializeNBT(msg.nbt)
			));
			ctx.get().setPacketHandled(true);
		}
	}
}
