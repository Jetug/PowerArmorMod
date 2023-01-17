package com.jetug.power_armor_mod.common.util.enums;

public enum BodyPart{
    HEAD      (0,"head"      ),
    BODY      (1,"body"      ),
    LEFT_ARM  (2,"left_arm"  ),
    RIGHT_ARM (3,"right_arm" ),
    LEFT_LEG  (4,"left_leg"  ),
    RIGHT_LEG (5,"right_leg" ),
    LEFT_HAND (6,"left_hand" ),
    RIGHT_HAND(7,"right_hand");

    private int id;
    private String name;

    private static final BodyPart[] values = BodyPart.values();

    BodyPart(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static BodyPart getById(int id) {
        return values[id];
    }


    public int getId(){
        return id;
    }

    public String getName() {
        return name;
    }
}