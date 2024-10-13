package com.jetug.example.common.registery;

import com.jetug.chassis_core.ChassisCore;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static net.minecraft.world.item.CreativeModeTab.Output;
import static net.minecraft.world.item.CreativeModeTab.builder;

public class ModItemTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ChassisCore.MOD_ID);

    public static final RegistryObject<CreativeModeTab> ITEMS = CREATIVE_MODE_TABS.register("chassis_core_items",
            () -> builder().icon(() -> new ItemStack(ChassisArmorItems.FRAME_ITEM.get()))
                    .title(Component.translatable("itemGroup.chassis_core"))
                    .displayItems((params, output) -> registerItems(output))
                    .build());

    private static void registerItems(Output output) {
        if(ChassisCore.isDebugging()) {
            for (var entry : ChassisArmorItems.ITEMS.getEntries()) {
                output.accept(entry.get());
            }
        }
    }

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
