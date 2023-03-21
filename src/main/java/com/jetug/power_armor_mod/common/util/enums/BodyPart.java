package com.jetug.power_armor_mod.common.util.enums;

import com.google.gson.annotations.*;

public enum BodyPart{
    @SerializedName("head"      ) HEAD      ("head"      ),
    @SerializedName("body"      ) BODY      ("body"      ),
    @SerializedName("left_arm"  ) LEFT_ARM  ("left_arm"  ),
    @SerializedName("right_arm" ) RIGHT_ARM ("right_arm" ),
    @SerializedName("left_leg"  ) LEFT_LEG  ("left_leg"  ),
    @SerializedName("right_leg" ) RIGHT_LEG ("right_leg" ),
    @SerializedName("engine"    ) ENGINE    ("engine"    );

//    @SerializedName("left_hand" ) LEFT_HAND ("left_hand" ),
//    @SerializedName("right_hand") RIGHT_HAND("right_hand");

    private final String name;
    private static final BodyPart[] values = BodyPart.values();

    BodyPart(String name) {
        this.name = name;
    }

    public static BodyPart getById(int id) {
        return values[id];
    }

    public int getId(){
        return ordinal();
    }

    public String getName() {
        return name;
    }
}