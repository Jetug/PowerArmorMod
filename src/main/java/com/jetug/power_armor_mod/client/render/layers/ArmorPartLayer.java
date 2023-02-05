package com.jetug.power_armor_mod.client.render.layers;

import com.jetug.power_armor_mod.client.ClientConfig;
import com.jetug.power_armor_mod.common.minecraft.entity.PowerArmorEntity;
import com.jetug.power_armor_mod.common.util.constants.Global;
import com.jetug.power_armor_mod.common.util.enums.BodyPart;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

import static com.jetug.power_armor_mod.common.util.constants.Resources.POWER_ARMOR_MODEL_LOCATION;

public class ArmorPartLayer extends GeoLayerRenderer<PowerArmorEntity> {
    public BodyPart bodyPart;
    public ResourceLocation texture;

    public ArmorPartLayer(IGeoRenderer<PowerArmorEntity> entityRenderer, BodyPart bodyPart) {
        super(entityRenderer);
        var settings = ClientConfig.resourceManager.getPartSettings(bodyPart);
        this.texture = settings.getTexture();
        this.bodyPart = bodyPart;

    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, PowerArmorEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if(entity.hasArmor(bodyPart) && !entity.isInvisible()) {
            int overlay = OverlayTexture.NO_OVERLAY;
            RenderType cameo = RenderType.armorCutoutNoCull(texture);
            matrixStackIn.pushPose();
            matrixStackIn.scale(1.0f, 1.0f, 1.0f);
            matrixStackIn.translate(0.0d, 0.0d, 0.0d);
            this.getRenderer().render(this.getEntityModel().getModel(POWER_ARMOR_MODEL_LOCATION), entity, partialTicks, cameo, matrixStackIn,
                    bufferIn, bufferIn.getBuffer(cameo), packedLightIn, overlay, 1f, 1f, 1f, 1f);
            matrixStackIn.popPose();
        }
    }
}