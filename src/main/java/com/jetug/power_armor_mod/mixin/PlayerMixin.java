package com.jetug.power_armor_mod.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import static com.jetug.power_armor_mod.common.util.extensions.PlayerExtension.*;

@Mixin(Player.class)
public abstract class PlayerMixin extends Entity {

    public PlayerMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(at = @At(value = "HEAD"), method = "wantsToStopRiding", cancellable = true)
    private void wantsToStopRiding(CallbackInfoReturnable<Boolean> cir) {
        if(isWearingPowerArmor(this))
            cir.setReturnValue(false);
    }
}
