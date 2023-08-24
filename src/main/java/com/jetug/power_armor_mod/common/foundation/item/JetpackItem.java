package com.jetug.power_armor_mod.common.foundation.item;

import com.jetug.power_armor_mod.common.data.enums.BodyPart;

public class JetpackItem extends ChassisEquipment {
    public final int heat;
    public final float speed;
    public final float force;

    public JetpackItem(int heat, Float speed, float force) {
        super(BodyPart.BACK);
        this.heat = heat;
        this.speed = speed;
        this.force = force;
    }
}