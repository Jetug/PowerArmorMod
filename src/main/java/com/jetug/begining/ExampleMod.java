package com.jetug.begining;

import com.jetug.begining.client.Messages;
import com.jetug.begining.common.entity.data.*;
import com.jetug.begining.common.events.EventHandler;
import com.jetug.begining.common.registery.ModEntityTypes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import software.bernie.geckolib3.GeckoLib;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ExampleMod.MOD_ID)
public class ExampleMod
{
    public static final String MOD_ID = "begining";

    public ExampleMod()
    {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::setup);

        GeckoLib.initialize();

//        ModItems.register(eventBus);
//        ModBlocks.register(eventBus);
        ModEntityTypes.register(eventBus);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        //Messages.register();
        CapabilityManager.INSTANCE.register(IPlayerData.class, new PlayerDataStorage(), ModPlayerData::new);
        CapabilityManager.INSTANCE.register(IPowerArmorPartData.class, new PowerArmorPartDataStorage(), PowerArmorPartData::new);
    }
}
