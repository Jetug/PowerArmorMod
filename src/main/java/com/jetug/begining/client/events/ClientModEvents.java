package com.jetug.begining.client.events;

import com.jetug.begining.ExampleMod;
import com.jetug.begining.common.registery.ModEntityTypes;
import com.jetug.begining.client.render.GeckoRenderer;
import com.jetug.begining.client.render.PowerArmorRenderer;
import com.jetug.begining.client.render.TestRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = ExampleMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientModEvents {
    private ClientModEvents(){}

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event){
        //event.registerLayerDefinition(PowerArmorModel.LAYER_LOCATION, PowerArmorModel::createBodyLayer);
        //event.registerLayerDefinition(TestModel.LAYER_LOCATION, TestModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event){
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.POWER_ARMOR.get(), manager -> new PowerArmorRenderer(manager));
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.TEST_ENTITY.get(), TestRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.GECKO_ENTITY.get(), GeckoRenderer::new);
        //RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.PLAYER_POWER_ARMOR.get(), PlayerPowerArmorRenderer::new);

        //RenderingRegistry.registerEntityRenderingHandler(EntityType.PLAYER, TestRenderer::new);
    }
}