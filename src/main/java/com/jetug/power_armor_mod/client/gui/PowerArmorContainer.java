package com.jetug.power_armor_mod.client.gui;

import com.jetug.power_armor_mod.common.foundation.entity.PowerArmorEntity;
import com.jetug.power_armor_mod.common.foundation.item.EquipmentBase;
import com.jetug.power_armor_mod.common.foundation.registery.ContainerRegistry;
import com.jetug.power_armor_mod.common.util.Pos2D;
import com.jetug.power_armor_mod.common.data.enums.BodyPart;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import static com.jetug.power_armor_mod.common.data.constants.Gui.*;
import static com.jetug.power_armor_mod.common.data.constants.Resources.*;
import static com.jetug.power_armor_mod.common.data.enums.BodyPart.*;

public class PowerArmorContainer extends AbstractContainerMenu {
    public static final int SIZE = 7;

    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
    private static final int SLOT_SIZE = 18;
    private static final int INVENTORY_POS_X = 8;
    private static final int INVENTORY_POS_Y = 84;
    private static final int HOTBAR_POS_Y = 142;
    private static final int HOTBAR_POS_X = INVENTORY_POS_X;
    private static final int INVENTORY_ROW_SIZE = 9;


    static final ResourceLocation[] TEXTURE_EMPTY_SLOTS = new ResourceLocation[]
            {
                    EMPTY_ARMOR_SLOT_HEAD,
                    EMPTY_ARMOR_SLOT_BODY,
                    EMPTY_ARMOR_SLOT_LEFT_ARM,
                    EMPTY_ARMOR_SLOT_RIGHT_ARM,
                    EMPTY_ARMOR_SLOT_LEFT_LEG,
                    EMPTY_ARMOR_SLOT_RIGHT_LEG
            };


    private final PowerArmorEntity powerArmor;
    private final Container armorInventory;

    public PowerArmorContainer(int i, Inventory playerInventory) {
        this(i, new SimpleContainer(SIZE), playerInventory, null);
    }


    public PowerArmorContainer(int containerId, Container container, Inventory playerInventory, PowerArmorEntity entity) {
        super(ContainerRegistry.ARMOR_CONTAINER.get(), containerId);
        this.powerArmor = entity;
        this.armorInventory = container;
        this.armorInventory.startOpen(playerInventory.player);

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);

        createSlot(HEAD     , HEAD_SLOT_POS     );
        createSlot(BODY     , BODY_SLOT_POS     );
        createSlot(LEFT_ARM , RIGHT_ARM_SLOT_POS);
        createSlot(RIGHT_ARM, LEFT_ARM_SLOT_POS );
        createSlot(LEFT_LEG , RIGHT_LEG_SLOT_POS);
        createSlot(RIGHT_LEG, LEFT_LEG_SLOT_POS );
        createSlot(ENGINE   ,  ENGINE_SLOT_POS  );
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return this.armorInventory.stillValid(playerIn) && this.powerArmor.isAlive() && this.powerArmor.distanceTo(playerIn) < 8.0F;
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        Slot sourceSlot = slots.get(index);
        if (!sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX,
                    TE_INVENTORY_FIRST_SLOT_INDEX + SIZE, false)) {
                return ItemStack.EMPTY;
            }
        } else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + SIZE) {
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + index);
            return ItemStack.EMPTY;
        }
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
            this.addSlot(new Slot(playerInventory, slot, HOTBAR_POS_X + slot * SLOT_SIZE, HOTBAR_POS_Y));
        }
    }

    private void createSlot(BodyPart bodyPart, Pos2D pos){
        this.addSlot(new Slot(armorInventory, bodyPart.getId(), pos.x, pos.y) {
            @Override
            public void setChanged() {
                container.setChanged();
            }
            @Override
            public boolean mayPlace(ItemStack stack) {
                return super.mayPlace(stack)
                        && stack.getItem() instanceof EquipmentBase item
                        && item.part == bodyPart;
            }

//            @Override
//            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
//                return Pair.of(InventoryMenu.BLOCK_ATLAS, EMPTY_ARMOR_SLOT_HEAD);
//            }
        });
    }
}