package com.jetug.power_armor_mod.client.render.renderers.item;

import com.jetug.power_armor_mod.client.model.item.DrillModel;
import com.jetug.power_armor_mod.common.foundation.item.DrillItem;
import com.jetug.power_armor_mod.common.foundation.item.HandItem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

import static com.jetug.power_armor_mod.client.render.renderers.CustomHandRenderer.HAND_MODEL;

public class DrillRenderer extends GeoItemRenderer<DrillItem> {

    public DrillRenderer() {
        super(new DrillModel());
    }

    @Override
    public void render(DrillItem animatable, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, ItemStack stack) {
        super.render(animatable, poseStack, bufferSource, packedLight, stack);
    }
}
