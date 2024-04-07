package com.jetug.chassis_core.client.render.renderers;

import com.jetug.chassis_core.client.model.ChassisHeadModel;
import com.jetug.chassis_core.common.foundation.entity.WearableChassis;
import mod.azure.azurelib.renderer.GeoObjectRenderer;

public class ChassisHeadRenderer extends GeoObjectRenderer<WearableChassis> {
    public ChassisHeadRenderer() {
        super(new ChassisHeadModel());
    }
}
