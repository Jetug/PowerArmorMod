package com.jetug.power_armor_mod.common.capability.data;

public class ModPlayerData implements IPlayerData{
    public static final String IS_IN_POWER_ARMOR = "isInPowerArmor";

    private boolean isInPowerArmor;

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
