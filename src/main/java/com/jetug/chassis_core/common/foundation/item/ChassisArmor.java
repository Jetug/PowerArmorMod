package com.jetug.chassis_core.common.foundation.item;

import com.jetug.chassis_core.common.foundation.ChassisArmorMaterial;
import com.jetug.chassis_core.common.data.enums.*;
import com.jetug.chassis_core.common.foundation.container.slot.EquipmentSlot;
import com.jetug.chassis_core.common.util.Pos2I;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;

public class ChassisArmor extends DamageableItem {
    private final ChassisArmorMaterial material;

    public ChassisArmor(Properties pProperties, ChassisArmorMaterial material, String part) {
        super(pProperties.durability(material.getDurabilityForSlot(part)), part);
        this.material = material;
    }

    public ChassisArmorMaterial getMaterial(){
        return material;
    }

    public float getDamageAfterAbsorb(float damage){
        return CombatRules.getDamageAfterAbsorb(damage, material.getDefenseForSlot(part), material.getToughness());
    }

    public static boolean hasArmor(ItemStack itemStack) {
        return getItemDamage(itemStack) < itemStack.getMaxDamage();
    }

    public void damageArmor(ItemStack itemStack, int dmg)
    {
        var resultDamage = getItemDamage(itemStack)+dmg;
        setItemDamage(itemStack, Math.min(resultDamage, itemStack.getMaxDamage()));
    }

    public int getIngredientCount(){
        return material.getCraftPerSlotForSlot(part);
    }
}
