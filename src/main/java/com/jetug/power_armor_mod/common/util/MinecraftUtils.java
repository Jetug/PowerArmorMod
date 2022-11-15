package com.jetug.power_armor_mod.common.util;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;

public class MinecraftUtils {
    public static void sendMessage(String message, Entity entity){
        var player = Minecraft.getInstance().player;
//        player.sendMessage(new StringTextComponent(message), entity.getUUID());
    }

}
