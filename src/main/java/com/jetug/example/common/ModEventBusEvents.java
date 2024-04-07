package com.jetug.example.common;

import com.jetug.chassis_core.ChassisCore;
import com.jetug.example.common.entities.ExampleChassis;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.jetug.example.common.registery.EntityTypes.EXAMPLE_CHASSIS;

@Mod.EventBusSubscriber(modid = ChassisCore.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(EXAMPLE_CHASSIS.get(), ExampleChassis.createAttributes().build());
    }
}