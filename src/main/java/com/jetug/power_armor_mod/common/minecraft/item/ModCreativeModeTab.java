package com.jetug.power_armor_mod.common.minecraft.item;

import com.jetug.power_armor_mod.common.minecraft.registery.ModItems;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModCreativeModeTab {
    public static final ItemGroup MY_TAB = new ItemGroup("xxx") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.DICK.get());
        }
    };
}
