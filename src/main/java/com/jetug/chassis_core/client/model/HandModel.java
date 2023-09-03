package com.jetug.chassis_core.client.model;

import com.jetug.chassis_core.client.render.utils.ResourceHelper;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static com.jetug.chassis_core.client.render.utils.ResourceHelper.*;
import static com.jetug.chassis_core.client.render.utils.ResourceHelper.getChassisResource;

@SuppressWarnings("rawtypes")
public class HandModel extends AnimatedGeoModel {
    @Override
    public ResourceLocation getModelLocation(Object object) {
        return getChassisResource("geo/hand/", "_hand.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(Object object) {
        //return ResourceHelper.getChassisResource("textures/hand/", "_hand.png");
        return getChassisResource("textures/entity/", ".png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(Object animatable) {
        return getChassisResource("animations/hand/", "_hand.animation.json");
    }
}
