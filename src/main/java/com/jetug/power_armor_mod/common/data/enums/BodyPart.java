package com.jetug.power_armor_mod.common.data.enums;

import com.google.gson.annotations.*;

public enum BodyPart{
    @SerializedName("head"           ) HELMET         ,
    @SerializedName("body"           ) BODY_ARMOR     ,
    @SerializedName("left_arm"       ) LEFT_ARM_ARMOR ,
    @SerializedName("right_arm"      ) RIGHT_ARM_ARMOR,
    @SerializedName("left_leg"       ) LEFT_LEG_ARMOR , //5
    @SerializedName("right_leg"      ) RIGHT_LEG_ARMOR,
    @SerializedName("engine"         ) ENGINE         ,
    @SerializedName("back"           ) BACK           ,
    @SerializedName("cooling"        ) COOLING        ,
    @SerializedName("body_frame"     ) BODY_FRAME     , //10
    @SerializedName("left_arm_frame" ) LEFT_ARM_FRAME ,
    @SerializedName("right_arm_frame") RIGHT_ARM_FRAME,
    @SerializedName("left_leg_frame" ) LEFT_LEG_FRAME ,
    @SerializedName("right_leg_frame") RIGHT_LEG_FRAME,
    @SerializedName("left_hand"      ) LEFT_HAND,
    @SerializedName("right_hand"     ) RIGHT_HAND; //16

    private static final BodyPart[] values = BodyPart.values();

    public static BodyPart getById(int id) {
        return values[id];
    }

    public int getArmorPartId(){
        return switch (this){
            case HELMET -> 0;
            case BODY_ARMOR -> 1;
            case LEFT_ARM_ARMOR, RIGHT_ARM_ARMOR -> 2;
            case LEFT_LEG_ARMOR , RIGHT_LEG_ARMOR -> 3;
            default -> throw new IllegalStateException("Unexpected value: " + this);
        };
    }

    public boolean isArmorItem(){
        return this.ordinal() < 6;
    }

    public int getId(){
        return ordinal();
    }
}