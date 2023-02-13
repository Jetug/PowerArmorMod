package com.jetug.power_armor_mod.common.foundation.entity;

import com.jetug.power_armor_mod.common.util.enums.BodyPart;

public interface IPowerArmor {
    public int getArmorDurability(BodyPart bodyPart);
    public void damageArmor(BodyPart bodyPart, float damage);
}
