package com.jetug.chassis_core.common.foundation.item;

import com.jetug.chassis_core.common.data.enums.BodyPart;

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