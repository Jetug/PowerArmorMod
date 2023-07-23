package com.jetug.power_armor_mod.common.network.packet;

import com.jetug.power_armor_mod.common.network.actions.Action;
import com.jetug.power_armor_mod.common.network.ActionRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static com.jetug.power_armor_mod.common.util.extensions.PlayerExtension.isWearingPowerArmor;

@SuppressWarnings("ALL")
public class GenericPacket {
    int entityId = -1;
    Action action = null;

    public GenericPacket(int entityId, Action action) {
        this.entityId = entityId;
        this.action = action;
    }

    public GenericPacket() {}

    public static void write(GenericPacket message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.entityId);
        buffer.writeInt(message.action.getId());
        message.action.write(buffer);
    }

    public static GenericPacket read(FriendlyByteBuf buffer) {
        var entityId = buffer.readInt();
        var action = ActionRegistry.getAction(buffer.readInt());
        return new GenericPacket(entityId, action.read(buffer));
    }

    public static void handle(GenericPacket message, Supplier<NetworkEvent.Context> context) {
        boolean isClientSide = context.get().getDirection().getReceptionSide() == LogicalSide.CLIENT;
        if(isClientSide){
            message.action.doClientAction(message.action, context, message.entityId);
        }
        else{
            message.action.doServerAction(message.action, context, message.entityId);
        }
    }
}