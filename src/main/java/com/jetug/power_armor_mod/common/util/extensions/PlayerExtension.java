package com.jetug.power_armor_mod.common.util.extensions;

import com.jetug.power_armor_mod.common.foundation.entity.PowerArmorEntity;
import com.jetug.power_armor_mod.common.util.enums.ActionType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;

import static com.jetug.power_armor_mod.common.network.PacketSender.*;

public class PlayerExtension {

    public static boolean isWearingPowerArmor(){
        var player = Minecraft.getInstance().player;
        return player != null && isWearingPowerArmor(player);
    }

    public static boolean isWearingPowerArmor(Player player){
        return player.getVehicle() instanceof PowerArmorEntity;
    }

    public static PowerArmorEntity getLocalPlayerArmor(){
        try{
            return (PowerArmorEntity) Minecraft.getInstance().player.getVehicle();
        }
        catch (Exception e){
            return null;
        }
    }

    public static PowerArmorEntity getPlayerArmor(Player player){
        try{ return (PowerArmorEntity) player.getVehicle(); }
        catch (Exception e){ return null; }
    }

    public static void stopWearingArmor(LocalPlayer player) {
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
