package com.jetug.power_armor_mod.common.foundation.registery;

import com.jetug.power_armor_mod.common.foundation.entity.PowerArmorEntity;
import com.jetug.power_armor_mod.common.foundation.entity.PowerArmorPartEntity;
import com.jetug.power_armor_mod.common.data.constants.Global;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityTypeRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, Global.MOD_ID);

    public static final RegistryObject<EntityType<PowerArmorEntity>> POWER_ARMOR =
            registerEntity("power_armor", EntityType.Builder
                    .of(PowerArmorEntity::new, MobCategory.MISC)
                    .sized(1.0f, 2.3f));

    public static final RegistryObject<EntityType<PowerArmorPartEntity>> POWER_ARMOR_PART =
            registerEntity("power_armor_part", EntityType.Builder.of(PowerArmorPartEntity::new, MobCategory.MISC));


    private static <T extends Entity> RegistryObject<EntityType<T>> registerEntity(String entityName, EntityType.Builder<T> builder) {
        return ENTITY_TYPES.register(entityName, () -> builder.build(new ResourceLocation(Global.MOD_ID, entityName).toString()));
    }

    public static void register(IEventBus eventBus){
        ENTITY_TYPES.register(eventBus);
    }
}