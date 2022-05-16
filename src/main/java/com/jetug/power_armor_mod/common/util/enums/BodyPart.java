package com.jetug.power_armor_mod.common.util.enums;

public enum BodyPart{
    HEAD("head_"),
    BODY("body"),
    LEFT_ARM("left_arm"),
    RIGHT_ARM("right_arm"),
    LEFT_LEG("left_leg"),
    RIGHT_LEG("right_leg");

    String name;

    public String getName() {
        return name;
    }

    BodyPart(String name) {
        this.name = name;
    }
}