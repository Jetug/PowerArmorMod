package com.jetug.chassis_core.mixin.client;

import com.jetug.chassis_core.common.util.helpers.PlayerUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ForgeGui.class)
@OnlyIn(Dist.CLIENT)
public class ForgeIngameGuiMixin extends Gui {
    public ForgeIngameGuiMixin(Minecraft mc) {
        super(mc, mc.getItemRenderer());
    }

    @Inject(method = "renderHealthMount(IILcom/mojang/blaze3d/vertex/PoseStack;)V", at = @At("HEAD"), cancellable = true, remap = false)
    protected void renderHealthMount(int width, int height, PoseStack poseStack, CallbackInfo ci) {
        if (PlayerUtils.isLocalWearingChassis()) ci.cancel();
    }

    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;setSeed(J)V"))
    public void render(PoseStack poseStack, float partialTick, CallbackInfo ci) {
        IGuiOverlay overlay = (gui, poseStack1, partialTick1, screenWidth, screenHeight) -> {
            var minecraft = Minecraft.getInstance();
            if (PlayerUtils.isLocalWearingChassis() && !minecraft.options.hideGui && gui.shouldDrawSurvivalElements()) {
                gui.setupOverlayRenderState(true, false);
                gui.renderFood(screenWidth, screenHeight, poseStack1);
            }
        };

        overlay.render((ForgeGui) (Gui) this, poseStack, partialTick, screenWidth, screenHeight);
    }
//
//    @Final
//    @Shadow(remap = false)
//    public static final IIngameOverlay FOOD_LEVEL_ELEMENT = OverlayRegistry.registerOverlayTop("Food Level", (gui, poseStack, partialTick, screenWidth, screenHeight) -> {
//        var minecraft = Minecraft.getInstance();
//        if (PlayerUtils.isLocalWearingChassis() && !minecraft.options.hideGui && gui.shouldDrawSurvivalElements()) {
//            gui.setupOverlayRenderState(true, false);
//            gui.renderFood(screenWidth, screenHeight, poseStack);
//        }
//    });
}
