package com.jetug.power_armor_mod.common.foundation.registery;

import com.jetug.power_armor_mod.common.data.constants.Global;
import com.jetug.power_armor_mod.common.foundation.block.entity.ArmorStationBlockEntity;
import com.jetug.power_armor_mod.common.foundation.block.entity.CastingTableBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntitieRegistry {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Global.MOD_ID);

    public static final RegistryObject<BlockEntityType<ArmorStationBlockEntity>> ARMOR_STATION_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("armor_station_block_entity", () ->
                    BlockEntityType.Builder.of(ArmorStationBlockEntity::new,
                            BlockRegistry.ARMOR_STATION.get()).build(null));

    public static final RegistryObject<BlockEntityType<CastingTableBlockEntity>> CASTING_TABLE_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("casting_table_block_entity", () ->
                    BlockEntityType.Builder.of(CastingTableBlockEntity::new,
                            BlockRegistry.CASTING_TABLE.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
