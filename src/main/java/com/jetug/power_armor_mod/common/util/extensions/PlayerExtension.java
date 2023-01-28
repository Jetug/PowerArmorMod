package com.jetug.power_armor_mod.common.util.extensions;

import com.jetug.power_armor_mod.common.minecraft.entity.PowerArmorEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;

public class PlayerExtension {
    public static boolean isWearingPowerArmor(Player player){
        return player.getVehicle() instanceof PowerArmorEntity;
    }

    public static void sendMessage(String text){
        var player = Minecraft.getInstance().player;
        try {
            player.sendMessage(new TextComponent(text), player.getUUID());
        }
        catch (Exception ignored) {}
    }
}
