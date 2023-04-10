package com.jetug.power_armor_mod.common.foundation.registery;

import com.jetug.power_armor_mod.common.foundation.screen.container.PowerArmorContainer;
import com.jetug.power_armor_mod.common.data.constants.Global;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ContainerRegistry {
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister
            .create(ForgeRegistries.CONTAINERS, Global.MOD_ID);

    public static final RegistryObject<MenuType<PowerArmorContainer>> ARMOR_CONTAINER
            = CONTAINERS.register("power_armor", () -> new MenuType<>(PowerArmorContainer::new));

    public static void register(IEventBus eventBus) {
        CONTAINERS.register(eventBus);
    }
}
