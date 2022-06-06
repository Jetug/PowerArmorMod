package com.jetug.power_armor_mod.common.minecraft.entity;

import com.jetug.power_armor_mod.client.render.ResourceHelper;
import com.jetug.power_armor_mod.common.util.enums.BodyPart;
import com.jetug.power_armor_mod.common.util.enums.EquipmentType;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class EquipmentSlot extends Slot {
    public EquipmentSlot(PowerArmorEntity parent, BodyPart part, EquipmentType type) {
        super(parent, part, type);
    }
}
