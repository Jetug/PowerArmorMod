package com.jetug.begining.common.entity.data;

import com.jetug.begining.ExampleMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.jetug.begining.ExampleMod.MOD_ID;

@Mod.EventBusSubscriber
public class CapabilityHandler {
    public static final ResourceLocation PLAYER_DATA_LOCATION = new ResourceLocation(MOD_ID, "player_data");

    @SubscribeEvent
    public void attachCapability(AttachCapabilitiesEvent<Entity> event)
    {
        if (!(event.getObject() instanceof PlayerEntity))
            return;
        event.addCapability(PLAYER_DATA_LOCATION, new PlayerDataProvider());
    }
}
