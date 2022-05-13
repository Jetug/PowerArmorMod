package com.jetug.power_armor_mod.common.registery;

import com.jetug.power_armor_mod.PowerArmorMod;
import com.jetug.power_armor_mod.common.entity.entity_type.*;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, PowerArmorMod.MOD_ID);

    public static final RegistryObject<EntityType<PowerArmorEntity>> POWER_ARMOR =
            ENTITY_TYPES.register("power_armor", () -> EntityType.Builder.of(PowerArmorEntity::new, EntityClassification.CREATURE)
                    .sized(1f, 2.5f)
                    .build(new ResourceLocation(PowerArmorMod.MOD_ID, "power_armor").toString()));

//    public static final RegistryObject<EntityType<PowerArmorPartEntity>> POWER_ARMOR_BODY_PART =
//            ENTITY_TYPES.register("power_armor_body_part", () -> EntityType.Builder.of(PowerArmorPartEntity::new, EntityClassification.CREATURE)
//                    .sized(1f, 3f)
//                    .build(new ResourceLocation(ExampleMod.MOD_ID, "power_armor").toString()));

    public static final RegistryObject<EntityType<TestEntity>> TEST_ENTITY =
            ENTITY_TYPES.register("test_model", () -> EntityType.Builder.of(TestEntity::new, EntityClassification.CREATURE)
                    .sized(1f, 3f)
                    .build(new ResourceLocation(PowerArmorMod.MOD_ID, "test_model").toString()));

    public static final RegistryObject<EntityType<GeckoEntity>> GECKO_ENTITY =
            ENTITY_TYPES.register("gecko_model", () -> EntityType.Builder.of(GeckoEntity::new, EntityClassification.CREATURE)
                    .sized(1f, 3f)
                    .build(new ResourceLocation(PowerArmorMod.MOD_ID, "gecko_model").toString()));

//    public static final RegistryObject<EntityType<PlayerPowerArmorEntity>> PLAYER_POWER_ARMOR =
//            ENTITY_TYPES.register("player_power_armor", () -> EntityType.Builder.of(PlayerPowerArmorEntity::new, EntityClassification.MISC)
//                    .sized(1f, 3f)
//                    .build(new ResourceLocation(ExampleMod.MOD_ID, "player_power_armor").toString()));

//    public static final RegistryObject<EntityType<PlayerExtension>> PLAYER =
//            ENTITY_TYPES.register("player", () -> EntityType.Builder.of(PlayerExtension::new, EntityClassification.MISC)
//                    .clientTrackingRange(32)
//                    .updateInterval(2))
//                    .sized(1f, 3f)
//                    .build(new ResourceLocation(ExampleMod.MOD_ID, "player").toString()));

    public static void register(IEventBus eventBus){
        ENTITY_TYPES.register(eventBus);
    }
}
