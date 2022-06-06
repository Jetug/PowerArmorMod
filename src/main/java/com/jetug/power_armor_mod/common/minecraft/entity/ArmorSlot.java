package com.jetug.power_armor_mod.common.minecraft.entity;

import com.jetug.power_armor_mod.client.render.ResourceHelper;
import com.jetug.power_armor_mod.common.util.enums.BodyPart;
import com.jetug.power_armor_mod.common.util.enums.EquipmentType;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class ArmorSlot extends Slot {
    public ArmorSlot(IPowerArmor parent, BodyPart part, EquipmentType type) {
        super(parent, part, type);
    }

    public boolean hasArmor(){
        return getDurability() > 0;
    }

    public float getDurability(){
        return parent.getArmorDurability(part);
    }

    public void setDurability(float value){
        parent.setArmorDurability(part, value);
    }

    public void damage(float damage){
        parent.damageArmor(part, damage);
    }
}
