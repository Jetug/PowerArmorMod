package com.jetug.power_armor_mod.common.network.packet;

import com.jetug.power_armor_mod.common.util.enums.ActionType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PowerArmorPacket{
    ActionType action = null;
    private int entityId = -1;

    public PowerArmorPacket(int entityId, final ActionType action) {
        this.entityId = entityId;
        this.action = action;
    }

    public PowerArmorPacket() {}

    public static void write(PowerArmorPacket message, FriendlyByteBuf buffer) {
        buffer.writeByte(message.action.getId());
        buffer.writeInt(message.entityId);
    }

    public static PowerArmorPacket read(FriendlyByteBuf buffer) {
        var action = ActionType.getById(buffer.readByte());
        var entityId = buffer.readInt();
        return new PowerArmorPacket(entityId, action);
    }

    public static void handle(PowerArmorPacket message, Supplier<NetworkEvent.Context> context) {
        if(context.get().getDirection() == NetworkDirection.LOGIN_TO_CLIENT){
            var player = Minecraft.getInstance().player;
            var entity = player.level.getEntity(message.entityId);


        }
        else if(context.get().getDirection() == NetworkDirection.LOGIN_TO_SERVER) {
            var player = context.get().getSender();
            var entity = player.level.getEntity(message.entityId);

        }

    }
}