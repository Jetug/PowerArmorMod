package com.jetug.chassis_core.common.foundation.registery;

import com.jetug.chassis_core.common.foundation.container.screen.ArmorStationScreen;
import com.jetug.chassis_core.common.foundation.container.screen.ChassisGui;
import net.minecraft.client.gui.screens.MenuScreens;

import static com.jetug.chassis_core.common.foundation.registery.ContainerRegistry.*;

public class GuiRegistry {
    public static void register() {
        MenuScreens.register(ARMOR_CONTAINER.get(), ChassisGui::new);
        MenuScreens.register(ARMOR_STATION_MENU.get(), ArmorStationScreen::new);
        //MenuScreens.register(CASTING_TABLE_STATION_MENU.get(), CastingTableGui::new);
    }
}
