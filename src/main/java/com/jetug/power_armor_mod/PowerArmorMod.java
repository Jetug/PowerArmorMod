package com.jetug.power_armor_mod;

import com.jetug.power_armor_mod.client.render.ResourceHelper;
import com.jetug.power_armor_mod.common.capability.providers.ArmorDataProvider;
import com.jetug.power_armor_mod.common.capability.providers.PlayerDataProvider;
import com.jetug.power_armor_mod.common.minecraft.registery.ModBlocks;
import com.jetug.power_armor_mod.common.minecraft.registery.ModEntityTypes;
import com.jetug.power_armor_mod.common.minecraft.registery.ModItems;
import com.jetug.power_armor_mod.common.network.PacketHandler;
import com.jetug.power_armor_mod.common.util.constants.Global;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import software.bernie.geckolib3.GeckoLib;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Global.MOD_ID)
public class PowerArmorMod
{
    public PowerArmorMod() {
        GeckoLib.initialize();
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::setup);
        ModItems.register(eventBus);
        ModBlocks.register(eventBus);
        ModEntityTypes.register(eventBus);
        EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        PlayerDataProvider.register();
        ArmorDataProvider.register();
        PacketHandler.register();
        ResourceHelper.register();
    }
}
