package com.jetug.power_armor_mod.client.events;

import com.jetug.power_armor_mod.client.render.renderers.*;
import com.mojang.blaze3d.vertex.*;
import net.minecraftforge.api.distmarker.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.common.*;

import static com.jetug.power_armor_mod.common.util.extensions.PlayerExtension.*;

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
    public static void onRenderHand(RenderArmEvent event) {
        if (!isWearingChassis()) return;

        var poseStack = new PoseStack();
        poseStack.translate(0.5, -0.5, -0.6);

        CustomHandRenderer.getHandRenderer().render(
                CustomHandRenderer.getHandAmimator(),
                getPlayerChassis(),
                poseStack,
                event.getMultiBufferSource(),
                event.getPackedLight());

        event.setCanceled(true);
    }
}