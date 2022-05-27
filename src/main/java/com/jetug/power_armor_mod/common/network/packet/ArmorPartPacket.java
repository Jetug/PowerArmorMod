package com.jetug.power_armor_mod.common.network.packet;

import com.jetug.power_armor_mod.common.capability.data.ArmorDataProvider;
import com.jetug.power_armor_mod.common.capability.data.IArmorPartData;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

import static software.bernie.geckolib3.GeckoLib.LOGGER;

public class ArmorPartPacket {

	IArmorPartData capability = null;

	private int entityID = -1;
	private CompoundNBT nbt = null;
	

	public ArmorPartPacket(final IArmorPartData capability) {
		this.capability = capability;
	}

	public ArmorPartPacket() {

	}

	public void write(PacketBuffer buffer) {
		buffer.writeInt(capability.getEntity().getId());
		buffer.writeNbt(capability.serializeNBT());
	}
	
	public ArmorPartPacket read(PacketBuffer buffer) {
		entityID = buffer.readInt();
		nbt = buffer.readNbt();
		return this;
	}

	public void handle(final Supplier<NetworkEvent.Context> contextSupplier) {
		if (contextSupplier.get().getDirection() != NetworkDirection.PLAY_TO_CLIENT) {
			LOGGER.error("Capability message sent to the wrong side!", new Exception());
		}
		else {
			final Entity entity = Minecraft.getInstance().player.level.getEntity(entityID);
			if (entity != null) {
				entity.getCapability(ArmorDataProvider.POWER_ARMOR_PART_DATA).ifPresent(cap ->
						cap.deserializeNBT(nbt)
				);
			}
		}

		contextSupplier.get().setPacketHandled(true);

		/////////
//		Minecraft mc = Minecraft.getInstance();
//
//		ctx.get().enqueueWork(() ->
//				mc.player.getCapability(ArmorDataProvider.POWER_ARMOR_PART_DATA)
//						.ifPresent(cap -> cap.deserializeNBT(nbt)
//		));
//		ctx.get().setPacketHandled(true);
	}
}
