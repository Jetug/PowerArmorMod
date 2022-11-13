package com.jetug.power_armor_mod.common.capability.data;

import com.jetug.power_armor_mod.common.capability.providers.ArmorDataProvider;
import com.jetug.power_armor_mod.common.capability.providers.PlayerDataProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class DataManager {
//    public static IData getData(Entity entity){
//        if(entity instanceof Player){
//            return getPlayerData((Player)entity);
//        }
//        else if(entity instanceof PowerArmorPartEntity){
//            return getPowerArmorPartData((PowerArmorPartEntity)entity);
//        }
//        return null;
//    }

    public static IPlayerData getPlayerData(Player player){
        return player.getCapability(PlayerDataProvider.PLAYER_DATA, null).orElse(null);
    }

    public static IArmorPartData getPowerArmorPartData(Entity entity){
        return entity.getCapability(ArmorDataProvider.POWER_ARMOR_PART_DATA, null).orElse(null);
    }

    public static IPlayerData getClientPlayerData(){
        LocalPlayer clientPlayer = Minecraft.getInstance().player;
        return getPlayerData(clientPlayer);
    }
}
