package com.jetug.power_armor_mod.common.foundation.item;

import com.jetug.power_armor_mod.client.ClientConfig;
import com.jetug.power_armor_mod.common.data.json.EquipmentSettings;
import com.jetug.power_armor_mod.common.data.enums.*;
import com.jetug.power_armor_mod.common.foundation.ModCreativeModeTab;
import com.jetug.power_armor_mod.common.util.helpers.ResourceHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class EquipmentBase extends Item {

    public final BodyPart part;

    public EquipmentBase(Properties pProperties, BodyPart part) {
        super(pProperties);
        this.part = part;
    }

    public EquipmentBase(BodyPart part) {
        super(new Item.Properties().tab(ModCreativeModeTab.MY_TAB).stacksTo(1));
        this.part = part;
    }

    @Nullable
    public EquipmentSettings getPartSettings(){
        return ClientConfig.resourceManager.getEquipmentSettings(getName());
    }

    public String getName(){
        return ResourceHelper.getResourceName(getRegistryName());
    }

}
