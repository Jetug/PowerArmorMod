package com.jetug.power_armor_mod.common.network.packet;

import com.jetug.power_armor_mod.common.capability.data.IArmorPartData;
import com.jetug.power_armor_mod.common.capability.providers.ArmorDataProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ArmorPartClientPacket{
    IArmorPartData capability = null;

    private int entityID = -1;
    private CompoundTag nbt = null;


    public ArmorPartClientPacket(final IArmorPartData capability) {
        this.capability = capability;
    }

    public ArmorPartClientPacket() {}

    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(capability.getEntity().getId());
        buffer.writeNbt(capability.serializeNBT());
    }

    public ArmorPartClientPacket read(FriendlyByteBuf buffer) {
        entityID = buffer.readInt();
        nbt = buffer.readNbt();
        return this;
    }

    public void handle(final Supplier<NetworkEvent.Context> contextSupplier) {
        Entity entity = contextSupplier.get().getSender().level.getEntity(entityID);

        if (entity != null) {
            entity.getCapability(ArmorDataProvider.POWER_ARMOR_PART_DATA).ifPresent(cap ->
                    cap.deserializeNBT(nbt));
        }

        contextSupplier.get().setPacketHandled(true);
    }
}
