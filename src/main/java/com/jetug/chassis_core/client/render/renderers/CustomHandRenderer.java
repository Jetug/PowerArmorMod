package com.jetug.chassis_core.client.render.renderers;

import com.jetug.chassis_core.client.HandEntity;
import com.jetug.chassis_core.client.model.HandModel;
import com.jetug.chassis_core.client.render.layers.EquipmentLayer;
import com.jetug.chassis_core.common.data.enums.BodyPart;
import com.jetug.chassis_core.common.foundation.entity.WearableChassis;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.*;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.model.*;

import static com.jetug.chassis_core.client.render.utils.GeoUtils.renderEquipment;

@SuppressWarnings("unchecked")
public class CustomHandRenderer extends CustomGeoRenderer {
    private static CustomHandRenderer handRenderer;
    private static final HandModel handModel = new HandModel();

    static {
        AnimationController.addModelFetcher(animatable -> animatable instanceof HandEntity ? handModel : null);
    }

    private void initLayers(){
        addLayer(new EquipmentLayer(this, BodyPart.RIGHT_ARM_ARMOR));
    }

    public CustomHandRenderer(AnimatedGeoModel<HandEntity> model) {
        super(model);
        initLayers();
    }

    @Override
    public void render(WearableChassis chassisEntity, PoseStack poseStack,
                       @Nullable MultiBufferSource bufferSource, int packedLight) {
        super.render(chassisEntity, poseStack, bufferSource, packedLight);
        renderEquipment(handModel, chassisEntity, BodyPart.RIGHT_ARM_ARMOR, true);
    }

    public static void registerHandRenderer(){
        handRenderer = new CustomHandRenderer(handModel);
    }

    public static CustomHandRenderer getHandRenderer(){
        return handRenderer;
    }
}
