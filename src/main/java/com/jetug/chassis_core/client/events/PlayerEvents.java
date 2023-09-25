package com.jetug.chassis_core.client.events;

import com.jetug.chassis_core.client.render.renderers.*;
import com.jetug.chassis_core.common.util.helpers.PlayerUtils;
import com.mojang.blaze3d.vertex.*;
import net.minecraftforge.api.distmarker.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.common.*;

import static com.jetug.chassis_core.common.util.helpers.PlayerUtils.*;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class PlayerEvents {
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onRenderPlayer(RenderPlayerEvent.Pre event) {
        if (isWearingChassis(event.getPlayer())) {
            event.setCanceled(true);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent()
    public static void onRenderHand(RenderHandEvent event) {
        if (!isWearingChassis() || !isMainHandEmpty()) return;

        var poseStack = event.getPoseStack();
        poseStack.pushPose();
        poseStack.translate(0.5, -0.5, -0.6);

        CustomHandRenderer.getHandRenderer().render(
                getPlayerChassis(),
                event.getPoseStack(),
                event.getMultiBufferSource(),
                event.getPackedLight());

        poseStack.popPose();
        //event.setCanceled(true);
    }

//    @OnlyIn(Dist.CLIENT)
//    @SubscribeEvent()
//    public static void onRenderHand(RenderArmEvent event) {
//        if (!isWearingChassis()) return;
//
//        var poseStack = event.getPoseStack();
//        poseStack.translate(0.5, -0.5, -0.6);
//
//        CustomHandRenderer.getHandRenderer().render(
//                getPlayerChassis(),
//                poseStack,
//                event.getMultiBufferSource(),
//                event.getPackedLight());
//
//        event.setCanceled(true);
//    }
}