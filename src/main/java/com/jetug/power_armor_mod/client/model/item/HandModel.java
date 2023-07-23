package com.jetug.power_armor_mod.client.model.item;

import com.jetug.power_armor_mod.common.data.constants.Global;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static com.jetug.power_armor_mod.common.data.constants.Resources.*;

@SuppressWarnings("rawtypes")
public class HandModel extends AnimatedGeoModel {
    @Override
    public ResourceLocation getModelLocation(Object object) {
        return HAND_MODEL_LOCATION;
    }

    @Override
    public ResourceLocation getTextureLocation(Object object) {
        return FRAME_TEXTURE_LOCATION;
    }

    @Override
    public ResourceLocation getAnimationFileLocation(Object animatable) {
        return HAND_ANIMATION_LOCATION;
    }
}
