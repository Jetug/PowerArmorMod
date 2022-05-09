package com.jetug.begining.common.util.enums;

public enum BodyPart{
    HEAD("head"),
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