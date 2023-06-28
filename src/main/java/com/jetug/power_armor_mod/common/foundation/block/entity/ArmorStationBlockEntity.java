package com.jetug.power_armor_mod.common.foundation.block.entity;

import com.jetug.power_armor_mod.common.foundation.entity.PowerArmorEntity;
import com.jetug.power_armor_mod.common.foundation.registery.BlockEntitieRegistry;
import com.jetug.power_armor_mod.common.foundation.screen.menu.*;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.*;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.*;

import static com.jetug.power_armor_mod.common.foundation.screen.menu.ArmorStationMenu.*;

public class ArmorStationBlockEntity extends BlockEntityBase implements MenuProvider {
    public ArmorStationBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(BlockEntitieRegistry.ARMOR_STATION_BLOCK_ENTITY.get(), pWorldPosition, pBlockState, TE_INVENTORY_SLOT_COUNT);
    }

    public PowerArmorEntity frame;

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("container.armor_station");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory, Player pPlayer) {
        return new ArmorStationMenu(pContainerId, pInventory, this);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ArmorStationBlockEntity blockEntity) {
//        if(hasRecipe(blockEntity) && hasNotReachedStackLimit(blockEntity)) craftItem(blockEntity);
        var aabb = new AABB(pos).inflate(1);
        var entities = level.getEntitiesOfClass(PowerArmorEntity.class, aabb);
        blockEntity.frame = entities.isEmpty()? null : entities.get(0);
    }

//    private static void craftItem(ArmorStationBlockEntity entity) {
//        entity.itemHandler.extractItem(0, 1, false);
//        entity.itemHandler.extractItem(1, 1, false);
//        entity.itemHandler.getStackInSlot(2).hurt(1, new Random(), null);
//
//        entity.itemHandler.setStackInSlot(3, new ItemStack(ItemRegistry.CITRINE.get(),
//                entity.itemHandler.getStackInSlot(3).getCount() + 1));
//    }
//
//    private static boolean hasRecipe(ArmorStationBlockEntity entity) {
//        var hasItemInWaterSlot = PotionUtils.getPotion(entity.itemHandler.getStackInSlot(0)) == Potions.WATER;
//        var hasItemInFirstSlot = entity.itemHandler.getStackInSlot(1).getItem() == ItemRegistry.RAW_CITRINE.get();
//        var hasItemInSecondSlot = entity.itemHandler.getStackInSlot(2).getItem() == ItemRegistry.GEM_CUTTER_TOOL.get();
//
//        return hasItemInWaterSlot && hasItemInFirstSlot && hasItemInSecondSlot;
//    }

    private static boolean hasNotReachedStackLimit(ArmorStationBlockEntity entity) {
        return entity.itemHandler.getStackInSlot(3).getCount() < entity.itemHandler.getStackInSlot(3).getMaxStackSize();
    }
}
