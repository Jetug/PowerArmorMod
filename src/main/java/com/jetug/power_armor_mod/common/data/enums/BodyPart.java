package com.jetug.power_armor_mod.common.data.enums;

import com.google.gson.annotations.*;

public enum BodyPart{
    @SerializedName("head"      ) HELMET            ("head"      ),
    @SerializedName("body"      ) BODY_ARMOR        ("body"      ),
    @SerializedName("left_arm"  ) LEFT_ARM_ARMOR    ("left_arm"  ),
    @SerializedName("right_arm" ) RIGHT_ARM_ARMOR   ("right_arm" ),
    @SerializedName("left_leg"  ) LEFT_LEG_ARMOR    ("left_leg"  ),
    @SerializedName("right_leg" ) RIGHT_LEG_ARMOR   ("right_leg" ),
    @SerializedName("engine"    ) ENGINE            ("engine"    ),
    @SerializedName("engine"    ) BACK              ("back"      );

    private final String name;
    private static final BodyPart[] values = BodyPart.values();


    BodyPart(String name) {
        this.name = name;
    }

    public static BodyPart getById(int id) {
        return values[id];
    }

    public boolean isArmorItem(){
        return this.ordinal() < 6;
    }

    public int getId(){
        return ordinal();
    }

    public String getName() {
        return name;
    }
}