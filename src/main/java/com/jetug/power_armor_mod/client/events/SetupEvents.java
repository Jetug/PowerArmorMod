package com.jetug.power_armor_mod.client.events;

import com.jetug.power_armor_mod.client.render.renderers.PowerArmorRenderer;
import com.jetug.power_armor_mod.common.util.constants.Global;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static com.jetug.power_armor_mod.client.KeyBindings.getKeys;
import static com.jetug.power_armor_mod.common.minecraft.registery.ModEntityTypes.POWER_ARMOR;

@Mod.EventBusSubscriber(modid = Global.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class SetupEvents {
//    @OnlyIn(Dist.CLIENT)
//    @SubscribeEvent
//    public static void onClientSetup(FMLClientSetupEvent event){
//        RenderingRegistry.registerEntityRenderingHandler(POWER_ARMOR.get(), PowerArmorRenderer::new);
//    }

    @SubscribeEvent
    public static void onClientSetup(EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(POWER_ARMOR.get(), PowerArmorRenderer::new);
    }


    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void clientSetup( FMLClientSetupEvent event) {
        for (KeyMapping key: getKeys())
            ClientRegistry.registerKeyBinding(key);
    }
}