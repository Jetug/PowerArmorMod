package com.jetug.chassis_core.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import static com.jetug.chassis_core.common.util.helpers.PlayerUtils.*;

@Mixin(Player.class)
public abstract class PlayerMixin extends Entity {
    public PlayerMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(at = @At(value = "HEAD"), method = "wantsToStopRiding", cancellable = true)
    private void wantsToStopRiding(CallbackInfoReturnable<Boolean> cir) {
        if(isWearingChassis(this))
            cir.setReturnValue(false);
    }
}
