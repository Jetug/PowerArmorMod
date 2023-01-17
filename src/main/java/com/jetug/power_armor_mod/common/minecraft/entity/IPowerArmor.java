package com.jetug.power_armor_mod.common.minecraft.entity;

import com.jetug.power_armor_mod.common.util.enums.BodyPart;

public interface IPowerArmor {
    public int getArmorDurability(BodyPart bodyPart);

    public void setArmorDurability(BodyPart bodyPart, float value);

    public void damageArmor(BodyPart bodyPart, float damage);
}
