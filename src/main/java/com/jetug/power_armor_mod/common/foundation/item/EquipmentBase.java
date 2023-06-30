package com.jetug.power_armor_mod.common.foundation.item;

import com.jetug.power_armor_mod.client.ClientConfig;
import com.jetug.power_armor_mod.common.data.json.EquipmentSettings;
import com.jetug.power_armor_mod.common.data.enums.*;
import com.jetug.power_armor_mod.common.foundation.ModCreativeModeTab;
import com.jetug.power_armor_mod.common.util.helpers.ResourceHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class EquipmentBase extends Item {
    private static final String DAMAGE_KEY = "Damage";

    public final BodyPart part;

    public EquipmentBase(Properties pProperties, BodyPart part) {
        super(pProperties);
        this.part = part;
    }

    public EquipmentBase(BodyPart part) {
        super(new Item.Properties().tab(ModCreativeModeTab.MY_TAB).stacksTo(1));
        this.part = part;
    }

    public EquipmentSettings getPartSettings(){
        return ClientConfig.resourceManager.getEquipmentSettings(getName());
    }

    public String getName(){
        return ResourceHelper.getResourceName(getRegistryName());
    }

    public static int getItemDamage(ItemStack itemStack) {
        if(itemStack.hasTag()) {
            CompoundTag nbt = itemStack.getOrCreateTag();
            return nbt.getInt(DAMAGE_KEY);
        }
        else return 0;
    }

    public static void setItemDamage(ItemStack head, int totalDamage)
    {
        CompoundTag nbt = head.getOrCreateTag();
        nbt.putInt(DAMAGE_KEY, totalDamage);
    }
}
