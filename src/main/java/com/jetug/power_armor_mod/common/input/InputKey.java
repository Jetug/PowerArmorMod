package com.jetug.power_armor_mod.common.input;

import com.jetug.power_armor_mod.client.KeyBindings;
import net.minecraft.client.KeyMapping;

import static com.jetug.power_armor_mod.client.ClientConfig.OPTIONS;

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

    public static InputKey getByKey(KeyMapping key){
        for (InputKey value : values()) {
            if (value.key == key)
                return value;
        }
        return null;
    }
}
