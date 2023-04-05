package com.jetug.power_armor_mod.common.data.enums;

public enum EquipmentType {
    STANDARD (0),
    DRILL    (1);

    private int id;

    EquipmentType(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }
}
