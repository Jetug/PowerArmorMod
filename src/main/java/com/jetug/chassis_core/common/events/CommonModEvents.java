package com.jetug.chassis_core.common.events;

import com.jetug.chassis_core.ChassisCore;
import com.jetug.chassis_core.common.network.PacketHandler;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import static com.jetug.chassis_core.common.util.helpers.PlayerUtils.isWearingChassis;

@Mod.EventBusSubscriber(modid = ChassisCore.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonModEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
//        event.put(EntityTypeRegistry.ARMOR_CHASSIS.get(), WearableChassis.createAttributes().build());
//        event.put(EntityTypeRegistry.POWER_ARMOR_FRAME.get(), WearableChassis.createAttributes().build());
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
