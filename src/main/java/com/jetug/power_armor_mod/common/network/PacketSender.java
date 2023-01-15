package com.jetug.power_armor_mod.common.network;

import com.jetug.power_armor_mod.common.network.packet.ActionPacket;
import com.jetug.power_armor_mod.common.util.enums.ActionType;

import static com.jetug.power_armor_mod.common.network.PacketHandler.*;

public class PacketSender {
    public static void doServerAction(ActionType action) {
        sendToServer(new ActionPacket(action));
    }
}
