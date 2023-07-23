package com.jetug.power_armor_mod.common.foundation.screen.menu;

import com.jetug.power_armor_mod.common.foundation.entity.PowerArmorEntity;
import com.jetug.power_armor_mod.common.foundation.registery.ContainerRegistry;
import com.jetug.power_armor_mod.common.foundation.screen.slot.EquipmentSlot;
import com.jetug.power_armor_mod.common.util.Pos2D;
import com.jetug.power_armor_mod.common.data.enums.*;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;

import static com.jetug.power_armor_mod.common.data.constants.Gui.*;
import static com.jetug.power_armor_mod.common.data.enums.BodyPart.*;

public class PowerArmorMenu extends MenuBase {
    public static final int SIZE = 7;
    private static final int INVENTORY_POS_Y = 84;

//    static final ResourceLocation[] TEXTURE_EMPTY_SLOTS = new ResourceLocation[]
//            {
//                    EMPTY_ARMOR_SLOT_HEAD,
//                    EMPTY_ARMOR_SLOT_BODY,
//                    EMPTY_ARMOR_SLOT_LEFT_ARM,
//                    EMPTY_ARMOR_SLOT_RIGHT_ARM,
//                    EMPTY_ARMOR_SLOT_LEFT_LEG,
//                    EMPTY_ARMOR_SLOT_RIGHT_LEG
//            };

    public PowerArmorMenu(int i, Inventory playerInventory) {
        this(i, new SimpleContainer(SIZE), playerInventory, null);
    }

    public PowerArmorMenu(int containerId, Container container, Inventory playerInventory, PowerArmorEntity entity) {
        super(ContainerRegistry.ARMOR_CONTAINER.get(), containerId, container, playerInventory, entity, SIZE, INVENTORY_POS_Y);
        createSlot(HELMET, HEAD_SLOT_POS     );
        createSlot(BODY_ARMOR, BODY_SLOT_POS     );
        createSlot(LEFT_ARM_ARMOR, RIGHT_ARM_SLOT_POS);
        createSlot(RIGHT_ARM_ARMOR, LEFT_ARM_SLOT_POS );
        createSlot(LEFT_LEG_ARMOR, RIGHT_LEG_SLOT_POS);
        createSlot(RIGHT_LEG_ARMOR, LEFT_LEG_SLOT_POS );
        createSlot(ENGINE   , ENGINE_SLOT_POS   );
    }

//    private void createSlot(BodyPart bodyPart, Pos2D pos){
//        this.addSlot(new EquipmentSlot(bodyPart, container, bodyPart.getId(), pos.x, pos.y));
//    }
}