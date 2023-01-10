package com.jetug.power_armor_mod.client.gui;

import net.minecraft.world.inventory.MenuType;

public class ContainerRegistry {
    public static final RegistryObject<MenuType<ContainerDragon>> DRAGON_CONTAINER = register(
            () -> new MenuType<>(ContainerDragon::new), "dragon");
}
