package com.jetug.begining.common.entity.data;

import com.jetug.begining.common.entity.entity_type.PowerArmorPartEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

public class DataManager {
    public static IData getData(Entity entity){
        if(entity instanceof PlayerEntity){
            return getPlayerData((PlayerEntity)entity);
        }
        else if(entity instanceof PowerArmorPartEntity){
            return getPowerArmorPartData((PowerArmorPartEntity)entity);
        }
        return null;
    }
    public static IPlayerData getPlayerData(PlayerEntity player){
        return (IPlayerData) player.getCapability(PlayerDataProvider.PLAYER_DATA, null);
    }

    public static IPowerArmorPartData getPowerArmorPartData(PowerArmorPartEntity entity){
        return (IPowerArmorPartData) entity.getCapability(PlayerDataProvider.PLAYER_DATA, null);
    }

    public static IPlayerData getClientPlayerData(){
        ClientPlayerEntity clientPlayer = Minecraft.getInstance().player;
        return getPlayerData(clientPlayer);
    }
}
