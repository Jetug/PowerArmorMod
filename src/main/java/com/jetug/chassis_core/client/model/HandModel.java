package com.jetug.chassis_core.client.model;

import com.jetug.chassis_core.client.render.utils.ResourceHelper;
import mod.azure.azurelib.core.animatable.GeoAnimatable;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.resources.ResourceLocation;

import static com.jetug.chassis_core.client.render.utils.ResourceHelper.getChassisResource;

@SuppressWarnings("rawtypes")
public class HandModel extends GeoModel {
    @Override
    public ResourceLocation getModelResource(GeoAnimatable geoAnimatable) {
        return getChassisResource("geo/hand/", "_hand.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GeoAnimatable geoAnimatable) {
        return ResourceHelper.getChassisResource("textures/hand/", "_hand.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GeoAnimatable geoAnimatable) {
        return getChassisResource("animations/hand/", "_hand.animation.json");
    }
}
