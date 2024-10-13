package com.jetug.chassis_core.common.foundation.item;

import com.jetug.chassis_core.common.foundation.ChassisArmorMaterial;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.item.ItemStack;

public class ChassisArmor extends DamageableItem {
    public final ChassisArmorMaterial material;

    public ChassisArmor(Properties properties, ChassisArmorMaterial material) {
        super(properties);
        this.material = material;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        if(getConfig() == null)
            return super.getMaxDamage(stack);

        var part = getConfig().getPart();
        return material.getDurabilityForSlot(part);
    }

    public static boolean hasArmor(ItemStack itemStack) {
        return getItemDamage(itemStack) < itemStack.getMaxDamage();
    }

    public ChassisArmorMaterial getMaterial() {
        return material;
    }

    public float getDamageAfterAbsorb(float damage) {
        var part = getConfig().getPart();
        return CombatRules.getDamageAfterAbsorb(damage, material.getDefenseForSlot(part), material.getToughness());
    }

    public void damageArmor(ItemStack itemStack, int dmg) {
        var resultDamage = getItemDamage(itemStack) + dmg;
        setItemDamage(itemStack, Math.min(resultDamage, itemStack.getMaxDamage()));
    }
}
