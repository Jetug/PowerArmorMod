package com.jetug.power_armor_mod.common.foundation.screen.menu;

import com.jetug.power_armor_mod.common.data.enums.*;
import com.jetug.power_armor_mod.common.foundation.entity.PowerArmorEntity;
import com.jetug.power_armor_mod.common.foundation.registery.ContainerRegistry;
import com.jetug.power_armor_mod.common.foundation.screen.slot.EquipmentSlot;
import com.jetug.power_armor_mod.common.util.Pos2D;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;

import static com.jetug.power_armor_mod.common.data.constants.Gui.*;
import static com.jetug.power_armor_mod.common.data.enums.BodyPart.*;

public class ArmorStationMenu2 extends MenuBase {
    private static final int INVENTORY_POS_Y = 105;
    public  static final int SIZE = 6;

    public ArmorStationMenu2(int i, Inventory playerInventory) {
        this(i, new SimpleContainer(SIZE), playerInventory, null);
    }

    public ArmorStationMenu2(int containerId, Container container, Inventory playerInventory, PowerArmorEntity entity) {
        super(ContainerRegistry.ARMOR_STATION_MENU.get(), containerId, container, playerInventory, entity, SIZE, INVENTORY_POS_Y);
        createSlot(BODY_ARMOR, FRAME_BODY_SLOT_POS     );
        createSlot(LEFT_ARM_ARMOR, FRAME_LEFT_ARM_SLOT_POS );
        createSlot(RIGHT_ARM_ARMOR, FRAME_RIGHT_ARM_SLOT_POS);
        createSlot(LEFT_LEG_ARMOR, FRAME_LEFT_LEG_SLOT_POS );
        createSlot(RIGHT_LEG_ARMOR, FRAME_RIGHT_LEG_SLOT_POS);
        createSlot(ENGINE   , FRAME_RIGHT_LEG_SLOT_POS);
    }

    private void createSlot(BodyPart bodyPart, Pos2D pos){
        this.addSlot(new EquipmentSlot(bodyPart, container, bodyPart.getId(), pos.x, pos.y));
    }
}
