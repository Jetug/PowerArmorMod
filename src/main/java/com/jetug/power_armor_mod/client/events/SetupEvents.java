package com.jetug.power_armor_mod.client.events;

import com.jetug.power_armor_mod.client.ClientConfig;
import com.jetug.power_armor_mod.client.gui.GuiRegistry;
import com.jetug.power_armor_mod.client.input.LongClickController;
import com.jetug.power_armor_mod.client.render.renderers.PowerArmorRenderer;
import com.jetug.power_armor_mod.client.render.renderers.RenderNothing;
import com.jetug.power_armor_mod.common.util.constants.Global;
import com.jetug.power_armor_mod.client.input.DoubleClickController;
import com.jetug.power_armor_mod.test.screen.*;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.*;

import static com.jetug.power_armor_mod.client.KeyBindings.*;
import static com.jetug.power_armor_mod.common.minecraft.registery.ModEntityTypes.*;

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
        for (KeyMapping key: getKeys())
            ClientRegistry.registerKeyBinding(key);

        MenuScreens.register(ModMenuTypes.GEM_CUTTING_STATION_MENU.get(), GemCuttingStationScreen::new);
        event.enqueueWork(GuiRegistry::register);

        ClientConfig.resourceManager.loadConfigs();
        registerClickListeners();
    }

    private static void registerClickListeners() {
        DOUBLE_CLICK_CONTROLLER.addListener(InputController::onDoubleClick);
        LONG_CLICK_CONTROLLER.addRepeatListener(InputController::onRepeat);
    }


}