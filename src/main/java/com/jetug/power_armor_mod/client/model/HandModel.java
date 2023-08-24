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
        return getResourceLocation("geo/hand/", "_hand.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(Object object) {
        return getResourceLocation("textures/hand/", "_hand.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(Object animatable) {
        return getResourceLocation("animations/hand/", "_hand.animation.json");
    }

    private ResourceLocation getResourceLocation(String path, String extension){
        var chassis = PlayerExtension.getPlayerChassis();
        var name = chassis.getRegistryName();
        var resourceLocation = chassis.getType().getRegistryName();

        return new ResourceLocation(resourceLocation.getNamespace(), path + name + extension);
    }
}
