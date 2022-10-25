package com.jetug.power_armor_mod.client.events;

import com.jetug.power_armor_mod.PowerArmorMod;
import com.jetug.power_armor_mod.client.render.renderers.*;
import com.jetug.power_armor_mod.common.minecraft.registery.ModEntityTypes;
import net.minecraft.advancements.criterion.PlayerHurtEntityTrigger;
import net.minecraft.client.renderer.debug.GameTestDebugRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderArmEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = PowerArmorMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class                               ClientModEvents {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event){
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.HITBOX_TEST_ENTITY.get(), HitboxTestRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.POWER_ARMOR.get(), PowerArmorRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.TEST_ENTITY.get(), TestRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.GECKO_ENTITY.get(), GeckoRenderer::new);
    }

    public static void onClientSetup(GameTestDebugRenderer e){

    }
}