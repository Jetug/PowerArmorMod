package com.jetug.begining.common.entity.data;

public interface IPlayerData extends IData {
    boolean getIsInPowerArmor();
    void setIsInPowerArmor(boolean value);
    void copyFrom(IPlayerData source);

}
