package com.jetug.power_armor_mod.common.network.packet;

import com.jetug.power_armor_mod.common.network.data.ArmorData;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class InventoryPacket {
    public CompoundTag nbt = null;

    public InventoryPacket() {}

    public InventoryPacket(ArmorData armorData) {
        nbt = armorData.serializeNBT();
    }

    public InventoryPacket(CompoundTag armorData) {
        nbt = armorData;
    }

    public static void write(InventoryPacket message, FriendlyByteBuf buffer) {
        buffer.writeNbt(message.nbt);
    }

    public static InventoryPacket read(FriendlyByteBuf buffer) {
        return new InventoryPacket(buffer.readNbt());
    }

    public static void handle(InventoryPacket message, Supplier<NetworkEvent.Context> context) {
        Player player = null;

        if(context.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT){
            player = Minecraft.getInstance().player;
        }
        else if(context.get().getDirection() == NetworkDirection.PLAY_TO_SERVER) {
            //var server = ServerLifecycleHooks.getCurrentServer();
            player = context.get().getSender();
        }
//
//        var data = message.nbt;
//        var entity = player.level.getEntity(data.entityId);
//
//        if(entity instanceof WearableChassis powerArmor) powerArmor.setArmorData(data);

    }
}