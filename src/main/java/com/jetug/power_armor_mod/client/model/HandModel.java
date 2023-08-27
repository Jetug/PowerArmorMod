package com.jetug.power_armor_mod.client.model;

import com.jetug.power_armor_mod.common.util.extensions.PlayerExtension;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static com.jetug.power_armor_mod.client.render.utils.ResourceHelper.getResourceLocation;

@SuppressWarnings("rawtypes")
public class HandModel extends AnimatedGeoModel {
    @Override
    public ResourceLocation getModelLocation(Object object) {
        return getResourceLocation("geo/hand/", "_hand.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(Object object) {
        var res = getResourceLocation("textures/hand/", "_hand.png");
        return res;
    }

    @Override
    public ResourceLocation getAnimationFileLocation(Object animatable) {
        return getResourceLocation("animations/hand/", "_hand.animation.json");
    }
}
