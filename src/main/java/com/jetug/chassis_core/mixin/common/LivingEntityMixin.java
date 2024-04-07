package com.jetug.chassis_core.mixin.common;

import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.jetug.chassis_core.common.util.helpers.PlayerUtils.getEntityChassis;
import static com.jetug.chassis_core.common.util.helpers.PlayerUtils.isWearingChassis;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    protected LivingEntityMixin(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "getDamageAfterArmorAbsorb", at = @At("HEAD"), cancellable = true)
    protected void getDamageAfterArmorAbsorb(DamageSource pDamageSource, float pDamageAmount, CallbackInfoReturnable<Float> cir) {
//        var r1 = isWearingChassis(this);
//        var r2 =getEntityChassis(this);
//
//        ChassisCore.LOGGER.error(r1);
//        ChassisCore.LOGGER.error(r2);

        if (!pDamageSource.isBypassArmor() && isWearingChassis(this)) {
            var chassis = getEntityChassis(this);
            chassis.damageArmor(pDamageSource, pDamageAmount);
            pDamageAmount = CombatRules.getDamageAfterAbsorb(pDamageAmount, chassis.getTotalDefense(), chassis.getTotalToughness());
            cir.setReturnValue(pDamageAmount);
        }
    }
}
