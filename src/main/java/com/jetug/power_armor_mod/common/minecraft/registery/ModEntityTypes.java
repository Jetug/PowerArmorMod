package com.jetug.power_armor_mod.common.minecraft.registery;

import com.jetug.power_armor_mod.common.minecraft.entity.PowerArmorEntity;
import com.jetug.power_armor_mod.common.util.constants.Global;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, Global.MOD_ID);

    public static final RegistryObject<EntityType<PowerArmorEntity>> POWER_ARMOR =
            ENTITY_TYPES.register("power_armor", () -> EntityType.Builder
                    .of(PowerArmorEntity::new, MobCategory.CREATURE)
                    .sized(1.3f, 2.5f)
                    .build(new ResourceLocation(Global.MOD_ID, "power_armor").toString()));


    public static void register(IEventBus eventBus){
        ENTITY_TYPES.register(eventBus);
    }
}