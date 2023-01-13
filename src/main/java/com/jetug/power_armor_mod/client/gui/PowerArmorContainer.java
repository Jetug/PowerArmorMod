package com.jetug.power_armor_mod.client.gui;

import com.jetug.power_armor_mod.common.minecraft.entity.PowerArmorEntity;
import com.jetug.power_armor_mod.common.minecraft.item.PowerArmorItem;
import com.jetug.power_armor_mod.common.minecraft.registery.ContainerRegistry;
import com.jetug.power_armor_mod.common.util.enums.BodyPart;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class PowerArmorContainer extends AbstractContainerMenu {
    // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
    // must assign a slot number to each of the slots used by the GUI.
    // For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
    // Each time we add a Slot to the container, it automatically increases the slotIndex, which means
    //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    //  36 - 44 = TileInventory slots, which map to our TileEntity slot numbers 0 - 8)
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
    private static final int SIZE = 6;
    private static final int SLOT_SIZE = 18;
    private static final int INVENTORY_POS_X = 8;
    private static final int INVENTORY_POS_Y = 83;
    private static final int HOTBAR_POS_Y = 141;
    public static final int INVENTORY_ROW_SIZE = 9;

    private final Container armorInventory;
    private final PowerArmorEntity powerArmor;

    public PowerArmorContainer(int i, Inventory playerInventory) {
        this(i, new SimpleContainer(SIZE), playerInventory, null);
    }

    private Slot createSlot(Container container, int id, int x, int y, BodyPart bodyPart){
        return new Slot(container, id, x, y) {
            @Override
            public void setChanged() {
                this.container.setChanged();
            }

            @Override
            public boolean mayPlace(ItemStack stack) {
                return super.mayPlace(stack)
                        && stack.getItem() instanceof PowerArmorItem item
                        && item.part == bodyPart;
            }
        };
    }

    public PowerArmorContainer(int containerId, Container container, Inventory playerInventory, PowerArmorEntity entity) {
        super(ContainerRegistry.DRAGON_CONTAINER.get(), containerId);

        armorInventory = container;
        powerArmor = entity;
        armorInventory.startOpen(playerInventory.player);

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);

        this.addSlot(createSlot(container, 0, 117, 9, BodyPart.HEAD));
        this.addSlot(createSlot(container, 1, 117, 27, BodyPart.BODY));
        this.addSlot(createSlot(container, 2, 99 , 27, BodyPart.RIGHT_ARM));
        this.addSlot(createSlot(container, 3, 135, 27, BodyPart.LEFT_ARM));
        this.addSlot(createSlot(container, 4, 108, 44, BodyPart.RIGHT_LEG));
        this.addSlot(createSlot(container, 4, 126, 44, BodyPart.LEFT_LEG));
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return this.armorInventory.stillValid(playerIn) && this.powerArmor.isAlive() && this.powerArmor.distanceTo(playerIn) < 8.0F;
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        Slot sourceSlot = slots.get(index);
        if (!sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + SIZE, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + SIZE) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + index);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public void removed(Player playerIn) {
        super.removed(playerIn);
        armorInventory.stopOpen(playerIn);
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int row = 0; row < 3; ++row) {
            for (int slot = 0; slot < INVENTORY_ROW_SIZE; ++slot) {
                this.addSlot(new Slot(playerInventory,
                        slot + row * INVENTORY_ROW_SIZE + INVENTORY_ROW_SIZE,
                        INVENTORY_POS_X + slot * SLOT_SIZE,
                        INVENTORY_POS_Y + row * SLOT_SIZE));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int slot = 0; slot < 9; ++slot) {
            this.addSlot(new Slot(playerInventory, slot, INVENTORY_POS_X + slot * SLOT_SIZE, HOTBAR_POS_Y));
        }
    }
}