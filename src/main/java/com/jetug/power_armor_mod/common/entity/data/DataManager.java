package com.jetug.power_armor_mod.common.entity.data;

import com.jetug.power_armor_mod.common.entity.entity_type.PowerArmorPartEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
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

//    public static IPlayerData getPlayerData(PlayerEntity player, @Nullable Direction facing){
//        return player.getCapability(PlayerDataProvider.PLAYER_DATA, facing).orElse(null);
//    }

    public static IPowerArmorPartData getPowerArmorPartData(PowerArmorPartEntity entity){
        return entity.getCapability(PowerArmorDataProvider.POWER_ARMOR_PART_DATA, null).orElse(null);
    }

    public static IPlayerData getClientPlayerData(){
        ClientPlayerEntity clientPlayer = Minecraft.getInstance().player;
        return getPlayerData(clientPlayer);
    }
}
