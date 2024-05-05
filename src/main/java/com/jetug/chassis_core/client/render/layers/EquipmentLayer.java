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
        for (var part : entity.getEquipment()) {
            if (entity.isEquipmentVisible(part)) {
                var stack = entity.getEquipment(part);
                var item = entity.getEquipmentItem(part);

                if (item.getConfig() == null) return;
                var texture = item.getTexture(stack);

                if (texture == null) return;
                renderLayer(poseStack, entity, bakedModel, bufferSource, partialTick, packedLight, texture);
            }
        }
    }
}