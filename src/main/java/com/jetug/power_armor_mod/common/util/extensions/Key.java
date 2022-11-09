package com.jetug.power_armor_mod.common.util.extensions;

import net.minecraft.client.settings.KeyBinding;

public class Key {
    public static boolean isEqual(int key, KeyBinding keyBinding){
        return key == keyBinding.getKey().getValue();
    }
}
