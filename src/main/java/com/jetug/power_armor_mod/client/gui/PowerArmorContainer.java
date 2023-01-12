package com.jetug.power_armor_mod.client.gui;

import com.jetug.power_armor_mod.common.minecraft.entity.PowerArmorEntity;
import com.jetug.power_armor_mod.common.minecraft.item.PowerArmorItem;
import com.jetug.power_armor_mod.common.util.enums.BodyPart;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;

public class PowerArmorContainer extends AbstractContainerMenu {
    private final Container dragonInventory;
    private final PowerArmorEntity powerArmor;

    public PowerArmorContainer(int i, Inventory playerInventory) {
        this(i, new SimpleContainer(5), playerInventory, null);
    }

    private Slot createSlot(Container container, int id, int x, int y, BodyPart bodyPart){
        return new Slot(container, id, x, y) {
            @Override
            public void setChanged() {
                this.container.setChanged();
            }

            @Override
            public boolean mayPlace(ItemStack stack) {
                return super.mayPlace(stack) && stack.getItem()
                        instanceof PowerArmorItem item && item.part == bodyPart;
            }
        };
    }

    public PowerArmorContainer(int id, Container container, Inventory playerInventory, PowerArmorEntity entity) {
        super(ContainerRegistry.DRAGON_CONTAINER.get(), id);

        this.dragonInventory = container;
        this.powerArmor = entity;
        byte b0 = 3;
        dragonInventory.startOpen(playerInventory.player);
        int i = (b0 - 4) * 18;

        this.addSlot(createSlot(container, 0, 117, 9, BodyPart.HEAD));
        this.addSlot(createSlot(container, 1, 8, 18, BodyPart.BODY));
        this.addSlot(createSlot(container, 2, 8, 36, BodyPart.RIGHT_ARM));
        this.addSlot(createSlot(container, 3, 153, 18, BodyPart.LEFT_ARM));
        this.addSlot(createSlot(container, 4, 153, 36, BodyPart.RIGHT_LEG));

        int j;
        int k;

        for (j = 0; j < 3; ++j) {
            for (k = 0; k < 9; ++k) {
                this.addSlot(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, 150 + j * 18 + i));
            }
        }

        for (j = 0; j < 9; ++j) {
            this.addSlot(new Slot(playerInventory, j, 8 + j * 18, 208 + i));
        }
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return this.dragonInventory.stillValid(playerIn) && this.powerArmor.isAlive() && this.powerArmor.distanceTo(playerIn) < 8.0F;
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < this.dragonInventory.getContainerSize()) {
                if (!this.moveItemStackTo(itemstack1, this.dragonInventory.getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.getSlot(1).mayPlace(itemstack1) && !this.getSlot(1).hasItem()) {
                if (!this.moveItemStackTo(itemstack1, 1, 2, false)) {
                    return ItemStack.EMPTY;
                }

            } else if (this.getSlot(2).mayPlace(itemstack1) && !this.getSlot(2).hasItem()) {
                if (!this.moveItemStackTo(itemstack1, 2, 3, false)) {
                    return ItemStack.EMPTY;
                }

            } else if (this.getSlot(3).mayPlace(itemstack1) && !this.getSlot(3).hasItem()) {
                if (!this.moveItemStackTo(itemstack1, 3, 4, false)) {
                    return ItemStack.EMPTY;
                }

            } else if (this.getSlot(4).mayPlace(itemstack1) && !this.getSlot(4).hasItem()) {
                if (!this.moveItemStackTo(itemstack1, 4, 5, false)) {
                    return ItemStack.EMPTY;
                }

            } else if (this.getSlot(0).mayPlace(itemstack1)) {
                if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.dragonInventory.getContainerSize() <= 5 || !this.moveItemStackTo(itemstack1, 5, this.dragonInventory.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }
            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return itemstack;
    }

    @Override
    public void removed(Player playerIn) {
        super.removed(playerIn);
        this.dragonInventory.stopOpen(playerIn);
    }

}