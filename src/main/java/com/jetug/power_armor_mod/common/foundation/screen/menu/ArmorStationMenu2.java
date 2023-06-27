package com.jetug.power_armor_mod.common.foundation.screen.menu;

import com.jetug.power_armor_mod.common.data.enums.BodyPart;
import com.jetug.power_armor_mod.common.foundation.block.entity.ArmorStationBlockEntity;
import com.jetug.power_armor_mod.common.foundation.entity.PowerArmorEntity;
import com.jetug.power_armor_mod.common.foundation.item.EquipmentBase;
import com.jetug.power_armor_mod.common.foundation.registery.BlockRegistry;
import com.jetug.power_armor_mod.common.foundation.registery.ContainerRegistry;
import com.jetug.power_armor_mod.common.foundation.registery.ModMenuTypes;
import com.jetug.power_armor_mod.common.foundation.screen.slot.EquipmentSlot;
import com.jetug.power_armor_mod.common.util.Pos2D;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
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

public class ArmorStationMenu2 extends MenuBase {
    private static final int INVENTORY_POS_Y = 105;
    public  static final int SIZE = 5;

    public ArmorStationMenu2(int i, Inventory playerInventory) {
        this(i, new SimpleContainer(SIZE), playerInventory, null);
    }

    public ArmorStationMenu2(int containerId, Container container, Inventory playerInventory, PowerArmorEntity entity) {
        super(ContainerRegistry.ARMOR_CONTAINER.get(), containerId, container, playerInventory, entity, SIZE, INVENTORY_POS_Y);
        createSlot(BODY     , FRAME_BODY_SLOT_POS     );
        createSlot(LEFT_ARM , FRAME_LEFT_ARM_SLOT_POS );
        createSlot(RIGHT_ARM, FRAME_RIGHT_ARM_SLOT_POS);
        createSlot(LEFT_LEG , FRAME_LEFT_LEG_SLOT_POS );
        createSlot(RIGHT_LEG, FRAME_RIGHT_LEG_SLOT_POS);
    }

    private void createSlot(BodyPart bodyPart, Pos2D pos){
        this.addSlot(new EquipmentSlot(bodyPart, container, bodyPart.getId(), pos.x, pos.y));
    }
}
