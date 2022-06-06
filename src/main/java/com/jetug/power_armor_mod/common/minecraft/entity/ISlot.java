package com.jetug.power_armor_mod.common.minecraft.entity;

import com.jetug.power_armor_mod.common.util.enums.BodyPart;
import com.jetug.power_armor_mod.common.util.enums.EquipmentType;

public interface ISlot {
    public BodyPart getArmorPart();
    public EquipmentType getType();
    public void setType(EquipmentType type);
}
