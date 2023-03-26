package com.jetug.power_armor_mod.client.events;

import com.jetug.power_armor_mod.common.foundation.entity.PowerArmorEntity;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderArmEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

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
    public static void onHandRender(RenderArmEvent event)
    {
        HAND.get().getRenderer().renderByItem(
                new ItemStack(HAND.get()),
                ItemTransforms.TransformType.GROUND,
                event.getPoseStack(),
                event.getMultiBufferSource(),
                event.getPackedLight(),
                OverlayTexture.NO_OVERLAY);

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
