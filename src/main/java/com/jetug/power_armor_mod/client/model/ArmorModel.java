package com.jetug.power_armor_mod.client.model;

import com.jetug.power_armor_mod.common.foundation.entity.PowerArmorEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static com.jetug.power_armor_mod.common.data.constants.Resources.*;

public class ArmorModel <Type extends PowerArmorEntity & IAnimatable> extends AnimatedGeoModel<Type> {
    @Override
    public ResourceLocation getModelLocation(Type object) {
        return ARMOR_MODEL_LOCATION;
    }

    @Override
    public ResourceLocation getTextureLocation(Type object) {
        return ARMOR_TEXTURE_LOCATION;
    }

    @Override
    public ResourceLocation getAnimationFileLocation(Type object) {
        return ARMOR_ANIMATION_LOCATION;
    }
}
