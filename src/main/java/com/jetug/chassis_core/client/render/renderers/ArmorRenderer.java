package com.jetug.chassis_core.client.render.renderers;

import com.jetug.chassis_core.client.model.ArmorChassisModel;
import com.jetug.chassis_core.common.foundation.entity.WearableChassis;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class ArmorRenderer extends ModGeoRenderer<WearableChassis> {
    public static ArmorChassisModel<WearableChassis> armorChassisModel;

    public ArmorRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ArmorChassisModel<>());
        armorChassisModel = (ArmorChassisModel<WearableChassis>)getGeoModelProvider();
    }

    @Override
    public void render(WearableChassis entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}