package com.jetug.begining.common.entity.data;

public interface IPlayerData {
    boolean getIsInPowerArmor();
    void setIsInPowerArmor(boolean value);
    void copyFrom(IPlayerData source);

}
