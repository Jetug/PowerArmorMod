package com.jetug.chassis_core;

import com.jetug.chassis_core.common.foundation.registery.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib3.GeckoLib;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

@Mod(ChassisCore.MOD_ID)
public class ChassisCore {
    public static final String MOD_ID = "chassis_core";
    public static final Logger LOGGER = LogManager.getLogger();
    public static final IEventBus MOD_EVENT_BUS = FMLJavaModLoadingContext.get().getModEventBus();

    public ChassisCore() {
        GeckoLib.initialize();
        register();
        EVENT_BUS.register(this);
    }

    private void register() {
        ContainerRegistry.register(MOD_EVENT_BUS);
        EntityTypeRegistry.register(MOD_EVENT_BUS);
        ItemRegistry.register(MOD_EVENT_BUS);
    }
}