package com.jetug.example.common.container;

import com.jetug.chassis_core.common.config.holders.BodyPart;
import com.jetug.chassis_core.common.foundation.container.menu.EntityMenu;
import com.jetug.chassis_core.common.foundation.entity.WearableChassis;
import com.jetug.chassis_core.common.util.Pos2I;
import com.jetug.example.common.entities.ExampleChassis;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;

import static com.jetug.chassis_core.common.data.constants.ChassisPart.*;
import static com.jetug.example.common.registery.ContainerRegistry.EXAMPLE_CHASSIS_MENU;

public class ExampleChassisMenu extends EntityMenu {
    private static final int SIZE = 6;
    private static final int INVENTORY_POS_Y = 84;

    public ExampleChassisMenu(int i, Inventory playerInventory, FriendlyByteBuf buf) {
        this(i, new SimpleContainer(SIZE), playerInventory, null);
    }

    public ExampleChassisMenu(int containerId, Container container, Inventory playerInventory, WearableChassis entity) {
        super(EXAMPLE_CHASSIS_MENU.get(), containerId, container, playerInventory, entity, SIZE, INVENTORY_POS_Y);
        createSlot(BodyPart.HEAD       , new Pos2I(82, 11));
        createSlot(BodyPart.BODY       , new Pos2I(82, 32));
        createSlot(BodyPart.LEFT_ARM   , new Pos2I(61, 26));
        createSlot(BodyPart.RIGHT_ARM  , new Pos2I(103, 26));
        createSlot(BodyPart.LEFT_LEG   , new Pos2I(69, 54));
        createSlot(BodyPart.RIGHT_LEG  , new Pos2I(95, 54));
    }

    @Override
    protected int getId(BodyPart chassisPart) {
        return ExampleChassis.getId(chassisPart);
    }
}