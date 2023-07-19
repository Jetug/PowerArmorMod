package com.jetug.power_armor_mod.common.foundation.item;

import com.jetug.power_armor_mod.common.data.enums.BodyPart;

public class JetpackItem extends EquipmentBase {
    public final Integer heat;
    public final Float speed;

    public JetpackItem(Integer heat, Float speed) {
        super(BodyPart.BACK);
        this.heat = heat;
        this.speed = speed;
    }
}