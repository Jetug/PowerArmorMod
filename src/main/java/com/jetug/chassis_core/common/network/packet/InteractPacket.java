package com.jetug.chassis_core.common.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class InteractPacket {
    int entityId = -1;
    InteractionHand hand;
    Vec3 location;

    public InteractPacket(int entityId, InteractionHand isMainHand, Vec3 location) {
        this.entityId = entityId;
        this.hand = isMainHand;
        this.location = location;
    }

    public InteractPacket() {}

    public static void write(InteractPacket message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.entityId);
        buffer.writeEnum(message.hand);
        buffer.writeDouble(message.location.x);
        buffer.writeDouble(message.location.y);
        buffer.writeDouble(message.location.z);
    }

    public static InteractPacket read(FriendlyByteBuf buffer) {
        var entityId = buffer.readInt();
        var isMainHand = buffer.readEnum(InteractionHand.class);
        var location = new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());

        return new InteractPacket(entityId, isMainHand, location);
    }

    public static void handle(InteractPacket message, Supplier<NetworkEvent.Context> context) {
        ServerPlayer player = context.get().getSender();
        if (player == null) return;
        var entity = player.level.getEntity(message.entityId);
        if (entity == null) return;
        entity.interactAt(player, message.location ,message.hand);
    }
}