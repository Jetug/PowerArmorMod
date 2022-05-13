package com.jetug.power_armor_mod.common.util.constants;

import com.jetug.power_armor_mod.PowerArmorMod;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;

public class Attributes {
    public static final Attribute HEAD_ARMOR_HEALTH
            = new RangedAttribute(PowerArmorMod.MOD_ID + ".head_armor_health", 10.0D, 0.0D, Double.MAX_VALUE).setSyncable(true);
    public static final Attribute BODY_ARMOR_HEALTH
            = new RangedAttribute(PowerArmorMod.MOD_ID + ".body_armor_health", 10.0D, 0.0D, Double.MAX_VALUE).setSyncable(true);
    public static final Attribute LEFT_ARM_ARMOR_HEALTH
            = new RangedAttribute(PowerArmorMod.MOD_ID + ".left_arm_armor_health", 10.0D, 0.0D, Double.MAX_VALUE).setSyncable(true);
    public static final Attribute RIGHT_ARM_ARMOR_HEALTH
            = new RangedAttribute(PowerArmorMod.MOD_ID + ".right_arm_armor_health", 10.0D, 0.0D, Double.MAX_VALUE).setSyncable(true);
    public static final Attribute LEFT_LEG_ARMOR_HEALTH
            = new RangedAttribute(PowerArmorMod.MOD_ID + ".left_leg_armor_health", 10.0D, 0.0D, Double.MAX_VALUE).setSyncable(true);
    public static final Attribute RIGHT_LEG_ARMOR_HEALTH
            = new RangedAttribute(PowerArmorMod.MOD_ID + ".right_leg_armor_health", 10.0D, 0.0D, Double.MAX_VALUE).setSyncable(true);
}
