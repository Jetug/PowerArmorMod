package com.jetug.power_armor_mod.common.foundation.item;

import com.jetug.power_armor_mod.common.foundation.ModCreativeModeTab;
import net.minecraft.world.item.Item;

public class CastItem extends Item {
    private final ChassisArmor result;

    public CastItem(ChassisArmor result) {
        super(new Properties().tab(ModCreativeModeTab.MY_TAB).stacksTo(1));
        this.result = result;
    }

    public ChassisArmor getResult() {
        return result;
    }
}
