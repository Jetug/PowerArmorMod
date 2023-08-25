package com.jetug.power_armor_mod.client.render.renderers;

import com.jetug.power_armor_mod.client.model.HandModel;
import com.jetug.power_armor_mod.client.render.layers.EquipmentLayer;
import com.jetug.power_armor_mod.common.data.enums.BodyPart;
import com.jetug.power_armor_mod.common.foundation.entity.ArmorChassisEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.*;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.model.*;

import static com.jetug.power_armor_mod.client.render.renderers.ArmorChassisRenderer.*;

@SuppressWarnings("unchecked")
public class CustomHandRenderer extends CustomGeoRenderer<IAnimatable> {
    public static final String RIGHT_HAND_BONE = "right_lower_arm";
    private static CustomHandRenderer handRenderer;
    private static HandEntity handAmimator;
    private static final HandModel handModel = new HandModel();

    static {
        AnimationController.addModelFetcher(animatable -> animatable instanceof HandEntity ? handModel : null);
    }

    private void initLayers(){
        addLayer(new EquipmentLayer(this, BodyPart.RIGHT_ARM_ARMOR));
    }

    public CustomHandRenderer(AnimatedGeoModel<IAnimatable> model) {
        super(model);
        initLayers();
    }

    @Override
    public void render(IAnimatable animatable, ArmorChassisEntity chassisEntity, PoseStack poseStack,
                       @Nullable MultiBufferSource bufferSource, int packedLight) {
        super.render(animatable, chassisEntity, poseStack, bufferSource, packedLight);
        handleAttachment(handModel, chassisEntity, BodyPart.RIGHT_ARM_ARMOR);
    }

    public static void registerHandRenderer(){
        handRenderer = new CustomHandRenderer(handModel);
        handAmimator = new HandEntity();
    }

    public static CustomHandRenderer getHandRenderer(){
        return handRenderer;
    }

    public static HandEntity getHandAmimator(){
        return handAmimator;
    }
}
