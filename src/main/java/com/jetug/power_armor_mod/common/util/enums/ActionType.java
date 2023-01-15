package com.jetug.power_armor_mod.common.util.enums;

public enum ActionType {
    DISMOUNT(0);

    private int id;

    ActionType(int id) {
        this.id = id;
    }

    public int getId(){
        return id;
    }
}
