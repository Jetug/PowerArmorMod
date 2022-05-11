package com.jetug.begining;

import com.jetug.begining.client.Messages;
import com.jetug.begining.common.entity.data.*;
import com.jetug.begining.common.entity.entity_type.PowerArmorPartEntity;
import com.jetug.begining.common.events.EventHandler;
import com.jetug.begining.common.registery.ModEntityTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import software.bernie.geckolib3.GeckoLib;

import static com.jetug.begining.common.util.constants.Resources.PLAYER_DATA_LOCATION;
import static com.jetug.begining.common.util.constants.Resources.POWER_ARMOR_PART_DATA_LOCATION;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ExampleMod.MOD_ID)
public class ExampleMod
{
    public static final String MOD_ID = "begining";

    public ExampleMod()
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
