package com.jetug.begining.common.events;

import com.jetug.begining.ExampleMod;
import com.jetug.begining.common.entity.data.PlayerDataProvider;
import com.jetug.begining.common.entity.data.PowerArmorDataProvider;
import com.jetug.begining.common.entity.entity_type.GeckoEntity;
import com.jetug.begining.common.entity.entity_type.PowerArmorEntity;
import com.jetug.begining.common.entity.entity_type.PowerArmorPartEntity;
import com.jetug.begining.common.entity.entity_type.TestEntity;
import com.jetug.begining.common.registery.ModEntityTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.jetug.begining.common.util.constants.Resources.PLAYER_DATA_LOCATION;
import static com.jetug.begining.common.util.constants.Resources.POWER_ARMOR_PART_DATA_LOCATION;

@Mod.EventBusSubscriber(modid = ExampleMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonModEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event){
        event.put(ModEntityTypes.POWER_ARMOR.get(), PowerArmorEntity.createAttributes().build());
        event.put(ModEntityTypes.TEST_ENTITY.get(), TestEntity.createAttributes().build());
        event.put(ModEntityTypes.GECKO_ENTITY.get(), GeckoEntity.createAttributes().build());
        //event.put(ModEntityTypes.PLAYER_POWER_ARMOR.get(), PlayerPowerArmorEntity.createAttributes().build());
    }
}
