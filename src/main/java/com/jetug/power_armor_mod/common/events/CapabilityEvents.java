package com.jetug.power_armor_mod.common.events;

import com.jetug.power_armor_mod.common.capability.data.ArmorDataProvider;
import com.jetug.power_armor_mod.common.capability.data.PlayerDataProvider;
import com.jetug.power_armor_mod.common.minecraft.entity.PowerArmorEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class CapabilityEvents {
    @SubscribeEvent
    public static void attachCapabilitiesEntity(final AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (entity instanceof PlayerEntity)
            PlayerDataProvider.attach(event);
        else if (entity instanceof PowerArmorEntity)
            ArmorDataProvider.attach(event);
    }
}
