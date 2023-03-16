package com.jetug.power_armor_mod.mixin;

import com.jetug.power_armor_mod.common.foundation.entity.PowerArmorEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.jetug.power_armor_mod.common.util.extensions.PlayerExtension.*;

@Mixin(Mob.class)
public abstract class LivingEntityMixin {
    @Shadow private LivingEntity target;

    @Inject(method = "setTarget", at = @At("TAIL"))
    public void setTarget(LivingEntity pTarget, CallbackInfo ci) {
        if(pTarget instanceof Player player && isWearingPowerArmor(player)){
            target = (LivingEntity) player.getVehicle();
        }
        else if(pTarget instanceof PowerArmorEntity powerArmor
                && (!powerArmor.hasPlayerPassenger() || powerArmor.getPlayerPassenger().isCreative())){
            target = null;
        }
    }
}
