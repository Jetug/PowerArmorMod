package com.jetug.power_armor_mod.common.network.packet;

import com.jetug.power_armor_mod.common.capability.data.IArmorPartData;
import com.jetug.power_armor_mod.common.capability.providers.ArmorDataProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static software.bernie.geckolib3.GeckoLib.LOGGER;

public class ArmorPartPacket {

	IArmorPartData capability = null;

	private int entityID = -1;
	private CompoundTag nbt = null;


	public ArmorPartPacket(final IArmorPartData capability) {
		this.capability = capability;
	}

	public ArmorPartPacket() {}

	public void write(FriendlyByteBuf buffer) {
		buffer.writeInt(capability.getEntity().getId());
		buffer.writeNbt(capability.serializeNBT());
	}

	public ArmorPartPacket read(FriendlyByteBuf buffer) {
		entityID = buffer.readInt();
		nbt = buffer.readNbt();
		return this;
	}

	public void handle(final Supplier<NetworkEvent.Context> contextSupplier) {
		if (contextSupplier.get().getDirection() != NetworkDirection.PLAY_TO_CLIENT)
			LOGGER.error("Capability message sent to the wrong side!", new Exception());
		else {
			Entity entity = Minecraft.getInstance().player.level.getEntity(entityID);

			if (entity != null) {
				entity.getCapability(ArmorDataProvider.POWER_ARMOR_PART_DATA)
						.ifPresent(cap -> cap.deserializeNBT(nbt));
			}
		}
		contextSupplier.get().setPacketHandled(true);
	}
}
