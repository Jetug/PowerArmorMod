package com.jetug.chassis_core.common.foundation.item;

import com.jetug.chassis_core.common.data.enums.BodyPart;

public class PowerKnuckle extends ChassisEquipment {
    public final int heat;
    public final float force;

    public PowerKnuckle(int heat, float force) {
        super(BodyPart.RIGHT_HAND);
        this.heat = heat;
        this.force = force;
    }
}
