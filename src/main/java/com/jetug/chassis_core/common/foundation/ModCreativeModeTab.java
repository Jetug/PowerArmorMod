package com.jetug.chassis_core.common.foundation;

import com.jetug.chassis_core.common.foundation.registery.ItemRegistry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModeTab {
    public static final CreativeModeTab MY_TAB = new CreativeModeTab("ArmorPunk") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ItemRegistry.PA_HELMET.get());
        }
    };
}
