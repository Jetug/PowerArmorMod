package com.jetug.chassis_core.common.util.helpers;

import com.jetug.chassis_core.common.data.enums.ActionType;
import com.jetug.chassis_core.common.foundation.entity.WearableChassis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

import static com.jetug.chassis_core.common.network.PacketSender.doServerAction;

public class PlayerUtils {
    public static void addEffect(Player player, MobEffect effect, int amplifier){
        player.addEffect(new MobEffectInstance(effect, WearableChassis.EFFECT_DURATION, amplifier, false, false));
    }

    public static LocalPlayer getLocalPlayer(){
        return Minecraft.getInstance().player;
    }

    public static boolean isMainHandEmpty(){
        return getLocalPlayer().getMainHandItem().isEmpty();
    }

    public static boolean isWearingChassis(){
        return isWearingChassis(getLocalPlayer());
    }

    public static boolean isWearingChassis(Entity entity){
        return entity != null && entity.getVehicle() instanceof WearableChassis;
    }

    public static WearableChassis getPlayerChassis(){
        return getPlayerChassis(getLocalPlayer());
    }

    @Nullable
    public static WearableChassis getPlayerChassis(Entity entity){
        if(entity.getVehicle() instanceof WearableChassis)
            return (WearableChassis) entity.getVehicle();
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
