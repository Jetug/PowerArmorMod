package com.jetug.example.common.registery;

import com.jetug.chassis_core.ChassisCore;
import com.jetug.example.common.container.ExampleChassisMenu;
import com.jetug.example.common.container.ExampleChassisStationMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ContainerRegistry {
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister
            .create(ForgeRegistries.MENU_TYPES, ChassisCore.MOD_ID);

    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(IContainerFactory<T> factory, String name) {
        return CONTAINERS.register(name, () -> IForgeMenuType.create(factory));
    }    public static final RegistryObject<MenuType<ExampleChassisMenu>> EXAMPLE_CHASSIS_MENU
            = CONTAINERS.register("example_chassis_menu", () -> new MenuType<>(ExampleChassisMenu::new));

    public static void register(IEventBus eventBus) {
        CONTAINERS.register(eventBus);
    }    public static final RegistryObject<MenuType<ExampleChassisStationMenu>> EXAMPLE_STATION_MENU
            = CONTAINERS.register("example_chassis_station_menu", () -> new MenuType<>(ExampleChassisStationMenu::new));




}
