package com.jetug.power_armor_mod.common.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;

public class MinecraftUtils {
    public static void sendMessage(String message, Entity entity){
        PlayerEntity player = Minecraft.getInstance().player;
        player.sendMessage(new StringTextComponent(message), entity.getUUID());
    }

}
