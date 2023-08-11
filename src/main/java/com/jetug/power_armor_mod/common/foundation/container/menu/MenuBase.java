package com.jetug.power_armor_mod.common.foundation.container.menu;

import com.jetug.power_armor_mod.common.data.enums.BodyPart;
import com.jetug.power_armor_mod.common.foundation.container.slot.EquipmentSlot;
import com.jetug.power_armor_mod.common.util.Pos2D;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;

import static com.jetug.power_armor_mod.common.data.constants.Gui.*;
import static java.lang.System.*;

public class MenuBase extends AbstractContainerMenu {
    private static final int SLOT_SIZE = 18;
    private static final int INVENTORY_POS_X = 8;
    private static final int HOTBAR_POS_X = INVENTORY_POS_X;
    private static final int INVENTORY_ROW_SIZE = 9;

    private final HashMap<BodyPart, Integer> slotsMap = new HashMap<>();
    private Integer slotId = 0;

    protected final Entity entity;
    protected final int inventoryPosY;
    protected final int hotbarPosY;
    protected final Container container;
    protected int size;

    public MenuBase(MenuType<?> pMenuType, int containerId, Container container, Inventory playerInventory,
                    Entity entity, int size, int inventoryPosY) {
        super(pMenuType, containerId);
        this.entity = entity;
        this.container = container;
        this.container.startOpen(playerInventory.player);
        this.inventoryPosY = inventoryPosY;
        this.hotbarPosY = inventoryPosY + 58;
        this.size = size;
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return this.container.stillValid(playerIn) && this.entity.isAlive() && this.entity.distanceTo(playerIn) < 8.0F;
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        try{
            var sourceSlot = slots.get(index);
            if (!sourceSlot.hasItem()) return ItemStack.EMPTY;
            var sourceStack = sourceSlot.getItem();
            var copyOfSourceStack = sourceStack.copy();

            if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
                if (!moveItemStackTo(sourceStack, INVENTORY_FIRST_SLOT_INDEX,
                        INVENTORY_FIRST_SLOT_INDEX + size, false))
                    return ItemStack.EMPTY;
            } else if (index < INVENTORY_FIRST_SLOT_INDEX + size) {
                if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false))
                    return ItemStack.EMPTY;
            } else {
                out.println("Invalid slotIndex:" + index);
                return ItemStack.EMPTY;
            }

            if (sourceStack.getCount() == 0) sourceSlot.set(ItemStack.EMPTY);
            else sourceSlot.setChanged();
            sourceSlot.onTake(playerIn, sourceStack);
            return copyOfSourceStack;
        }
        catch (Exception ignored){
            return ItemStack.EMPTY;
        }

    }

    @Override
    public void removed(Player playerIn) {
        super.removed(playerIn);
        container.stopOpen(playerIn);
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int row = 0; row < 3; ++row) {
            for (int slot = 0; slot < INVENTORY_ROW_SIZE; ++slot) {
                this.addSlot(new Slot(playerInventory,
                        slot + row * INVENTORY_ROW_SIZE + INVENTORY_ROW_SIZE,
                        INVENTORY_POS_X + slot * SLOT_SIZE,
                        inventoryPosY + row * SLOT_SIZE));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int slot = 0; slot < 9; ++slot) {
            this.addSlot(new Slot(playerInventory, slot, HOTBAR_POS_X + slot * SLOT_SIZE, hotbarPosY));
        }
    }

    protected void createSlot(BodyPart bodyPart, Pos2D pos){
        slotsMap.put(bodyPart, slotId);
        this.addSlot(new EquipmentSlot(bodyPart, container, bodyPart.getId(), pos.x, pos.y));
        slotId++;
    }
}