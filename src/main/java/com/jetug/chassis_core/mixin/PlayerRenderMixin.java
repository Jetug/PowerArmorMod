package com.jetug.chassis_core.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(PlayerRenderer.class)
public class
PlayerRenderMixin {
    private static final String getEntityHitResult = "getEntityHitResult(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;D)Lnet/minecraft/world/phys/EntityHitResult;";

    @Inject(at = @At(value = "TAIL"), method = "renderHand", cancellable = true, remap = false)
    private void renderHand(PoseStack pMatrixStack, MultiBufferSource pBuffer, int pCombinedLight, AbstractClientPlayer pPlayer,
                            ModelPart pRendererArm, ModelPart pRendererArmwear, CallbackInfo ci) {

//        if(isWearingChassis()){
//            var renderer = HAND.get().getRenderer();
//            //renderer.renderByItem(new ItemStack(HAND.get()), ItemTransforms.TransformType.GROUND, pMatrixStack, pBuffer, pCombinedLight, 0);
//
//            var entity = getPlayerChassis();
//            Minecraft.getInstance().getItemInHandRenderer().renderItem(entity, new ItemStack(HAND.get()), ItemTransforms.TransformType.GROUND, false, pMatrixStack, pBuffer, pCombinedLight);
//
//            ci.cancel();
//        }
    }
}