package com.jetug.power_armor_mod.client.events;

import com.jetug.power_armor_mod.client.render.*;
import com.jetug.power_armor_mod.client.render.renderers.*;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
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

//        camera.setup(minecraft.level,
//                minecraft.getCameraEntity() == null ? minecraft.player : minecraft.getCameraEntity(),
//                !minecraft.options.getCameraType().isFirstPerson(),
//                minecraft.options.getCameraType().isMirrored(), 1.0F);
//
//        camera.setAnglesInternal(cameraSetup.getYaw(), cameraSetup.getPitch());


//        Matrix3f matrix3f = poseStack.last().normal().copy();
//        if (matrix3f.invert()) {
//            RenderSystem.setInverseViewRotationMatrix(matrix3f);
//        }

        ///
//        var poseStack = new PoseStack();
//        poseStack.translate(-556, -59, 62);

        //poseStack.pushPose();


//www

        poseStack.mulPose(Vector3f.XP.rotationDegrees(camera.getXRot()));
        poseStack.mulPose(Vector3f.YP.rotationDegrees(camera.getYRot() + 180.0F));
        applyItemArmTransform(poseStack, HumanoidArm.RIGHT, 1);
        poseStack.pushPose();


        bobView(poseStack, partialTick);

//        double d0 = minecraft.options.fov;
//
//        minecraft.levelRenderer.prepareCullFrustum(poseStack, camera.getPosition(),
//                gameRenderer.getProjectionMatrix(Math.max(d0, minecraft.options.fov)));



//        try {
//            poseStack.pushPose();
//            var mt = GameRenderer.class.getDeclaredMethod("bobView", PoseStack.class, float.class);
//            mt.setAccessible(true);
//
//            mt.invoke(gameRenderer, poseStack, 1f);
//            poseStack.popPose();
//
//            mt.invoke(gameRenderer, poseStack, 1f);
//
//        } catch (Exception e) {
//            var i = e;
//        }

//        poseStack.translate(-0.5D, -0.5D, -0.5D);
//        poseStack.scale(1.0F, -1.0F, -1.0F);
//        poseStack.translate(0.5f, 0.51f, 0.5f);

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

    private static void bobView(PoseStack pMatrixStack, float pPartialTicks) {
        if (!(Minecraft.getInstance().getCameraEntity() instanceof Player player)) return;
        float dist = player.walkDist - player.walkDistO;
        float f1 = -(player.walkDist + dist * pPartialTicks);
        float f2 = Mth.lerp(pPartialTicks, player.oBob, player.bob);
        pMatrixStack.translate(Mth.sin(f1 * (float)Math.PI) * f2 * 0.5F, -Math.abs(Mth.cos(f1 * (float)Math.PI) * f2), 0.0D);
        pMatrixStack.mulPose(Vector3f.ZP.rotationDegrees(Mth.sin(f1 * (float)Math.PI) * f2 * 3.0F));
        pMatrixStack.mulPose(Vector3f.XP.rotationDegrees(Math.abs(Mth.cos(f1 * (float)Math.PI - 0.2F) * f2) * 5.0F));
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
