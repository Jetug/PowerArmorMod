package com.jetug.chassis_core.client.render.layers;

import com.jetug.chassis_core.common.foundation.entity.WearableChassis;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mod.azure.azurelib.cache.object.BakedGeoModel;
import mod.azure.azurelib.renderer.GeoRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;

@SuppressWarnings("ConstantConditions")
public class EquipmentLayer<T extends WearableChassis> extends LayerBase<T> {
    public EquipmentLayer(GeoRenderer<T> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack poseStack, T entity, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource,
                       VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        for(var part : entity.getEquipment()) {
            if (entity.isEquipmentVisible(part)) {
                var stack = entity.getEquipment(part);
                var item = entity.getEquipmentItem(part);

                if (item.getConfig() == null) return;
                var texture = item.getTexture(stack);

                if (texture == null) return;
//                    render(poseStack, bufferSource, packedLight, entity, partialTick, texture);
                renderLayer(poseStack, entity, bakedModel, bufferSource, partialTick, packedLight, texture);
            }
        }
    }



//
//    @Override
//    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn,
//                       int packedLightIn, T entity,
//                       float limbSwing, float limbSwingAmount, float partialTicks,
//                       float ageInTicks, float netHeadYaw, float headPitch) {
//
//        for(var part : entity.getEquipment()) {
//            if (entity.isEquipmentVisible(part)) {
//                var stack = entity.getEquipment(part);
//                var item = entity.getEquipmentItem(part);
//
//                if (item.getConfig() == null) return;
//                var texture = item.getTexture(stack);
//
//                if (texture != null)
//                    render(matrixStackIn, bufferIn, packedLightIn, entity, partialTicks, texture);
//            }
//        }
//    }
//AutoGlowingGeoLayer
//    private void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight,
//                        T entity, float partialTicks, ResourceLocation texture) {
//        int overlay = OverlayTexture.NO_OVERLAY;
//        var renderType = RenderType.armorCutoutNoCull(texture);
//        poseStack.pushPose();
//        poseStack.scale(1.0f, 1.0f, 1.0f);
//        poseStack.translate(0.0d, 0.0d, 0.0d);
//        var model = getRenderer().getGeoModel().getModelResource(entity);
//        this.getRenderer().defaultRender(poseStack,entity,
//                        buffer, renderType, buffer.getBuffer(renderType),0f, partialTicks, packedLight)
////                .render(this.getEntityModel().getModel(model), entity, partialTicks, renderType, poseStack,
////                        buffer, buffer.getBuffer(renderType), packedLight, overlay, 1f, 1f, 1f, 1f);
//        poseStack.popPose();
//    }
//
//    public void renderLayer(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, RenderType renderType,
//                       MultiBufferSource bufferSource, VertexConsumer buffer,
//                       float partialTick, int packedLight, int packedOverlay) {
//        this.getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable,
//                renderType, bufferSource.getBuffer(renderType), partialTick,
//                packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
//    }

}