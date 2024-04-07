package com.jetug.example.common.registery;

import com.jetug.example.common.screen.ExampleChassisScreen;
import com.jetug.example.common.screen.ExampleChassisStationScreen;
import net.minecraft.client.gui.screens.MenuScreens;

public class GuiRegistry {
    public static void register() {
        MenuScreens.register(ContainerRegistry.EXAMPLE_CHASSIS_MENU.get(), ExampleChassisScreen::new);
        MenuScreens.register(ContainerRegistry.EXAMPLE_STATION_MENU.get(), ExampleChassisStationScreen::new);
    }
}
