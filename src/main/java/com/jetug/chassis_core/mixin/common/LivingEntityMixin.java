package com.jetug.chassis_core.mixin.common;

import com.jetug.chassis_core.ChassisCore;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import static com.jetug.chassis_core.common.util.helpers.PlayerUtils.*;

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
            pDamageAmount = CombatRules.getDamageAfterAbsorb(pDamageAmount, chassis.getTotalDefense() ,chassis.getTotalToughness());
            cir.setReturnValue(pDamageAmount);
        }
    }
}
