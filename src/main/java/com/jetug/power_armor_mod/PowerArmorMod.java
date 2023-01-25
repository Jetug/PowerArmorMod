package com.jetug.power_armor_mod;

import com.jetug.power_armor_mod.common.minecraft.registery.ContainerRegistry;
import com.jetug.power_armor_mod.client.render.ResourceHelper;
import com.jetug.power_armor_mod.common.minecraft.registery.ModBlockEntities;
import com.jetug.power_armor_mod.common.minecraft.registery.ModBlocks;
import com.jetug.power_armor_mod.common.minecraft.registery.ModEntityTypes;
import com.jetug.power_armor_mod.common.minecraft.registery.ModItems;
import com.jetug.power_armor_mod.common.network.PacketHandler;
import com.jetug.power_armor_mod.common.util.constants.Global;
import com.jetug.power_armor_mod.test.screen.ModMenuTypes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import software.bernie.geckolib3.GeckoLib;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

@Mod(Global.MOD_ID)
public class PowerArmorMod
{
    public static final IEventBus MOD_EVENT_BUS = FMLJavaModLoadingContext.get().getModEventBus();

    public PowerArmorMod() {
        GeckoLib.initialize();
        register();
        EVENT_BUS.register(this);
    }

    private void register() {
        ContainerRegistry.register(MOD_EVENT_BUS);
        ModBlockEntities.register(MOD_EVENT_BUS);
        ModBlocks.register(MOD_EVENT_BUS);
        ModEntityTypes.register(MOD_EVENT_BUS);
        ModMenuTypes.register(MOD_EVENT_BUS);
        ModItems.register(MOD_EVENT_BUS);
    }
}