package com.jetug.power_armor_mod.common.util.helpers;

public class MathHelper {
    public static int getInPercents(int value, int maxValue){
       return (int)((float)value / maxValue * 100);
    }

    public float getPercentOf(int value, int percents){
        return value * ((float)percents / 100.0f);
    }
}
