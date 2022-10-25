package com.jetug.power_armor_mod;

import com.jetug.power_armor_mod.client.render.ResourceHelper;
import com.jetug.power_armor_mod.common.capability.data.IPlayerData;
import com.jetug.power_armor_mod.common.capability.data.PlayerDataProvider;
import com.jetug.power_armor_mod.common.capability.data.ArmorDataProvider;
import com.jetug.power_armor_mod.common.minecraft.entity.PowerArmorEntity;
import com.jetug.power_armor_mod.common.minecraft.entity.PowerArmorPartEntity;
import com.jetug.power_armor_mod.common.minecraft.registery.ModBlocks;
import com.jetug.power_armor_mod.common.minecraft.registery.ModItems;
import com.jetug.power_armor_mod.common.network.PacketHandler;
import com.jetug.power_armor_mod.common.minecraft.registery.ModEntityTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib3.GeckoLib;

import static com.jetug.power_armor_mod.common.capability.data.DataManager.getPlayerData;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(PowerArmorMod.MOD_ID)
public class PowerArmorMod
{
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "power_armor_mod";

    public PowerArmorMod() {
        GeckoLib.initialize();
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::setup);
        ModItems.register(eventBus);
        ModBlocks.register(eventBus);
        ModEntityTypes.register(eventBus);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        PlayerDataProvider.register();
        ArmorDataProvider.register();
        PacketHandler.register();
        ResourceHelper.register();
    }
}
