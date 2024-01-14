package com.jetug.chassis_core.client.render.layers;

import com.jetug.chassis_core.common.foundation.entity.WearableChassis;
import com.jetug.chassis_core.common.util.helpers.PlayerUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mod.azure.azurelib.cache.object.BakedGeoModel;
import mod.azure.azurelib.core.animatable.GeoAnimatable;
import mod.azure.azurelib.renderer.GeoRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;

public class PovLayer extends LayerBase {
    public PovLayer(GeoRenderer<WearableChassis> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack poseStack, GeoAnimatable animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        super.render(poseStack, animatable, bakedModel, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);

        var entity = PlayerUtils.getLocalPlayerChassis();
        if(entity == null) return;

        for(var part : entity.getPovEquipment()) {
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
}
