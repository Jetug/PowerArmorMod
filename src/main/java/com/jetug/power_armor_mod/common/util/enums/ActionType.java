package com.jetug.power_armor_mod.common.util.enums;

public enum ActionType {
    DISMOUNT;

    private static final ActionType[] values = ActionType.values();

    public static ActionType getById(int id) {
        return values[id];
    }

    public int getId() {
        return ordinal();
    }
}
