package com.jetug.chassis_core.common.foundation.registery;

import com.jetug.chassis_core.ChassisCore;
import com.jetug.chassis_core.common.foundation.container.menu.*;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ContainerRegistry {
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister
            .create(ForgeRegistries.CONTAINERS, ChassisCore.MOD_ID);

    public static final RegistryObject<MenuType<ChassisMenu>> ARMOR_CONTAINER
            = CONTAINERS.register("power_armor", () -> new MenuType<>(ChassisMenu::new));

    public static final RegistryObject<MenuType<ArmorStationMenu>> ARMOR_STATION_MENU
            = CONTAINERS.register("armor_station", () -> new MenuType<>(ArmorStationMenu::new));

    public static final RegistryObject<MenuType<CastingTableMenu>> CASTING_TABLE_MENU =
            registerMenuType(CastingTableMenu::new, "casting_table_menu");


    private static <T extends AbstractContainerMenu>RegistryObject<MenuType<T>> registerMenuType(IContainerFactory<T> factory, String name) {
        return CONTAINERS.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void register(IEventBus eventBus) {
        CONTAINERS.register(eventBus);
    }
}
