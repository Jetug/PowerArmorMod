package com.jetug.power_armor_mod.common.foundation.registery;

import com.jetug.power_armor_mod.common.foundation.screen.menu.ArmorStationMenu2;
import com.jetug.power_armor_mod.common.foundation.screen.menu.PowerArmorMenu;
import com.jetug.power_armor_mod.common.data.constants.Global;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ContainerRegistry {
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister
            .create(ForgeRegistries.CONTAINERS, Global.MOD_ID);

    public static final RegistryObject<MenuType<PowerArmorMenu>> ARMOR_CONTAINER
            = CONTAINERS.register("power_armor", () -> new MenuType<>(PowerArmorMenu::new));

    public static final RegistryObject<MenuType<ArmorStationMenu2>> ARMOR_STATION_MENU
            = CONTAINERS.register("armor_station", () -> new MenuType<>(ArmorStationMenu2::new));

    public static void register(IEventBus eventBus) {
        CONTAINERS.register(eventBus);
    }
}
