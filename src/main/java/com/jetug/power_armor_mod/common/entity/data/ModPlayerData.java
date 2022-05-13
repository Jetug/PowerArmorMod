package com.jetug.power_armor_mod.common.entity.data;

public class ModPlayerData implements IPlayerData{
    private boolean isInPowerArmor;

    public static final String IS_IN_POWER_ARMOR = "isInPowerArmor";

//    public static IPlayerData getPlayerData(PlayerEntity player){
//        return getPlayerData(player, null);
//    }
//
//    public static IPlayerData getPlayerData(PlayerEntity player, @Nullable Direction facing){
//        return (IPlayerData) player.getCapability(PlayerDataProvider.PLAYER_DATA, facing);
//    }

    public boolean getIsInPowerArmor(){
        return isInPowerArmor;
    }

    public void setIsInPowerArmor(boolean value){
        isInPowerArmor = value;
    }

    public void copyFrom(IPlayerData source){
        isInPowerArmor = source.getIsInPowerArmor();
    }
}
