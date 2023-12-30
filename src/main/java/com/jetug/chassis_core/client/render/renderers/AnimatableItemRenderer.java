package com.jetug.chassis_core.client.render.renderers;

import com.jetug.chassis_core.common.foundation.item.CustomizableItem;
import com.jetug.chassis_core.common.foundation.item.IConfigProvider;
import com.jetug.chassis_core.common.foundation.item.StackUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mod.azure.azurelib.cache.object.BakedGeoModel;
import mod.azure.azurelib.core.animatable.GeoAnimatable;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class AnimatableItemRenderer<T extends IConfigProvider & GeoAnimatable> extends GeoItemEntityRenderer<T> {
    public AnimatableItemRenderer(GeoModel<T> model) {
        super(model);
    }

//    @Override
//    public void renderByItem(ItemStack stack, ItemTransforms.TransformType transformType, PoseStack poseStack,
//                             MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
//        var item = (T)stack.getItem();
//        renderAttachments(stack, item);
//        super.renderByItem(stack, transformType, poseStack, bufferSource, packedLight, packedOverlay);
//    }

    @Override
    public void render(ItemStack stack, ItemTransforms.TransformType transformType,
                       PoseStack poseStack, T animatable, @Nullable MultiBufferSource bufferSource,
                       @Nullable RenderType renderType, @Nullable VertexConsumer buffer, int packedLight) {
        renderAttachments(stack, animatable);
        super.render(stack, transformType, poseStack, animatable, bufferSource, renderType, buffer, packedLight);
    }

    protected void renderAttachments(ItemStack stack, T item) {
        var config = item.getConfig();

        if(config != null) {
            var allMods = config.mods;
            var visibleMods = StackUtils.getAttachments(stack);

            for (var name : allMods)
                getGeoModel().getBone(name).ifPresent((bone) -> bone.setHidden(true));
            for (var name : visibleMods)
                getGeoModel().getBone(name).ifPresent((bone) -> bone.setHidden(false));
        }
    }

    @Override
    public void preRender(PoseStack poseStack, T animatable, BakedGeoModel model, MultiBufferSource bufferSource,
                          VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay,
                          float red, float green, float blue, float alpha) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight,
                packedOverlay, red, green, blue, alpha);

    }
}