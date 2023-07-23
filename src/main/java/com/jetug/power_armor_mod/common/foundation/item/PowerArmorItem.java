package com.jetug.power_armor_mod.common.foundation.item;

import com.jetug.power_armor_mod.common.foundation.PowerArmorMaterial;
import com.jetug.power_armor_mod.common.foundation.ModCreativeModeTab;
import com.jetug.power_armor_mod.common.data.enums.*;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.item.ItemStack;

public class PowerArmorItem extends DamageableItem {
    private final PowerArmorMaterial material;
    public final float speed;

    public PowerArmorItem(PowerArmorMaterial material, BodyPart part) {
        this(material, part, 1f);
    }

    public PowerArmorItem(PowerArmorMaterial material, BodyPart part, float speed) {
        super((new Properties()).durability(material.getDurabilityForSlot(part)).tab(ModCreativeModeTab.MY_TAB), part);
        this.material = material;
        this.speed = speed;
    }

    public PowerArmorMaterial getMaterial(){
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
