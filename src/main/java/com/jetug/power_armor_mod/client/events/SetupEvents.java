package com.jetug.power_armor_mod.client.events;

import com.jetug.power_armor_mod.client.gui.GuiRegistry;
import com.jetug.power_armor_mod.client.render.ModGameRenderer;
import com.jetug.power_armor_mod.client.render.renderers.PowerArmorRenderer;
import com.jetug.power_armor_mod.common.util.constants.Global;
import com.jetug.power_armor_mod.test.screen.GemCuttingStationScreen;
import com.jetug.power_armor_mod.test.screen.ModMenuTypes;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

import java.lang.reflect.Field;

import static com.jetug.power_armor_mod.client.KeyBindings.getKeys;
import static com.jetug.power_armor_mod.common.minecraft.registery.ModEntityTypes.POWER_ARMOR;

@Mod.EventBusSubscriber(modid = Global.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class SetupEvents {

    @SubscribeEvent()
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(POWER_ARMOR.get(), PowerArmorRenderer::new);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        for (KeyMapping key: getKeys())
            ClientRegistry.registerKeyBinding(key);

        MenuScreens.register(ModMenuTypes.GEM_CUTTING_STATION_MENU.get(), GemCuttingStationScreen::new);
        event.enqueueWork(GuiRegistry::register);
    }
}