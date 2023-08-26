package com.jetug.power_armor_mod.common.foundation.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class PowerArmorFrame extends ArmorChassisEntity{
    public PowerArmorFrame(EntityType<? extends ArmorChassisBase> type, Level worldIn) {
        super(type, worldIn);
    }
}
