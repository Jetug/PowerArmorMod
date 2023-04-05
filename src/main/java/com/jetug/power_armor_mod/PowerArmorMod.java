package com.jetug.power_armor_mod;

import com.jetug.power_armor_mod.common.foundation.registery.*;
import com.jetug.power_armor_mod.common.data.constants.Global;
import com.jetug.power_armor_mod.common.test.screen.ModMenuTypes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import software.bernie.geckolib3.GeckoLib;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

@Mod(Global.MOD_ID)
public class PowerArmorMod {
    public static final IEventBus MOD_EVENT_BUS = FMLJavaModLoadingContext.get().getModEventBus();

    public PowerArmorMod() {
        GeckoLib.initialize();
        register();
        EVENT_BUS.register(this);
    }

    private void register() {
        ContainerRegistry.register(MOD_EVENT_BUS);
        BlockEntitieRegistry.register(MOD_EVENT_BUS);
        BlockRegistry.register(MOD_EVENT_BUS);
        EntityTypeRegistry.register(MOD_EVENT_BUS);
        ModMenuTypes.register(MOD_EVENT_BUS);
        ItemRegistry.register(MOD_EVENT_BUS);
    }
}