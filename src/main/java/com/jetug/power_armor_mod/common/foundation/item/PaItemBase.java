package com.jetug.power_armor_mod.common.foundation.item;

import com.jetug.power_armor_mod.client.ClientConfig;
import com.jetug.power_armor_mod.common.json.ArmorPartSettings;
import com.jetug.power_armor_mod.common.util.helpers.ResourceHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class PaItemBase extends Item {
    private static final String DAMAGE_KEY = "Damage";

    public PaItemBase(Properties pProperties) {
        super(pProperties);
    }

    public ArmorPartSettings getPartSettings(){
        return ClientConfig.resourceManager.getPartSettings(getName());
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
