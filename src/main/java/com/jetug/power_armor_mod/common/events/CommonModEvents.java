package com.jetug.power_armor_mod.common.events;

import com.jetug.power_armor_mod.common.foundation.entity.ArmorChassisEntity;
import com.jetug.power_armor_mod.common.foundation.registery.EntityTypeRegistry;
import com.jetug.power_armor_mod.common.network.PacketHandler;
import com.jetug.power_armor_mod.common.data.constants.Global;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import static com.jetug.power_armor_mod.common.util.extensions.PlayerExtension.isWearingChassis;

@Mod.EventBusSubscriber(modid = Global.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonModEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(EntityTypeRegistry.ARMOR_CHASSIS.get(), ArmorChassisEntity.createAttributes().build());
        event.put(EntityTypeRegistry.POWER_ARMOR_FRAME.get(), ArmorChassisEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void setup(final FMLCommonSetupEvent event) {
        PacketHandler.register();
    }

//    @SubscribeEvent
//    public static void registerModifierSerializers(@Nonnull final RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
//        event.getRegistry().register(new CastModifier.Serializer()
//                        .setRegistryName(new ResourceLocation(Global.MOD_ID,"cast_in_dungeon")));
//    }
}
