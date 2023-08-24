package com.jetug.power_armor_mod.client.model;

import com.jetug.power_armor_mod.common.data.constants.Global;
import com.jetug.power_armor_mod.common.util.extensions.PlayerExtension;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static com.jetug.generated.resources.Textures.*;
import static com.jetug.power_armor_mod.common.data.constants.Resources.*;

@SuppressWarnings("rawtypes")
public class HandModel extends AnimatedGeoModel {
    @Override
    public ResourceLocation getModelLocation(Object object) {
        var chassis = PlayerExtension.getPlayerChassis();
        var name = chassis.getRegistryName();
        return new ResourceLocation(Global.MOD_ID, "geo/hand/" + name + "_hand.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(Object object) {
        return ENTITY_ARMOR_CHASSIS;
    }

    @Override
    public ResourceLocation getAnimationFileLocation(Object animatable) {
        return HAND_ANIMATION_LOCATION;
    }
}
