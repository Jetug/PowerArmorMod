package com.jetug.power_armor_mod.common.util.extensions;

public class Key {
    public static boolean isEqual(int key, KeyMapping keyBinding){
        return key == keyBinding.getKey().getValue();
    }
}
