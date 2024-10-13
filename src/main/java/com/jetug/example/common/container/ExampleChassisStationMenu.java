package com.jetug.example.common.container;

import com.jetug.chassis_core.common.config.holders.BodyPart;
import com.jetug.chassis_core.common.foundation.container.menu.EntityMenu;
import com.jetug.chassis_core.common.foundation.entity.WearableChassis;
import com.jetug.example.common.entities.ExampleChassis;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;

import static com.jetug.chassis_core.common.data.constants.ChassisPart.*;
import static com.jetug.chassis_core.common.data.constants.Gui.*;
import static com.jetug.example.common.registery.ContainerRegistry.EXAMPLE_STATION_MENU;

public class ExampleChassisStationMenu extends EntityMenu {
    public static final int SIZE = 6;
    private static final int INVENTORY_POS_Y = 105;

    public ExampleChassisStationMenu(int i, Inventory playerInventory, FriendlyByteBuf buf) {
        this(i, new SimpleContainer(SIZE), playerInventory, null);
    }

    public ExampleChassisStationMenu(int containerId, Container container, Inventory playerInventory, WearableChassis entity) {
        super(EXAMPLE_STATION_MENU.get(), containerId, container, playerInventory, entity, SIZE, INVENTORY_POS_Y);
        createSlot(BodyPart.HEAD     , FRAME_BODY_SLOT_POS);
        createSlot(BodyPart.BODY     , FRAME_LEFT_ARM_SLOT_POS);
        createSlot(BodyPart.LEFT_ARM , FRAME_RIGHT_ARM_SLOT_POS);
        createSlot(BodyPart.RIGHT_ARM, FRAME_LEFT_LEG_SLOT_POS);
        createSlot(BodyPart.LEFT_LEG , FRAME_RIGHT_LEG_SLOT_POS);
        createSlot(BodyPart.RIGHT_LEG, ENGINE_SLOT_POS2);
    }

    @Override
    protected int getId(BodyPart chassisPart) {
        return ExampleChassis.getId(chassisPart);
    }
}
