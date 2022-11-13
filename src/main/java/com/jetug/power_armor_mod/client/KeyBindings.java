package com.jetug.power_armor_mod.client;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class KeyBindings {

    public static final KeyBinding DASH = new KeyBinding("key.dash", KeyConflictContext.IN_GAME,
            InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_CAPS_LOCK, "key.categories.armor");

    public static final KeyBinding LEAVE = new KeyBinding("key.leave", KeyConflictContext.IN_GAME,
            InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_G, "key.categories.armor");

    public static List<KeyBinding> getKeys() {
        List<KeyBinding> keys = new ArrayList<>();

        for (Field field: KeyBindings.class.getFields()) {
            try {
                if (field.get(null) instanceof KeyBinding)
                    keys.add((KeyBinding)field.get(null));
            }
            catch (IllegalAccessException ignored) {}
        }

        return keys;
    }
}
