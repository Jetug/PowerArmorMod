package com.jetug.power_armor_mod.common.events;

import com.jetug.power_armor_mod.common.capability.armordata.ArmorDataProvider;
import com.jetug.power_armor_mod.common.minecraft.entity.PowerArmorEntity;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.jetug.power_armor_mod.common.capability.constants.Capabilities.ARMOR_DATA_RESOURCE;

@Mod.EventBusSubscriber
public class CapabilityEvents {
    @SubscribeEvent
    public static void attachCapabilitiesEntity(final AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (entity instanceof PowerArmorEntity)
            event.addCapability(ARMOR_DATA_RESOURCE, new ArmorDataProvider(entity));
    }
}
