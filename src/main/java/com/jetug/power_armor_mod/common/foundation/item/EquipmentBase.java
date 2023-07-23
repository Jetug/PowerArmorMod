package com.jetug.power_armor_mod.common.foundation.item;

import com.jetug.power_armor_mod.common.data.json.EquipmentSettings;
import com.jetug.power_armor_mod.common.data.enums.*;
import com.jetug.power_armor_mod.common.foundation.ModCreativeModeTab;
import com.jetug.power_armor_mod.common.util.helpers.ResourceHelper;
import net.minecraft.world.item.Item;

import javax.annotation.Nullable;

import static com.jetug.power_armor_mod.client.ClientConfig.*;

public class EquipmentBase extends Item {
    private String name = null;
    private EquipmentSettings settings = null;

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
    public EquipmentSettings getSettings(){
        if(settings == null) settings = modResourceManager.getEquipmentSettings(getName());
        return settings;
    }

    public String getName(){
        if(name == null) name = ResourceHelper.getResourceName(getRegistryName());
        return name;
    }
}
