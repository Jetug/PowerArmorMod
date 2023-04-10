package com.jetug.power_armor_mod.client.render.renderers.item;

import com.jetug.power_armor_mod.client.model.item.FramePartModel;
import com.jetug.power_armor_mod.client.model.item.HandModel;
import com.jetug.power_armor_mod.common.foundation.item.FramePartItem;
import com.jetug.power_armor_mod.common.foundation.item.HandItem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class FramePartRenderer extends GeoItemRenderer<FramePartItem> {
    public static final FramePartModel FRAME_PART_MODEL = new FramePartModel();

    public FramePartRenderer() {
        super(FRAME_PART_MODEL);
    }

    @Override
    public void render(FramePartItem animatable, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, ItemStack stack) {
        super.render(animatable, poseStack, bufferSource, packedLight, stack);
    }
}
