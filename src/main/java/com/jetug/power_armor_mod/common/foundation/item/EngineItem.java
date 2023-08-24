package com.jetug.power_armor_mod.common.foundation.item;

import com.jetug.power_armor_mod.common.data.enums.*;

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
