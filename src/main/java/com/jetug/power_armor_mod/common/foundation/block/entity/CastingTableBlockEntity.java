package com.jetug.power_armor_mod.common.foundation.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CastingTableBlockEntity extends AbstractFurnaceBlockEntity {
    public CastingTableBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityType.FURNACE, pPos, pBlockState, RecipeType.SMELTING);
    }

    protected Component getDefaultName() {
        return new TranslatableComponent("container.casting_table");
    }

    protected AbstractContainerMenu createMenu(int pId, Inventory pPlayer) {
        return new FurnaceMenu(pId, pPlayer, this, this.dataAccess);
    }
}