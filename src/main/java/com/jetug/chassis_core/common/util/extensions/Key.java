package com.jetug.chassis_core.common.util.extensions;

import net.minecraft.client.KeyMapping;

public class Key {
    public static boolean isEqual(int key, KeyMapping keyBinding){
        return key == keyBinding.getKey().getValue();
    }
}
