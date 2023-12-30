package com.jetug.chassis_core.common.input;

import com.jetug.chassis_core.client.*;
import net.minecraft.client.KeyMapping;

import static com.jetug.chassis_core.client.ClientConfig.OPTIONS;

public enum InputKey {
    UP    (OPTIONS.keyUp),
    DOWN  (OPTIONS.keyDown),
    LEFT  (OPTIONS.keyLeft),
    RIGHT (OPTIONS.keyRight),
    JUMP  (OPTIONS.keyJump),
    LEAVE (KeyBindings.LEAVE),
    USE   (OPTIONS.keyUse),
    ATTACK(OPTIONS.keyAttack);

    public final KeyMapping key;

    InputKey(KeyMapping key) {
        this.key = key;
    }

    public static InputKey getByKey(int key){
        for (InputKey value : values()) {
            if (value.key.getKey().getValue() == key)
                return value;
        }
        return null;
    }
}
