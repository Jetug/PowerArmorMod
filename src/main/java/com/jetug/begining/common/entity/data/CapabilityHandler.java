package com.jetug.begining.common.entity.data;

import com.jetug.begining.common.entity.entity_type.PowerArmorPartEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.jetug.begining.ExampleMod.MOD_ID;
import static com.jetug.begining.common.util.constants.Resources.*;

//@Mod.EventBusSubscriber()
//public class CapabilityHandler {
//    @SubscribeEvent
//    public void attachCapability(AttachCapabilitiesEvent<Entity> event)
//    {
//        Entity entity = event.getObject();
//
//        if (entity instanceof PlayerEntity) {
//            event.addCapability(PLAYER_DATA_LOCATION, new PlayerDataProvider());
//        }
//        else if(entity instanceof PowerArmorPartEntity){
//            event.addCapability(POWER_ARMOR_PART_DATA_LOCATION, new PowerArmorDataProvider());
//        }
//    }
//}
