package com.jetug.chassis_core.common.foundation.item;

import com.jetug.chassis_core.common.data.enums.*;

public class EngineItem extends ChassisEquipment {
    public final Integer overheat;
    public final Integer heat;
    public final Float speed;

    public EngineItem(Integer overheat, Integer heat, Float speed) {
        super(BodyPart.ENGINE);
        this.overheat = overheat;
        this.heat = heat;
        this.speed = speed;
    }
}
