package com.jetug.chassis_core.client.model;

import com.jetug.chassis_core.client.render.utils.ResourceHelper;
import com.jetug.chassis_core.common.foundation.item.ChassisEquipment;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static com.jetug.chassis_core.client.render.utils.ResourceHelper.getChassisResource;

public class EquipmentModel<T extends ChassisEquipment> extends AnimatedGeoModel<T> {
    @Override
    public ResourceLocation getModelLocation(T item) {
        return item.getConfig().getModelLocation();
    }

    @Override
    public ResourceLocation getTextureLocation(T item) {
        return item.getConfig().getTextureLocation();
    }

    @Override
    public ResourceLocation getAnimationFileLocation(T animatable) {
        return null;
    }
}