package com.jetug.power_armor_mod.common.foundation.registery;

import com.jetug.power_armor_mod.client.gui.ArmorStationGui2;
import com.jetug.power_armor_mod.client.gui.PowerArmorGui;
import net.minecraft.client.gui.screens.MenuScreens;

public class GuiRegistry {
    public static void register() {
        MenuScreens.register(ContainerRegistry.ARMOR_CONTAINER.get(), PowerArmorGui::new);
        MenuScreens.register(ContainerRegistry.ARMOR_STATION_MENU.get(), ArmorStationGui2::new);
    }
}
