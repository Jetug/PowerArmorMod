package com.jetug.power_armor_mod.client.events;

import com.jetug.power_armor_mod.client.ClientConfig;
import com.jetug.power_armor_mod.client.gui.hud.HeatRenderer;
import com.jetug.power_armor_mod.common.foundation.registery.GuiRegistry;
import com.jetug.power_armor_mod.client.input.LongClickController;
import com.jetug.power_armor_mod.client.render.renderers.*;
import com.jetug.power_armor_mod.client.render.renderers.RenderNothing;
import com.jetug.power_armor_mod.common.data.constants.Global;
import com.jetug.power_armor_mod.client.input.DoubleClickController;
import com.jetug.power_armor_mod.client.gui.menu.ArmorStationGui;
import com.jetug.power_armor_mod.common.foundation.registery.ModMenuTypes;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.*;

import static com.jetug.power_armor_mod.client.KeyBindings.*;
import static com.jetug.power_armor_mod.client.render.renderers.CustomHandRenderer.*;
import static com.jetug.power_armor_mod.common.foundation.registery.EntityTypeRegistry.*;

@Mod.EventBusSubscriber(modid = Global.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class SetupEvents {
    @OnlyIn(Dist.CLIENT)
    public static final DoubleClickController DOUBLE_CLICK_CONTROLLER = new DoubleClickController();
    @OnlyIn(Dist.CLIENT)
    private static final LongClickController LONG_CLICK_CONTROLLER = new LongClickController();

    @SubscribeEvent()
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(POWER_ARMOR.get(), PowerArmorRenderer::new);
        event.registerEntityRenderer(POWER_ARMOR_PART.get(), RenderNothing::new);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        for (KeyMapping key: getKeys()) ClientRegistry.registerKeyBinding(key);

        MenuScreens.register(ModMenuTypes.GEM_CUTTING_STATION_MENU.get(), ArmorStationGui::new);
        event.enqueueWork(GuiRegistry::register);
        ClientConfig.resourceManager.loadConfigs();
        registerClickListeners();
        setupGui(event);
        registerHandRenderer();
    }

    private static void registerClickListeners() {
        DOUBLE_CLICK_CONTROLLER.addListener(InputEvents::onDoubleClick);
        LONG_CLICK_CONTROLLER.setRepeatListener(InputEvents::onLongClick);
        LONG_CLICK_CONTROLLER.setReleaseListener(InputEvents::onLongRelease);
    }

    public static void setupGui(FMLClientSetupEvent event)
    {
        OverlayRegistry.registerOverlayAbove(ForgeIngameGui.FOOD_LEVEL_ELEMENT, "Armor heat", new HeatRenderer());
    }
}