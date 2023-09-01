package com.jetug.chassis_core.common.foundation.item;

import com.jetug.chassis_core.common.data.json.EquipmentSettings;
import com.jetug.chassis_core.common.data.enums.*;
import com.jetug.chassis_core.client.render.utils.ResourceHelper;
import net.minecraft.world.item.Item;

import javax.annotation.Nullable;

import static com.jetug.chassis_core.client.ClientConfig.*;

public class ChassisEquipment extends Item {
    private String name = null;
    private EquipmentSettings settings = null;

    public final BodyPart part;

    public ChassisEquipment(Properties pProperties, BodyPart part) {
        super(pProperties);
        this.part = part;
    }

//    public ChassisEquipment(BodyPart part) {
//        super(new Item.Properties().stacksTo(1));
//        this.part = part;
//    }

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
