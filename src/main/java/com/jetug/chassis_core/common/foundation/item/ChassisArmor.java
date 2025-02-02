package com.jetug.chassis_core.common.foundation.item;

import com.jetug.chassis_core.common.foundation.ChassisArmorMaterial;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.item.ItemStack;

public class ChassisArmor extends DamageableItem {
    public final ChassisArmorMaterial material;

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
}
