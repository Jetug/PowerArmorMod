package com.jetug.chassis_core.client.model;

import com.jetug.chassis_core.ChassisCore;
import com.jetug.chassis_core.common.foundation.item.TestItem;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.resources.ResourceLocation;

public class TestModel extends GeoModel<TestItem> {
    @Override
    public ResourceLocation getModelResource(TestItem testItem) {
        return new ResourceLocation(ChassisCore.MOD_ID, "geo/test.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(TestItem testItem) {
        return new ResourceLocation(ChassisCore.MOD_ID, "textures/test.png");
    }

    @Override
    public ResourceLocation getAnimationResource(TestItem testItem) {
        return null;
    }
}
