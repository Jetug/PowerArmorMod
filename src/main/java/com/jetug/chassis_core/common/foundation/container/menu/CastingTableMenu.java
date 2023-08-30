package com.jetug.chassis_core.common.foundation.container.menu;

import com.jetug.chassis_core.client.ClientData;
import com.jetug.chassis_core.common.foundation.container.menu.base.BlockEntityMenu;
import com.jetug.chassis_core.common.foundation.item.CastItem;
import com.jetug.chassis_core.common.foundation.registery.ContainerRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import static com.jetug.chassis_core.common.foundation.registery.BlockRegistry.CASTING_TABLE;

public class CastingTableMenu extends BlockEntityMenu {
    private static final int INVENTORY_POS_Y = 84;
    public static final int SIZE = 4;


    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
    private static final int TE_INVENTORY_SLOT_COUNT = 4;

    //private final CastingTableEntity blockEntity;
    private final Level level;

    public ContainerData getData() {
        if(level.isClientSide){
            var clientData = ClientData.containerData.get(blockEntity.getBlockPos());
            return clientData == null ? data : clientData;
        }
        return data;
    }

    private final ContainerData data;

    public CastingTableMenu(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {
        this(pContainerId, inv, inv.player.level.getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(4));
    }

    public CastingTableMenu(int pContainerId, Inventory inv, BlockEntity entity, ContainerData pData) {
        super(ContainerRegistry.CASTING_TABLE_MENU.get(),
                pContainerId, new SimpleContainer(SIZE), inv, entity,
                SIZE, INVENTORY_POS_Y, CASTING_TABLE.get());

        checkContainerSize(inv, SIZE);
        this.level = inv.player.level;
        this.data = pData;

        this.blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
            var i = 0;
            this.addSlot(new SlotItemHandler(handler, i++, 56, 17));
            this.addSlot(new SlotItemHandler(handler, i++, 56, 53){
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) {
                    return isFuel(stack);
                }
            });
            this.addSlot(new SlotItemHandler(handler, i++, 116, 35){
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) {
                    return false;
                }
            });
            this.addSlot(new SlotItemHandler(handler, i++, 142, 35){
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) {
                    return stack.getItem() instanceof CastItem;
                }
            });
        });
    }

//    @Override
//    public ItemStack quickMoveStack(Player playerIn, int index) {
//        Slot sourceSlot = slots.get(index);
//        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
//        ItemStack sourceStack = sourceSlot.getItem();
//        ItemStack copyOfSourceStack = sourceStack.copy();
//
//        // Check if the slot clicked is one of the vanilla container slots
//        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
//            // This is a vanilla container slot so merge the stack into the tile inventory
//            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
//                    + TE_INVENTORY_SLOT_COUNT, false)) {
//                return ItemStack.EMPTY;  // EMPTY_ITEM
//            }
//        } else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
//            // This is a TE slot so merge the stack into the players inventory
//            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
//                return ItemStack.EMPTY;
//            }
//        } else {
//            System.out.println("Invalid slotIndex:" + index);
//            return ItemStack.EMPTY;
//        }
//        // If stack size == 0 (the entire stack was moved) set slot contents to null
//        if (sourceStack.getCount() == 0) {
//            sourceSlot.set(ItemStack.EMPTY);
//        } else {
//            sourceSlot.setChanged();
//        }
//        sourceSlot.onTake(playerIn, sourceStack);
//        return copyOfSourceStack;
//    }

    protected boolean isFuel(ItemStack pStack) {
        return net.minecraftforge.common.ForgeHooks.getBurnTime(pStack, RecipeType.BLASTING) > 0;
    }

    public int getBurnProgress() {
        int i = this.getData().get(2);
        int j = this.getData().get(3);
        return j != 0 && i != 0 ? i * 24 / j : 0;
    }

    public int getLitProgress() {
        int i = this.getData().get(1);
        if (i == 0) {
            i = 200;
        }

        return this.getData().get(0) * 13 / i;
    }

    public boolean isLit() {
        return this.getData().get(0) > 0;
    }

//    private void addPlayerInventory(Inventory playerInventory) {
//        for (int i = 0; i < 3; ++i)
//            for (int l = 0; l < 9; ++l)
//                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
//    }
//
//    private void addPlayerHotbar(Inventory playerInventory) {
//        for (int i = 0; i < 9; ++i) {
//            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
//        }
//    }
}