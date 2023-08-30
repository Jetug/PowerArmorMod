package com.jetug.chassis_core.client.render.renderers.item;

import com.jetug.chassis_core.client.model.item.DrillModel;
import com.jetug.chassis_core.common.foundation.item.DrillItem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class DrillRenderer extends GeoItemRenderer<DrillItem> {

    public DrillRenderer() {
        super(new DrillModel());
    }

    @Override
    public void render(DrillItem animatable, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, ItemStack stack) {
        super.render(animatable, poseStack, bufferSource, packedLight, stack);
    }
}
