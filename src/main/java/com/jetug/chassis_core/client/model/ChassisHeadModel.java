package com.jetug.chassis_core.client.model;

import com.jetug.chassis_core.common.data.constants.Resources;
import com.jetug.chassis_core.common.foundation.entity.WearableChassis;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;

public class ChassisHeadModel extends GeoModel<WearableChassis> {
    @Override
    public ResourceLocation getModelResource(WearableChassis geoAnimatable) {
        return Resources.resourceLocation("geo/head.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(WearableChassis geoAnimatable) {
        if (geoAnimatable.hasPlayerPassenger())
            return ((AbstractClientPlayer) geoAnimatable.getPlayerPassenger()).getSkinTextureLocation();
        return Minecraft.getInstance().player.getSkinTextureLocation();
    }

    @Override
    public ResourceLocation getAnimationResource(WearableChassis geoAnimatable) {
        return null;
    }
}