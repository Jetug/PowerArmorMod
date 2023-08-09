package com.jetug.power_armor_mod.common.foundation.registery;

import com.jetug.power_armor_mod.client.gui.menu.*;
import net.minecraft.client.gui.screens.MenuScreens;

import static com.jetug.power_armor_mod.common.foundation.registery.ContainerRegistry.*;

public class GuiRegistry {
    public static void register() {
        MenuScreens.register(ARMOR_CONTAINER.get(), PowerArmorGui::new);
        MenuScreens.register(ARMOR_STATION_MENU.get(), ArmorStationGui::new);
        //MenuScreens.register(CASTING_TABLE_STATION_MENU.get(), CastingTableGui::new);
    }
}
