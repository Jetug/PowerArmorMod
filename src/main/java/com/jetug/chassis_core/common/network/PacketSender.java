package com.jetug.chassis_core.common.network;

import com.jetug.chassis_core.common.network.actions.Action;
import com.jetug.chassis_core.common.network.packet.ActionPacket;
import com.jetug.chassis_core.common.network.packet.GenericPacket;
import com.jetug.chassis_core.common.data.enums.ActionType;

import static com.jetug.chassis_core.common.network.PacketHandler.*;

@SuppressWarnings("rawtypes")
public class PacketSender {
    public static void doServerAction(ActionType action) {
        sendToServer(new ActionPacket(action));
    }

    public static void doServerAction(Action action, int entityId) {
        sendToServer(new GenericPacket(entityId, action));
    }

    public static void doClientAction(Action action) {
        sendToAllPlayers(new GenericPacket(-1, action));
    }
    public static void doClientAction(Action action, int entityId) {
        sendToAllPlayers(new GenericPacket(entityId, action));
    }
}
