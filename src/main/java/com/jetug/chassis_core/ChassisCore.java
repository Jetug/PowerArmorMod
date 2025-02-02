package com.jetug.chassis_core;

import com.jetug.chassis_core.client.render.layers.PlayerSkinStorage;
import com.jetug.example.common.registery.EntityTypes;
import com.jetug.chassis_core.common.foundation.registery.ItemRegistry;
import com.jetug.chassis_core.common.network.PacketHandler;
import com.jetug.example.common.registery.ContainerRegistry;
import mod.azure.azurelib.AzureLib;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

@Mod(ChassisCore.MOD_ID)
public class ChassisCore {
    public static final String MOD_ID = "chassis_core";
    public static final Logger LOGGER = LogManager.getLogger();
    public static final IEventBus MOD_EVENT_BUS = FMLJavaModLoadingContext.get().getModEventBus();
    public static final PlayerSkinStorage SKIN_STORAGE = PlayerSkinStorage.INSTANCE;

    public ChassisCore() {
        AzureLib.initialize();
        register();
        EVENT_BUS.register(this);
        MOD_EVENT_BUS.addListener(this::onCommonSetup);

    }

    private void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(PacketHandler::register);
    }

    private void register() {
        ItemRegistry.register(MOD_EVENT_BUS);
        ContainerRegistry.register(MOD_EVENT_BUS);
        EntityTypes.register(MOD_EVENT_BUS);
    }
}