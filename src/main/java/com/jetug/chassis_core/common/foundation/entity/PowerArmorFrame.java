package com.jetug.chassis_core.common.foundation.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.manager.AnimationData;

public class PowerArmorFrame extends WearableChassis {
    public PowerArmorFrame(EntityType<? extends ArmorChassisBase> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    public void registerControllers(AnimationData data) {

    }
}
