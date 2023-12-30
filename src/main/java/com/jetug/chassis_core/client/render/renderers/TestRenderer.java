package com.jetug.chassis_core.client.render.renderers;

import com.jetug.chassis_core.client.model.TestModel;
import com.jetug.chassis_core.client.render.layers.PlayerSkinLayer;
import com.jetug.chassis_core.common.foundation.item.TestItem;
import mod.azure.azurelib.model.GeoModel;
import mod.azure.azurelib.renderer.GeoItemRenderer;

public class TestRenderer extends GeoItemRenderer<TestItem> {
    public TestRenderer() {
        super(new TestModel());
        addRenderLayer(new PlayerSkinLayer<>(this));
    }
}
