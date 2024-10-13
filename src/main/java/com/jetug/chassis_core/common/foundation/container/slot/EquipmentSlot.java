package com.jetug.chassis_core.common.foundation.container.slot;

import com.jetug.chassis_core.common.config.holders.BodyPart;
import com.jetug.chassis_core.common.foundation.item.ChassisEquipment;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public class EquipmentSlot extends Slot {
    private final BodyPart bodyPart;

    public EquipmentSlot(BodyPart bodyPart, Container itemHandler, int index, int x, int y) {
        super(itemHandler, index, x, y);
        this.bodyPart = bodyPart;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        if(stack.getItem() instanceof ChassisEquipment item){
            var part = item.getConfig().getPart();
            var equalSlots = part == bodyPart;
            return super.mayPlace(stack) && equalSlots;
        }
        return false;
    }
}