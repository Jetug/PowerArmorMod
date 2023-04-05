package com.jetug.power_armor_mod.common.events;

import com.jetug.power_armor_mod.common.foundation.entity.PowerArmorEntity;
import com.jetug.power_armor_mod.common.foundation.registery.EntityTypeRegistry;
import com.jetug.power_armor_mod.common.network.PacketHandler;
import com.jetug.power_armor_mod.common.data.constants.Global;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = Global.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonModEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(EntityTypeRegistry.POWER_ARMOR.get(), PowerArmorEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void setup(final FMLCommonSetupEvent event) {
        PacketHandler.register();
    }
}
