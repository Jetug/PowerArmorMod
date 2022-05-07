package com.jetug.begining.client.render;

import com.jetug.begining.common.entity.entity_type.GeckoEntity;
import com.jetug.begining.client.model.GeckoModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class GeckoRenderer extends GeoEntityRenderer<GeckoEntity> {
    public GeckoRenderer(EntityRendererManager renderManager) {
        super(renderManager, new GeckoModel());
    }
}
