package com.jetug.power_armor_mod.client.render.renderers;

import com.jetug.power_armor_mod.common.minecraft.entity.GeckoEntity;
import com.jetug.power_armor_mod.client.model.GeckoModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class GeckoRenderer extends GeoEntityRenderer<GeckoEntity> {
    public GeckoRenderer(EntityRendererManager renderManager) {
        super(renderManager, new GeckoModel());
    }
}
