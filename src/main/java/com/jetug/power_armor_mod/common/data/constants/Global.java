package com.jetug.power_armor_mod.common.data.constants;

import com.jetug.power_armor_mod.common.util.helpers.timer.TickTimer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Global {
    public static final String MOD_ID = "power_armor_mod";
    public static final Logger LOGGER = LogManager.getLogger();
    public static final TickTimer CLIENT_TIMER = new TickTimer();
    public static final IEventBus MOD_EVENT_BUS = FMLJavaModLoadingContext.get().getModEventBus();

    public static Entity referenceMob = null;
}
