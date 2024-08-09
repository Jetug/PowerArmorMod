package com.jetug.chassis_core.mixin.client;

import com.jetug.chassis_core.common.util.helpers.PlayerUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.UUID;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin implements AutoCloseable {
    @Shadow @Final Minecraft minecraft;

    @Inject(method = "bobView(Lcom/mojang/blaze3d/vertex/PoseStack;F)V", at = @At("HEAD"), cancellable = true)
    private void bobView(PoseStack pPoseStack, float pPartialTicks, CallbackInfo ci) {
        if (this.minecraft.getCameraEntity() instanceof Player player &&
                PlayerUtils.isWearingChassis(player)) {
            var chassis = PlayerUtils.getEntityChassis(player);
            assert chassis != null;
            float speed = chassis.walkDist - chassis.walkDistO;
            float f1 = -(chassis.walkDist + speed * pPartialTicks);
            float bob = Mth.lerp(pPartialTicks, chassis.oBob, chassis.bob);
            pPoseStack.translate(
                    Mth.sin(f1 * (float)Math.PI) * bob * 0.5F,
                    -Math.abs(Mth.cos(f1 * (float)Math.PI) * bob),
                    0.0F);
            pPoseStack.mulPose(Axis.ZP.rotationDegrees(Mth.sin(f1 * (float)Math.PI) * bob * 3.0F));
            pPoseStack.mulPose(Axis.XP.rotationDegrees(Math.abs(Mth.cos(f1 * (float)Math.PI - 0.2F) * bob) * 5.0F));
            ci.cancel();
        }
    }
}
