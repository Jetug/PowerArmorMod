package com.jetug.chassis_core.client.render.renderers;

import com.jetug.chassis_core.common.foundation.item.AnimatableItem;
import com.jetug.chassis_core.common.foundation.item.StackUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mod.azure.azurelib.cache.object.BakedGeoModel;
import mod.azure.azurelib.model.GeoModel;
import mod.azure.azurelib.renderer.GeoItemRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.item.ItemStack;

public class AnimatableItemRenderer<T extends AnimatableItem> extends GeoItemRenderer<T> {
    public AnimatableItemRenderer(GeoModel<T> model) {
        super(model);
    }

    @Override
    public void renderByItem(ItemStack stack, ItemTransforms.TransformType transformType, PoseStack poseStack,
                             MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        super.renderByItem(stack, transformType, poseStack, bufferSource, packedLight, packedOverlay);

        var allMods = animatable.getConfig().mods;
        var visibleMods = StackUtils.getMods(stack);

        for(var name : allMods)
            getGeoModel().getBone(name).ifPresent((bone) -> bone.setHidden(true));
        for(var name : visibleMods)
            getGeoModel().getBone(name).ifPresent((bone) -> bone.setHidden(false));
    }

    @Override
    public void preRender(PoseStack poseStack, T animatable, BakedGeoModel model, MultiBufferSource bufferSource,
                          VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay,
                          float red, float green, float blue, float alpha) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight,
                packedOverlay, red, green, blue, alpha);

    }
}
