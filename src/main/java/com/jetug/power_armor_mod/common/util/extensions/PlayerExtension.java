package com.jetug.power_armor_mod.common.util.extensions;

import com.jetug.power_armor_mod.common.foundation.entity.PowerArmorEntity;
import com.jetug.power_armor_mod.common.data.enums.ActionType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import static com.jetug.power_armor_mod.client.ClientConfig.*;
import static com.jetug.power_armor_mod.common.network.PacketSender.*;

public class PlayerExtension {

    public static boolean isWearingPowerArmor(){
        return isWearingPowerArmor(getLocalPlayer());
    }

    public static boolean isWearingPowerArmor(Entity player){
        return player != null && player.getVehicle() instanceof PowerArmorEntity;
    }

    public static PowerArmorEntity getPlayerPowerArmor(){
        return getPlayerPowerArmor(getLocalPlayer());
    }

    public static PowerArmorEntity getPlayerPowerArmor(Player player){
        if(player.getVehicle() instanceof PowerArmorEntity)
            return (PowerArmorEntity) player.getVehicle();
        else return null;
    }

    public static void stopWearingArmor(Player player) {
        player.stopRiding();
        doServerAction(ActionType.DISMOUNT);
        player.setInvisible(false);
    }

    public static void sendMessage(String text){
        var player = Minecraft.getInstance().player;
        try {
            player.sendMessage(new TextComponent(text), player.getUUID());
        }
        catch (Exception ignored) {}
    }
}
