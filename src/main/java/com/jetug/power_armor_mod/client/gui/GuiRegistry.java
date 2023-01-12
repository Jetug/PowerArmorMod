package com.jetug.power_armor_mod.client.gui;

import net.minecraft.client.gui.screens.MenuScreens;

public class GuiRegistry {
    public static void register() {
        MenuScreens.register(ContainerRegistry.DRAGON_CONTAINER.get(), PowerArmorGui::new);
    }
}
