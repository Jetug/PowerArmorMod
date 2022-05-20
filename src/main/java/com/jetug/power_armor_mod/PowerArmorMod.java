package com.jetug.power_armor_mod;

import com.jetug.power_armor_mod.common.entity.data.*;
import com.jetug.power_armor_mod.common.entity.entity_type.PowerArmorPartEntity;
import com.jetug.power_armor_mod.common.registery.ModEntityTypes;
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

// The value here should match an entry in the META-INF/mods.toml file
@Mod(PowerArmorMod.MOD_ID)
public class PowerArmorMod
{
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "power_armor_mod";

    public PowerArmorMod()
    {
        GeckoLib.initialize();
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::setup);
//        ModItems.register(eventBus);
//        ModBlocks.register(eventBus);
        ModEntityTypes.register(eventBus);

        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPlayerLogsIn(PlayerEvent.ItemPickupEvent event) {
        {
            PlayerEntity player = event.getPlayer();

            IPlayerData dataPA = player.getCapability(PlayerDataProvider.PLAYER_DATA, null).orElse(null);
            if(dataPA != null)
                player.sendMessage(new StringTextComponent("not null : " + dataPA.getIsInPowerArmor()), player.getUUID());
            else
                player.sendMessage(new StringTextComponent("PA null"), player.getUUID());

        }}

    @SubscribeEvent
    public void attachCapability(AttachCapabilitiesEvent<Entity> event)
    {
        Entity entity = event.getObject();

        if (entity instanceof PlayerEntity) {
            PlayerDataProvider.attach(event);
        }
        else if(entity instanceof PowerArmorPartEntity){
            PowerArmorDataProvider.attach(event);
        }
    }

    private void setup(final FMLCommonSetupEvent event) {
        PlayerDataProvider.register();
        PowerArmorDataProvider.register();
    }
}
