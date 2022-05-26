package com.jetug.power_armor_mod.common.events;

import com.jetug.power_armor_mod.PowerArmorMod;
import com.jetug.power_armor_mod.common.entity.entitytype.GeckoEntity;
import com.jetug.power_armor_mod.common.entity.entitytype.PowerArmorEntity;
import com.jetug.power_armor_mod.common.entity.entitytype.TestEntity;
import com.jetug.power_armor_mod.common.registery.ModEntityTypes;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PowerArmorMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonModEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event){
        event.put(ModEntityTypes.POWER_ARMOR.get(), PowerArmorEntity.createAttributes().build());
        event.put(ModEntityTypes.TEST_ENTITY.get(), TestEntity.createAttributes().build());
        event.put(ModEntityTypes.GECKO_ENTITY.get(), GeckoEntity.createAttributes().build());
        //event.put(ModEntityTypes.PLAYER_POWER_ARMOR.get(), PlayerPowerArmorEntity.createAttributes().build());
    }
}
