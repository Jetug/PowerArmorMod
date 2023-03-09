package com.jetug.power_armor_mod.mixin;

import com.jetug.power_armor_mod.common.foundation.entity.PowerArmorEntity;
import com.jetug.power_armor_mod.common.foundation.entity.PowerArmorPartEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.damagesource.CombatTracker;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import software.bernie.shadowed.eliotlash.mclib.math.functions.classic.Pow;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Predicate;

import static com.jetug.power_armor_mod.common.util.extensions.PlayerExtension.*;

@Mixin(Mob.class)
public abstract class LivingEntityMixin {
    @Shadow private LivingEntity target;

    @Inject(method = "setTarget", at = @At("TAIL"))
    public void setTarget(LivingEntity pTarget, CallbackInfo ci) {
        if(pTarget instanceof Player player && isWearingPowerArmor(player)){
            target = (LivingEntity) player.getVehicle();
        }
        else if(pTarget instanceof PowerArmorEntity powerArmor && !powerArmor.hasPlayer()){
            target = null;
        }
    }
}
