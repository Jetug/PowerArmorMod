package com.jetug.power_armor_mod.common.network.packet;

import com.jetug.power_armor_mod.common.capability.providers.ArmorDataProvider;
import com.jetug.power_armor_mod.common.capability.data.IArmorPartData;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ArmorPartClientPacket{
    IArmorPartData capability = null;

    private int entityID = -1;
    private CompoundNBT nbt = null;


    public ArmorPartClientPacket(final IArmorPartData capability) {
        this.capability = capability;
    }

    public ArmorPartClientPacket() {}

    public void write(PacketBuffer buffer) {
        buffer.writeInt(capability.getEntity().getId());
        buffer.writeNbt(capability.serializeNBT());
    }

    public ArmorPartClientPacket read(PacketBuffer buffer) {
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
