package com.jetug.power_armor_mod.client.model;

import com.jetug.power_armor_mod.common.entity.entitytype.PowerArmorEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static com.jetug.power_armor_mod.common.util.constants.Resources.*;

public class HelmetModel <Type extends PowerArmorEntity & IAnimatable> extends AnimatedGeoModel<Type> {
    @Override
    public ResourceLocation getModelLocation(Type object) {
        return HELMET_MODEL_LOCATION;
    }

    @Override
    public ResourceLocation getTextureLocation(Type object) {
        return HELMET_TEXTURE_LOCATION;
    }

    @Override
    public ResourceLocation getAnimationFileLocation(Type object) {
        return null;
    }
}
