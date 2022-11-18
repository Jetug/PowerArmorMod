package com.jetug.power_armor_mod.common.capability.data;

import com.jetug.power_armor_mod.common.capability.TEST.IArmorData;
import net.minecraft.world.entity.Entity;

import static com.jetug.power_armor_mod.common.capability.Capabilities.ARMOR_DATA;

public class DataManager {
    public static IArmorData getPowerArmorPartData(Entity entity){
        return entity.getCapability(ARMOR_DATA, null).orElse(null);
    }

}
