package com.jetug.chassis_core.common.network;

import com.jetug.chassis_core.ChassisCore;
import com.jetug.chassis_core.common.config.CustomEquipmentManager;
import com.jetug.chassis_core.common.config.NetworkEquipmentManager;
import com.jetug.chassis_core.common.network.packet.S2CMessageUpdateEquipment;
import com.mrcrayfish.framework.api.FrameworkAPI;
import com.mrcrayfish.framework.api.network.FrameworkNetwork;
import com.mrcrayfish.framework.api.network.MessageDirection;
import net.minecraft.resources.ResourceLocation;

public class FrameworkPacketHandler {
    private static FrameworkNetwork PLAY_CHANNEL;

    public static FrameworkNetwork getPlayChannel() {
        return PLAY_CHANNEL;
    }

    public static void init() {
        PLAY_CHANNEL = FrameworkAPI.createNetworkBuilder(new ResourceLocation(ChassisCore.MOD_ID, "play"), 1)
                .registerPlayMessage(S2CMessageUpdateEquipment.class, MessageDirection.PLAY_CLIENT_BOUND)
                .build();

        FrameworkAPI.registerLoginData(new ResourceLocation(ChassisCore.MOD_ID, "network_equipment_manager"), NetworkEquipmentManager.LoginData::new);
        FrameworkAPI.registerLoginData(new ResourceLocation(ChassisCore.MOD_ID, "network_equipment_manager"), CustomEquipmentManager.LoginData::new);
    }
}
