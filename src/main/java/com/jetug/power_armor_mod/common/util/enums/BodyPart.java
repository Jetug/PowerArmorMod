package com.jetug.power_armor_mod.common.util.enums;

import com.google.gson.annotations.SerializedName;

public enum BodyPart{
    @SerializedName("head")
    HEAD      (0,"head"      ),
    @SerializedName("body")
    BODY      (1,"body"      ),
    @SerializedName("left_arm")
    LEFT_ARM  (2,"left_arm"  ),
    @SerializedName("right_arm")
    RIGHT_ARM (3,"right_arm" ),
    @SerializedName("left_leg")
    LEFT_LEG  (4,"left_leg"  ),
    @SerializedName("right_leg")
    RIGHT_LEG (5,"right_leg" ),
    @SerializedName("left_hand")
    LEFT_HAND (6,"left_hand" ),
    @SerializedName("right_hand")
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