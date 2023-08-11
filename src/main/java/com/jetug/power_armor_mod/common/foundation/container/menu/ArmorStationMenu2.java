package com.jetug.power_armor_mod.common.foundation.container.menu;

import com.jetug.power_armor_mod.common.foundation.container.menu.base.EntityMenu;
import com.jetug.power_armor_mod.common.foundation.entity.PowerArmorEntity;
import com.jetug.power_armor_mod.common.util.Pos2D;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;

import static com.jetug.power_armor_mod.common.data.constants.Gui.*;
import static com.jetug.power_armor_mod.common.data.enums.BodyPart.*;
import static com.jetug.power_armor_mod.common.foundation.entity.PowerArmorBase.P_SIZE;
import static com.jetug.power_armor_mod.common.foundation.registery.ContainerRegistry.*;

public class ArmorStationMenu2 extends EntityMenu {
    private static final int INVENTORY_POS_Y = 105;

    public ArmorStationMenu2(int i, Inventory playerInventory) {
        this(i, new SimpleContainer(P_SIZE), playerInventory, null);
    }

    public ArmorStationMenu2(int containerId, Container container, Inventory playerInventory, PowerArmorEntity entity) {
        super(ARMOR_STATION_MENU.get(), containerId, container, playerInventory, entity, P_SIZE, INVENTORY_POS_Y);
        createSlot(BODY_FRAME       , FRAME_BODY_SLOT_POS      );
        createSlot(LEFT_ARM_FRAME   , FRAME_LEFT_ARM_SLOT_POS  );
        createSlot(RIGHT_ARM_FRAME  , FRAME_RIGHT_ARM_SLOT_POS );
        createSlot(LEFT_LEG_FRAME   , FRAME_LEFT_LEG_SLOT_POS  );
        createSlot(RIGHT_LEG_FRAME  , FRAME_RIGHT_LEG_SLOT_POS );
        createSlot(ENGINE           , new Pos2D(122, 44) );
        createSlot(BACK             , new Pos2D(122, 16) );
        createSlot(COOLING          , new Pos2D(122, 72) );
    }


}
