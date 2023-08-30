package com.jetug.chassis_core.common.util.extensions;

public class Array {
    public static int[] arrayFloatToInt(float[] array){
        int[] result = new int[array.length];
        for (int i = 0; i < array.length ;i++){
            int val = Float.floatToIntBits(array[i]);
            if(val == 0)
                val = Float.floatToIntBits(1f);
            result[i] = val;
        }
        return result;
    }

    public static float[] arrayIntToFloat(int[] array){
        float[] result = new float[array.length];
        for (int i = 0; i < array.length ;i++){
            result[i] = Float.intBitsToFloat(array[i]);
        }
        return result;
    }
}
