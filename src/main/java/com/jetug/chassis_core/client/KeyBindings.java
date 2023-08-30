package com.jetug.chassis_core.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class KeyBindings {

    public static final KeyMapping DASH = new KeyMapping("key.dash", KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_CAPS_LOCK, "key.categories.armor");

    public static final KeyMapping LEAVE = new KeyMapping("key.leave", KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_G, "key.categories.armor");

    public static List<KeyMapping> getKeys() {
        List<KeyMapping> keys = new ArrayList<>();

        for (Field field: KeyBindings.class.getFields()) try {
            if (field.get(null) instanceof KeyMapping)
                keys.add((KeyMapping)field.get(null));
        } catch (IllegalAccessException ignored) {}

        return keys;
    }
}
