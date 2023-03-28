package com.jetug.power_armor_mod.client.events;

import com.jetug.power_armor_mod.client.render.*;
import com.jetug.power_armor_mod.client.render.renderers.*;
import com.jetug.power_armor_mod.common.foundation.entity.PowerArmorEntity;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix3f;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.common.*;
import org.lwjgl.opengl.GL11;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

import static com.jetug.power_armor_mod.client.render.renderers.item.HandRenderer.HAND_MODEL;
import static com.jetug.power_armor_mod.common.foundation.registery.ItemRegistry.HAND;
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
        cameraRoll = event.getRoll();
    }

    public static float cameraRoll;
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

        var camera = minecraft.cameraEntity;
        var poseStack = new PoseStack();

        //poseStack.mulPose(Vector3f.ZP.rotationDegrees(cameraRoll));

        poseStack.mulPose(Vector3f.XP.rotationDegrees(camera.getXRot()));
        poseStack.mulPose(Vector3f.YP.rotationDegrees(camera.getYRot() + 180.0F));
//        Matrix3f matrix3f = poseStack.last().normal().copy();
//        if (matrix3f.invert()) {
//            RenderSystem.setInverseViewRotationMatrix(matrix3f);
//        }

        ///
//        var poseStack = new PoseStack();
//        poseStack.translate(-556, -59, 62);

        //poseStack.pushPose();

        applyItemArmTransform(poseStack, HumanoidArm.RIGHT, 1);

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
