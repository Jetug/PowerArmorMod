package com.jetug.chassis_core.common.foundation.item;

import com.jetug.chassis_core.common.foundation.ChassisArmorMaterial;
import com.jetug.chassis_core.common.foundation.ModCreativeModeTab;
import com.jetug.chassis_core.common.data.enums.*;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.item.ItemStack;

public class ChassisArmor extends DamageableItem {
    private final ChassisArmorMaterial material;
    public final float speed;

    public ChassisArmor(ChassisArmorMaterial material, BodyPart part, float speed) {
        super((new Properties()).durability(material.getDurabilityForSlot(part)).tab(ModCreativeModeTab.MY_TAB), part);
        this.material = material;
        this.speed = speed;
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
