package com.jetug.chassis_core.mixin;

import net.minecraft.world.damagesource.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import static com.jetug.chassis_core.common.util.helpers.PlayerUtils.*;
import static net.minecraft.world.damagesource.CombatRules.*;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    protected LivingEntityMixin(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "getDamageAfterArmorAbsorb", at = @At("HEAD"), cancellable = true)
    protected void getDamageAfterArmorAbsorb(DamageSource pDamageSource, float pDamageAmount, CallbackInfoReturnable<Float> cir) {
        if (!pDamageSource.isBypassArmor() && isWearingChassis(this)) {
            var chassis = getPlayerChassis(this);
            chassis.damageArmor(pDamageSource, pDamageAmount);
            pDamageAmount = getDamageAfterAbsorb(pDamageAmount, chassis.getTotalDefense() ,chassis.getTotalToughness());
            cir.setReturnValue(pDamageAmount);

        }
    }
}
