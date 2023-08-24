package com.jetug.power_armor_mod.client.render.renderers;

import com.jetug.power_armor_mod.client.model.ArmorChassisModel;
import com.jetug.power_armor_mod.common.foundation.entity.ArmorChassisEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class ArmorRenderer extends ModGeoRenderer<ArmorChassisEntity> {
    public static ArmorChassisModel<ArmorChassisEntity> armorChassisModel;

    public ArmorRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ArmorChassisModel<>());
        armorChassisModel = (ArmorChassisModel<ArmorChassisEntity>)getGeoModelProvider();
    }

    @Override
    public void render(ArmorChassisEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}