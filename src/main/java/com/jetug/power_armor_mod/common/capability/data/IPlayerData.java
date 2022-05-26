package com.jetug.power_armor_mod.common.capability.data;

public interface IPlayerData{
    boolean getIsInPowerArmor();
    void setIsInPowerArmor(boolean value);
    void copyFrom(IPlayerData source);

}
