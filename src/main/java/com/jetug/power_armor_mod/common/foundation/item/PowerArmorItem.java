package com.jetug.power_armor_mod.common.foundation.item;

import com.jetug.power_armor_mod.client.ClientConfig;
import com.jetug.power_armor_mod.common.foundation.PowerArmorMaterial;
import com.jetug.power_armor_mod.common.json.ArmorPartSettings;
import com.jetug.power_armor_mod.common.foundation.ModCreativeModeTab;
import com.jetug.power_armor_mod.common.util.enums.BodyPart;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.item.ItemStack;

public class PowerArmorItem extends PaItemBase {
    public static final String DAMAGE_KEY = "Damage";
    private ArmorPartSettings armorPartSettings = null;
    private final PowerArmorMaterial material;
    public final BodyPart part;

    public PowerArmorItem(PowerArmorMaterial material, BodyPart part) {
        super((new Properties()).durability(material.getDurabilityForSlot(part)).tab(ModCreativeModeTab.MY_TAB));
        this.material = material;
        this.part = part;
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
