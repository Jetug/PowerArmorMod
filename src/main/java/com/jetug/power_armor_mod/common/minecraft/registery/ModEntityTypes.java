package com.jetug.power_armor_mod.common.minecraft.registery;

import com.jetug.power_armor_mod.PowerArmorMod;
import com.jetug.power_armor_mod.common.minecraft.entity.GeckoEntity;
import com.jetug.power_armor_mod.common.minecraft.entity.HitboxTestEntity;
import com.jetug.power_armor_mod.common.minecraft.entity.PowerArmorEntity;
import com.jetug.power_armor_mod.common.minecraft.entity.TestEntity;
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
                    .sized(1.7f, 2.5f)
                    .build(new ResourceLocation(PowerArmorMod.MOD_ID, "power_armor").toString()));

    public static final RegistryObject<EntityType<HitboxTestEntity>> HITBOX_TEST_ENTITY =
            ENTITY_TYPES.register("hitbox_test", () -> EntityType.Builder.of(HitboxTestEntity::new, EntityClassification.CREATURE)
                    .sized(1.7f, 2.5f)
                    .build(new ResourceLocation(PowerArmorMod.MOD_ID, "hitbox_test").toString()));

    public static final RegistryObject<EntityType<TestEntity>> TEST_ENTITY =
            ENTITY_TYPES.register("test_model", () -> EntityType.Builder.of(TestEntity::new, EntityClassification.CREATURE)
                    .sized(1f, 3f)
                    .build(new ResourceLocation(PowerArmorMod.MOD_ID, "test_model").toString()));

    public static final RegistryObject<EntityType<GeckoEntity>> GECKO_ENTITY =
            ENTITY_TYPES.register("gecko_model", () -> EntityType.Builder.of(GeckoEntity::new, EntityClassification.CREATURE)
                    .sized(1f, 3f)
                    .build(new ResourceLocation(PowerArmorMod.MOD_ID, "gecko_model").toString()));

    public static void register(IEventBus eventBus){
        ENTITY_TYPES.register(eventBus);
    }
}