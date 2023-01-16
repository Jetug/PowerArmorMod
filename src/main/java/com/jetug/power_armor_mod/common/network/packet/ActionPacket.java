package com.jetug.power_armor_mod.common.network.packet;

import com.jetug.power_armor_mod.common.util.enums.ActionType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ActionPacket{
    ActionType action = null;

    public ActionPacket(ActionType action) {
        this.action = action;
    }

    public ActionPacket() {}

    public static void write(ActionPacket message, FriendlyByteBuf buffer) {
        buffer.writeByte(message.action.getId());
    }

    public static ActionPacket read(FriendlyByteBuf buffer) {
        var action = ActionType.getById(buffer.readByte());
        return new ActionPacket(action);
    }

    public static void handle(ActionPacket message, Supplier<NetworkEvent.Context> context) {
        ServerPlayer player = context.get().getSender();
        if (player != null && message.action == ActionType.DISMOUNT) {
            player.stopRiding();
        }
    }
}