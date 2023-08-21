package com.jetug.power_armor_mod.client.render.renderers;

import com.jetug.power_armor_mod.client.model.PowerArmorModel;
import com.jetug.power_armor_mod.common.foundation.entity.PowerArmorEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class ArmorRenderer extends ModGeoRenderer<PowerArmorEntity> {
    public static PowerArmorModel<PowerArmorEntity> powerArmorModel;

    public ArmorRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new PowerArmorModel<>());
        powerArmorModel = (PowerArmorModel<PowerArmorEntity>)getGeoModelProvider();
    }

    @Override
    public void render(PowerArmorEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}