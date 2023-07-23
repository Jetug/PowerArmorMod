package com.jetug.power_armor_mod.common.network;

import com.jetug.power_armor_mod.common.network.actions.Action;
import com.jetug.power_armor_mod.common.network.packet.ActionPacket;
import com.jetug.power_armor_mod.common.network.packet.GenericPacket;
import com.jetug.power_armor_mod.common.data.enums.ActionType;

import static com.jetug.power_armor_mod.common.network.PacketHandler.*;

@SuppressWarnings("rawtypes")
public class PacketSender {
    public static void doServerAction(ActionType action) {
        sendToServer(new ActionPacket(action));
    }

    public static void doServerAction(Action action, int entityId) {
        sendToServer(new GenericPacket(entityId, action));
    }

//    public static void doClientAction(Action action, ServerPlayer player) {
//        sendTo(new GenericPacket(action), player);
//    }
}
