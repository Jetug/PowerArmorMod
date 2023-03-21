package com.jetug.power_armor_mod.common.foundation.registery;

import com.jetug.power_armor_mod.client.gui.PowerArmorGui;
import com.jetug.power_armor_mod.common.foundation.registery.ContainerRegistry;
import net.minecraft.client.gui.screens.MenuScreens;

public class GuiRegistry {
    public static void register() {
        MenuScreens.register(ContainerRegistry.ARMOR_CONTAINER.get(), PowerArmorGui::new);
    }
}
