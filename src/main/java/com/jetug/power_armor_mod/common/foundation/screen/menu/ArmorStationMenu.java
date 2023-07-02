package com.jetug.power_armor_mod.common.foundation.screen.menu;

import com.jetug.power_armor_mod.common.data.enums.*;
import com.jetug.power_armor_mod.common.foundation.item.EquipmentBase;
import com.jetug.power_armor_mod.common.foundation.registery.BlockRegistry;
import com.jetug.power_armor_mod.common.foundation.block.entity.ArmorStationBlockEntity;
import com.jetug.power_armor_mod.common.foundation.registery.ModMenuTypes;
import com.jetug.power_armor_mod.common.util.Pos2D;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import static com.jetug.power_armor_mod.common.data.constants.Gui.*;
import static com.jetug.power_armor_mod.common.data.enums.BodyPart.*;

public class ArmorStationMenu extends AbstractContainerMenu {
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
    private static final int SLOT_SIZE = 18;
    private static final int INVENTORY_POS_X = 8;
    private static final int INVENTORY_POS_Y = 105;
    private static final int HOTBAR_POS_Y = 163;
    private static final int HOTBAR_POS_X = INVENTORY_POS_X;
    private static final int INVENTORY_ROW_SIZE = 9;
    public  static final int TE_INVENTORY_SLOT_COUNT = 5;

    public final ArmorStationBlockEntity blockEntity;
    private final Level level;
    private int i = 0;

    public ArmorStationMenu(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {
        this(pContainerId, inv, inv.player.level.getBlockEntity(extraData.readBlockPos()));
    }

    public ArmorStationMenu(int pContainerId, Inventory inv, BlockEntity entity) {
        super(ModMenuTypes.GEM_CUTTING_STATION_MENU.get(), pContainerId);
        checkContainerSize(inv, TE_INVENTORY_SLOT_COUNT);
        blockEntity = ((ArmorStationBlockEntity) entity);
        this.level = inv.player.level;

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        this.blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
            createSlot(handler, BODY_ARMOR, FRAME_BODY_SLOT_POS     );
            createSlot(handler, LEFT_ARM_ARMOR, FRAME_LEFT_ARM_SLOT_POS );
            createSlot(handler, RIGHT_ARM_ARMOR, FRAME_RIGHT_ARM_SLOT_POS);
            createSlot(handler, LEFT_LEG_ARMOR, FRAME_LEFT_LEG_SLOT_POS );
            createSlot(handler, RIGHT_LEG_ARMOR, FRAME_RIGHT_LEG_SLOT_POS);
        });
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        var sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;
        var sourceStack = sourceSlot.getItem();
        var copyOfSourceStack = sourceStack.copy();

        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX,
                    TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX,
                    VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
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
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                pPlayer, BlockRegistry.ARMOR_STATION.get());
    }

    private void createSlot(IItemHandler handler, BodyPart bodyPart, Pos2D pos){
        this.addSlot(new SlotItemHandler( handler, i++, pos.x, pos.y){
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return super.mayPlace(stack)
                        && stack.getItem() instanceof EquipmentBase item
                        && item.part == bodyPart;
            }
        } );
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
}
