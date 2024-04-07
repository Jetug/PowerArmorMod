package com.jetug.chassis_core.common.network.packet;

import com.jetug.chassis_core.common.foundation.entity.WearableChassis;
import com.jetug.chassis_core.common.network.data.ArmorData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class C2SChassisPacket {
    public CompoundTag nbt = null;

    public C2SChassisPacket() {
    }

    public C2SChassisPacket(ArmorData armorData) {
        nbt = armorData.serializeNBT();
    }

    public C2SChassisPacket(CompoundTag armorData) {
        nbt = armorData;
    }

    public static void write(C2SChassisPacket message, FriendlyByteBuf buffer) {
        buffer.writeNbt(message.nbt);
    }

    public static C2SChassisPacket read(FriendlyByteBuf buffer) {
        return new C2SChassisPacket(buffer.readNbt());
    }

    public static void handle(C2SChassisPacket message, Supplier<NetworkEvent.Context> context) {
        var player = context.get().getSender();
        var data = message.getArmorData();
        var entity = player.level.getEntity(data.entityId);

        if (entity instanceof WearableChassis powerArmor)
            powerArmor.setArmorData(data);
    }

    public ArmorData getArmorData() {
        var data = new ArmorData();
        data.deserializeNBT(nbt);
        return data;
    }
}