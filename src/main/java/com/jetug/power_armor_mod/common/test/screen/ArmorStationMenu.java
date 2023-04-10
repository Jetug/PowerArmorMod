package com.jetug.power_armor_mod.common.test.screen;

import com.jetug.power_armor_mod.common.data.enums.BodyPart;
import com.jetug.power_armor_mod.common.foundation.item.EquipmentBase;
import com.jetug.power_armor_mod.common.foundation.registery.BlockRegistry;
import com.jetug.power_armor_mod.common.foundation.block.entity.ArmorStationBlockEntity;
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
    public  static final int TE_INVENTORY_SLOT_COUNT = 5;

    private final ArmorStationBlockEntity blockEntity;
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
            createSlot(handler, BODY     , FRAME_BODY_SLOT_POS     );
            createSlot(handler, LEFT_ARM , FRAME_LEFT_ARM_SLOT_POS );
            createSlot(handler, RIGHT_ARM, FRAME_RIGHT_ARM_SLOT_POS);
            createSlot(handler, LEFT_LEG , FRAME_LEFT_LEG_SLOT_POS );
            createSlot(handler, RIGHT_LEG, FRAME_RIGHT_LEG_SLOT_POS);
        });
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
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
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 86 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 144));
        }
    }
}
