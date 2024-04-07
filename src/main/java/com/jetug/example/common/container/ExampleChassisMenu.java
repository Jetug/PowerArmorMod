package com.jetug.example.common.container;

import com.jetug.chassis_core.common.foundation.container.menu.EntityMenu;
import com.jetug.chassis_core.common.foundation.entity.WearableChassis;
import com.jetug.chassis_core.common.util.Pos2I;
import com.jetug.example.common.entities.ExampleChassis;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;

import static com.jetug.chassis_core.common.data.constants.ChassisPart.*;
import static com.jetug.example.common.registery.ContainerRegistry.EXAMPLE_CHASSIS_MENU;

public class ExampleChassisMenu extends EntityMenu {
    public static final int SIZE = 7;
    private static final int INVENTORY_POS_Y = 84;

    public ExampleChassisMenu(int i, Inventory playerInventory) {
        this(i, new SimpleContainer(SIZE), playerInventory, null);
    }

    public ExampleChassisMenu(int containerId, Container container, Inventory playerInventory, WearableChassis entity) {
        super(EXAMPLE_CHASSIS_MENU.get(), containerId, container, playerInventory, entity, SIZE, INVENTORY_POS_Y);
        createSlot(HELMET          , new Pos2I(82 , 11));
        createSlot(BODY_ARMOR      , new Pos2I(82 , 32));
        createSlot(RIGHT_ARM_ARMOR  , new Pos2I(61 , 26));
        createSlot(LEFT_ARM_ARMOR , new Pos2I(103, 26));
        createSlot(RIGHT_LEG_ARMOR  , new Pos2I(69 , 54));
        createSlot(LEFT_LEG_ARMOR , new Pos2I(95 , 54));
    }

    @Override
    protected int getId(String chassisPart) {
        return ExampleChassis.getId(chassisPart);
    }
}