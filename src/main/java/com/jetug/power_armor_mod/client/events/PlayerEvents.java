package com.jetug.power_armor_mod.client.events;

import com.jetug.power_armor_mod.client.render.*;
import com.jetug.power_armor_mod.client.render.renderers.*;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.common.*;

import static com.jetug.power_armor_mod.common.util.extensions.PlayerExtension.*;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class PlayerEvents
{
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onRenderPlayer(RenderPlayerEvent.Pre event) {
        if(isWearingPowerArmor(event.getPlayer())){
            event.setCanceled(true);
        }
    }

//    @OnlyIn(Dist.CLIENT)
//    @SubscribeEvent()
//    public static void onHandRender2(RenderPlayerEvent.Pre event)
//    {
//        event.setCanceled(true);
//        Minecraft mc = Minecraft.getInstance();
//
//        var ent = new PowerArmorEntity(EntityType.VEX, mc.level);
//
//        Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entity.getClass()).render()
//                .doRender(entity, event.getX(), event.getY(), event.getZ(),
//                        event.getPlayerPassenger().rotationYaw, event.getPartialRenderTick());
//
//
//        Entity entity = EntityType.byKey(GodsCrownItem.getSelectedMorphMobOf(stack).toString()).get().create(event.getEntity().world);
//        Minecraft.getInstance().levelRenderer.gre.getRenderManager()
//                .getRenderer(entity.getClass()).doRender(entity, event.getX(),
//                        event.getY(), event.getZ(), event.getPlayerPassenger().rotationYaw,
//                        event.getPartialRenderTick());
//
//
//        float x = 5.0F;
//        float y = 5.0F;
//        float z = 5.0F;
//        float width = 5.0F;
//        float height = 5.0F;
//
//        Minecraft mc = Minecraft.getInstance();
//
//        var renderManager = mc.levelRenderer;
//        LivingEntity player = mc.player;
//
//        GlStateManager.pushMatrix();
//        GlStateManager.translate(-renderManager.viewerPosX + x, -renderManager.viewerPosY + y, -renderManager.viewerPosZ + z);
//        GlStateManager.rotate(System.currentTimeMillis() / 5 % 360, 0, 1, 0);
//
//        GlStateManager.disableTexture2D();
//        GlStateManager.enableBlend();
//        GlStateManager.disableCull();
//        GlStateManager.color(120.0F, 130.0F, 296.0F, 0.5F);
//        GL11.glBegin(GL11.GL_QUADS);
//        GL11.glVertex3f(0, 0, 0);
//        GL11.glVertex3f(0 + width, 0, 0);
//        GL11.glVertex3f(0 + width, 0 + height, 0);
//        GL11.glVertex3f(0, 0 + height, 0);
//        GL11.glEnd();
//        GlStateManager.enableTexture2D();
//        GlStateManager.disableBlend();
//        GlStateManager.enableCull();
//
//        GlStateManager.popMatrix();
//    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent()
    public static void onCamera(EntityViewRenderEvent.CameraSetup event)
    {
        cameraSetup = event;
    }

    public static EntityViewRenderEvent.CameraSetup cameraSetup;
    public static CustomHandRenderer handRenderer;
    public static HandAmimator handAmimator;

    public static void applyItemArmTransform(PoseStack pMatrixStack, HumanoidArm pHand, float pEquippedProg) {
        int i = pHand == HumanoidArm.RIGHT ? 1 : -1;
        pMatrixStack.translate((float)i * 0.56F, -0.52F + pEquippedProg * -0.6F, -0.72F);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent()
    public static void onHandRender(RenderArmEvent event)
    {
        if(handRenderer == null)
            handRenderer = new CustomHandRenderer();

        var minecraft = Minecraft.getInstance();
        var gameRenderer = minecraft.gameRenderer;
        var camera = minecraft.gameRenderer.getMainCamera();
        var poseStack = new PoseStack();
        var partialTick = minecraft.getFrameTime();
        //poseStack.mulPose(Vector3f.ZP.rotationDegrees(cameraRoll));


        poseStack.mulPose(Vector3f.XP.rotationDegrees(camera.getXRot()));
        poseStack.mulPose(Vector3f.YP.rotationDegrees(camera.getYRot() + 180.0F));
        //applyItemArmTransform(poseStack, HumanoidArm.RIGHT, 1);

        //renderItemInHand
        Pose pose = poseStack.last();
        pose.pose().setIdentity();
        pose.normal().setIdentity();
        poseStack.pushPose();

        bobHurt(poseStack, partialTick);
        if (minecraft.options.bobView) {
            bobView(poseStack, partialTick);
        }
        ///
        renderHandsWithItems(partialTick, poseStack);
        ///
        poseStack.popPose();

        boolean flag = minecraft.getCameraEntity() instanceof LivingEntity && ((LivingEntity)minecraft.getCameraEntity()).isSleeping();

        if (minecraft.options.getCameraType().isFirstPerson() && !flag) {
            ScreenEffectRenderer.renderScreenEffect(minecraft, poseStack);
            bobHurt(poseStack, partialTick);
        }
        if (minecraft.options.bobView) {
            bobView(poseStack, partialTick);
        }

        handRenderer.render(
                null,
                poseStack,
                event.getMultiBufferSource(),
                event.getPackedLight());

//        HAND.get().getRenderer().renderByItem(
//                new ItemStack(HAND.get()),
//                ItemTransforms.TransformType.GROUND,
//                poseStack,
//                event.getMultiBufferSource(),
//                event.getPackedLight(),
//                OverlayTexture.NO_OVERLAY);

//        HAND.get().getRenderer().render(
//                HAND.get(),
//                event.getPoseStack(),
//                event.getMultiBufferSource(),
//                event.getPackedLight(),
//                new ItemStack(HAND.get())
//        );
        //IGeoRenderer
        event.setCanceled(true);
    }

    public static void renderHandsWithItems(float pPartialTicks, PoseStack pMatrixStack) {
        LocalPlayer pPlayerEntity = Minecraft.getInstance().player;
        float f2 = Mth.lerp(pPartialTicks, pPlayerEntity.xBobO, pPlayerEntity.xBob);
        float f3 = Mth.lerp(pPartialTicks, pPlayerEntity.yBobO, pPlayerEntity.yBob);
        pMatrixStack.mulPose(Vector3f.XP.rotationDegrees((pPlayerEntity.getViewXRot(pPartialTicks) - f2) * 0.1F));
        pMatrixStack.mulPose(Vector3f.YP.rotationDegrees((pPlayerEntity.getViewYRot(pPartialTicks) - f3) * 0.1F));
    }


    private static void bobView(PoseStack pMatrixStack, float pPartialTicks) {
        if (!(Minecraft.getInstance().getCameraEntity() instanceof Player player)) return;
        float dist = player.walkDist - player.walkDistO;
        float f1 = -(player.walkDist + dist * pPartialTicks);
        float f2 = Mth.lerp(pPartialTicks, player.oBob, player.bob);
        pMatrixStack.translate(Mth.sin(f1 * (float)Math.PI) * f2 * 0.5F, -Math.abs(Mth.cos(f1 * (float)Math.PI) * f2), 0.0D);
        pMatrixStack.mulPose(Vector3f.ZP.rotationDegrees(Mth.sin(f1 * (float)Math.PI) * f2 * 3.0F));
        pMatrixStack.mulPose(Vector3f.XP.rotationDegrees(Math.abs(Mth.cos(f1 * (float)Math.PI - 0.2F) * f2) * 5.0F));

        renderArmWithItem()
    }

    private static void bobHurt(PoseStack pMatrixStack, float pPartialTicks) {
        if (!(Minecraft.getInstance().getCameraEntity() instanceof LivingEntity livingentity)) return;

        float f = (float)livingentity.hurtTime - pPartialTicks;
        if (livingentity.isDeadOrDying()) {
            float f1 = Math.min((float)livingentity.deathTime + pPartialTicks, 20.0F);
            pMatrixStack.mulPose(Vector3f.ZP.rotationDegrees(40.0F - 8000.0F / (f1 + 200.0F)));
        }

        if (f < 0.0F) return;

        f /= (float)livingentity.hurtDuration;
        f = Mth.sin(f * f * f * f * (float)Math.PI);
        float f2 = livingentity.hurtDir;
        pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(-f2));
        pMatrixStack.mulPose(Vector3f.ZP.rotationDegrees(-f * 14.0F));
        pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(f2));
    }

    private void renderArmWithItem(AbstractClientPlayer pPlayer, InteractionHand pHand, float pSwingProgress, ItemStack pStack, float pEquippedProgress, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pCombinedLight) {
        if (!pPlayer.isScoping()) {
            boolean flag = pHand == InteractionHand.MAIN_HAND;
            HumanoidArm humanoidarm = flag ? pPlayer.getMainArm() : pPlayer.getMainArm().getOpposite();
            pMatrixStack.pushPose();
            if (pStack.isEmpty()) {
                if (!pPlayer.isInvisible()) {
                    this.renderPlayerArm(pMatrixStack, pBuffer, pCombinedLight, pEquippedProgress, pSwingProgress, humanoidarm);
                }
            }
//            else {
//                boolean flag3 = humanoidarm == HumanoidArm.RIGHT;
//                if (pPlayer.isUsingItem() && pPlayer.getUseItemRemainingTicks() > 0 && pPlayer.getUsedItemHand() == pHand) {
//                    applyItemArmTransform(pMatrixStack, humanoidarm, pEquippedProgress);
//                }
//                else if (pPlayer.isAutoSpinAttack()) {
//                    applyItemArmTransform(pMatrixStack, humanoidarm, pEquippedProgress);
//                    int j = flag3 ? 1 : -1;
//                    pMatrixStack.translate((float)j * -0.4F, 0.8F, 0.3F);
//                    pMatrixStack.mulPose(Vector3f.YP.rotationDegrees((float)j * 65.0F));
//                    pMatrixStack.mulPose(Vector3f.ZP.rotationDegrees((float)j * -85.0F));
//                }
//                else {
//                    float f5 = -0.4F * Mth.sin(Mth.sqrt(pSwingProgress) * (float)Math.PI);
//                    float f6 = 0.2F * Mth.sin(Mth.sqrt(pSwingProgress) * ((float)Math.PI * 2F));
//                    float f10 = -0.2F * Mth.sin(pSwingProgress * (float)Math.PI);
//                    int l = flag3 ? 1 : -1;
//                    pMatrixStack.translate((float)l * f5, f6, f10);
//                    applyItemArmTransform(pMatrixStack, humanoidarm, pEquippedProgress);
//                }
//
//
//            }

//                pMatrixStack.pushPose();
            pMatrixStack.translate(-0.5D, -0.5D, -0.5D);
//                pMatrixStack.popPose();
            pMatrixStack.popPose();
        }
    }

    private void renderPlayerArm(PoseStack pMatrixStack, MultiBufferSource pBuffer, int pCombinedLight, float pEquippedProgress, float pSwingProgress, HumanoidArm pSide) {
        boolean flag = pSide != HumanoidArm.LEFT;
        float f = flag ? 1.0F : -1.0F;
        float f1 = Mth.sqrt(pSwingProgress);
        float f2 = -0.3F * Mth.sin(f1 * (float)Math.PI);
        float f3 = 0.4F * Mth.sin(f1 * ((float)Math.PI * 2F));
        float f4 = -0.4F * Mth.sin(pSwingProgress * (float)Math.PI);
        pMatrixStack.translate((double)(f * (f2 + 0.64000005F)), (double)(f3 + -0.6F + pEquippedProgress * -0.6F), (double)(f4 + -0.71999997F));
        pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(f * 45.0F));
        float f5 = Mth.sin(pSwingProgress * pSwingProgress * (float)Math.PI);
        float f6 = Mth.sin(f1 * (float)Math.PI);
        pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(f * f6 * 70.0F));
        pMatrixStack.mulPose(Vector3f.ZP.rotationDegrees(f * f5 * -20.0F));
        AbstractClientPlayer abstractclientplayer = this.minecraft.player;
        RenderSystem.setShaderTexture(0, abstractclientplayer.getSkinTextureLocation());
        pMatrixStack.translate((double)(f * -1.0F), (double)3.6F, 3.5D);
        pMatrixStack.mulPose(Vector3f.ZP.rotationDegrees(f * 120.0F));
        pMatrixStack.mulPose(Vector3f.XP.rotationDegrees(200.0F));
        pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(f * -135.0F));
        pMatrixStack.translate((double)(f * 5.6F), 0.0D, 0.0D);
        PlayerRenderer playerrenderer = (PlayerRenderer)this.entityRenderDispatcher.<AbstractClientPlayer>getRenderer(abstractclientplayer);
        if (flag) {
            playerrenderer.renderRightHand(pMatrixStack, pBuffer, pCombinedLight, abstractclientplayer);
        } else {
            playerrenderer.renderLeftHand(pMatrixStack, pBuffer, pCombinedLight, abstractclientplayer);
        }

    }

//    @OnlyIn(Dist.CLIENT)
//    @SubscribeEvent()
//    public static void onItemRender( event)
//    {
//
//    }

//    @OnlyIn(Dist.CLIENT)
//    @SubscribeEvent()
//    public static void onCollide( event)
//    {
//        Player player = event.getPlayerPassenger();
//        Entity target = event.getTarget();
//
//        if(isWearingPowerArmor(player)){
//            Vec3 vc = player.getViewVector(1.0F);
//            target.push(vc.x * 20, vc.y * 20, vc.z * 20);
//        }
//    }
}
