package com.jetug.power_armor_mod.common.events;

import com.jetug.power_armor_mod.PowerArmorMod;
import com.jetug.power_armor_mod.common.minecraft.entity.GeckoEntity;
import com.jetug.power_armor_mod.common.minecraft.entity.HitboxTestEntity;
import com.jetug.power_armor_mod.common.minecraft.entity.PowerArmorEntity;
import com.jetug.power_armor_mod.common.minecraft.entity.TestEntity;
import com.jetug.power_armor_mod.common.minecraft.registery.ModEntityTypes;
import com.jetug.power_armor_mod.common.util.helpers.timer.Timer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PowerArmorMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonModEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntityTypes.HITBOX_TEST_ENTITY.get(), HitboxTestEntity.createAttributes().build());
        event.put(ModEntityTypes.POWER_ARMOR.get(), PowerArmorEntity.createAttributes().build());
        event.put(ModEntityTypes.TEST_ENTITY.get(), TestEntity.createAttributes().build());
        event.put(ModEntityTypes.GECKO_ENTITY.get(), GeckoEntity.createAttributes().build());
    }
}
