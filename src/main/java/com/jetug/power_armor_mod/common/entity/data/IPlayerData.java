package com.jetug.power_armor_mod.common.entity.data;

public interface IPlayerData extends IData {
    boolean getIsInPowerArmor();
    void setIsInPowerArmor(boolean value);
    void copyFrom(IPlayerData source);

}
