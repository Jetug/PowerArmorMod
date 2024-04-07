package com.jetug.example.common.registery;

import net.minecraft.client.gui.screens.MenuScreens;
import com.jetug.example.common.screen.*;

public class GuiRegistry {
    public static void register() {
        MenuScreens.register(ContainerRegistry.EXAMPLE_CHASSIS_MENU.get(), ExampleChassisScreen::new);
        MenuScreens.register(ContainerRegistry.EXAMPLE_STATION_MENU.get(), ExampleChassisStationScreen::new);
    }
}
