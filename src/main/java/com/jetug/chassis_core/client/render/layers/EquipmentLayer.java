package com.jetug.chassis_core.client.render.layers;

import com.jetug.chassis_core.common.foundation.entity.WearableChassis;
import com.jetug.chassis_core.common.data.enums.*;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

@SuppressWarnings("ConstantConditions")
public class EquipmentLayer<T extends WearableChassis> extends GeoLayerRenderer<T> {
    private final IGeoRenderer entityRenderer;
    public BodyPart bodyPart;

    public EquipmentLayer(IGeoRenderer entityRenderer, BodyPart bodyPart) {
        super(entityRenderer);
        this.entityRenderer = entityRenderer;
        this.bodyPart = bodyPart;
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn,
                       int packedLightIn, T entity,
                       float limbSwing, float limbSwingAmount, float partialTicks,
                       float ageInTicks, float netHeadYaw, float headPitch) {
        if(entity.isEquipmentVisible(bodyPart)) {
            var item = entity.getEquipmentItem(bodyPart);
            if (item.getSettings() == null) return;
            var texture = item.getSettings().getTextureLocation();
            render(matrixStackIn, bufferIn, packedLightIn, entity, partialTicks, texture);
        }
    }

    private void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn,
                        T entity, float partialTicks, ResourceLocation texture) {
        int overlay = OverlayTexture.NO_OVERLAY;
        RenderType cameo = RenderType.armorCutoutNoCull(texture);
        matrixStackIn.pushPose();
        matrixStackIn.scale(1.0f, 1.0f, 1.0f);
        matrixStackIn.translate(0.0d, 0.0d, 0.0d);
        var model = entityRenderer.getGeoModelProvider().getModelLocation(entity);
        this.getRenderer().render(this.getEntityModel().getModel(model), entity, partialTicks, cameo, matrixStackIn,
                bufferIn, bufferIn.getBuffer(cameo), packedLightIn, overlay, 1f, 1f, 1f, 1f);
        matrixStackIn.popPose();
    }
}