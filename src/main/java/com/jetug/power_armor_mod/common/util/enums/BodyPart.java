package com.jetug.power_armor_mod.common.util.enums;

public enum BodyPart{
    HEAD      (0,"head"     ),
    BODY      (1,"body"     ),
    LEFT_ARM  (2,"left_arm" ),
    RIGHT_ARM (3,"right_arm"),
    LEFT_LEG  (4,"left_leg" ),
    RIGHT_LEG (5,"right_leg");

    private Integer id;
    private String name;

    BodyPart(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId(){
        return id;
    }

    public String getName() {
        return name;
    }
}