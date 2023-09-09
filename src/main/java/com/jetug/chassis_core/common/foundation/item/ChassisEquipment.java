package com.jetug.chassis_core.common.foundation.item;

import com.jetug.chassis_core.common.data.json.EquipmentConfig;
import com.jetug.chassis_core.client.render.utils.ResourceHelper;
import net.minecraft.world.item.Item;

import javax.annotation.Nullable;

import static com.jetug.chassis_core.client.ClientConfig.*;

public class ChassisEquipment extends Item {
    private String name = null;
    private EquipmentConfig settings = null;
    public final String part;

    public ChassisEquipment(Properties pProperties, String part) {
        super(pProperties);
        this.part = part;
    }

    @Nullable
    public EquipmentConfig getSettings(){
        if(settings == null) settings = modResourceManager.getEquipmentSettings(getName());
        return settings;
    }

    public String getName(){
        if(name == null) name = ResourceHelper.getResourceName(getRegistryName());
        return name;
    }
}
