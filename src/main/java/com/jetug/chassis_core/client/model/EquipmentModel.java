package com.jetug.chassis_core.client.model;

import com.jetug.chassis_core.common.data.json.EquipmentConfig;
import com.jetug.chassis_core.common.foundation.item.ChassisEquipment;
import com.jetug.chassis_core.common.foundation.item.StackUtils;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.resources.ResourceLocation;

public class EquipmentModel<T extends ChassisEquipment> extends GeoModel<T> {
    @Override
    public ResourceLocation getModelResource(T item) {
        return item.getConfig().getModelLocation();
    }

    @Override
    public ResourceLocation getTextureResource(T item) {
        return item.getConfig().getTextureLocation(StackUtils.DEFAULT);
    }

    @Override
    public ResourceLocation getAnimationResource(T animatable) {
        return null;
    }
}