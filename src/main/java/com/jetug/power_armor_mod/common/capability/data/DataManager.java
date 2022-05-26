package com.jetug.power_armor_mod.common.capability.data;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

public class DataManager {
//    public static IData getData(Entity entity){
//        if(entity instanceof PlayerEntity){
//            return getPlayerData((PlayerEntity)entity);
//        }
//        else if(entity instanceof PowerArmorPartEntity){
//            return getPowerArmorPartData((PowerArmorPartEntity)entity);
//        }
//        return null;
//    }

    public static IPlayerData getPlayerData(PlayerEntity player){
        return player.getCapability(PlayerDataProvider.PLAYER_DATA, null).orElse(null);
    }

    public static IArmorPartData getPowerArmorPartData(Entity entity){
        return entity.getCapability(ArmorDataProvider.POWER_ARMOR_PART_DATA, null).orElse(null);
    }

    public static IPlayerData getClientPlayerData(){
        ClientPlayerEntity clientPlayer = Minecraft.getInstance().player;
        return getPlayerData(clientPlayer);
    }
}
