package com.jetug.chassis_core.common.foundation.registery;

import com.jetug.chassis_core.common.foundation.ChassisArmorMaterial;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public class ArmorMaterialRegistry {
    public static ChassisArmorMaterial IRON = new ChassisArmorMaterial(
            "iron", 15, new int[]{3, 6, 4, 4}, 3,9,
            SoundEvents.ARMOR_EQUIP_IRON, 0.0F,
            () -> Ingredient.of(Items.IRON_INGOT), new int[]{10, 16, 4, 4});
}
