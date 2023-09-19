package com.jetug.chassis_core.client.render.renderers;

import com.jetug.chassis_core.common.data.enums.ChassisPart;
import com.jetug.chassis_core.common.foundation.entity.HandEntity;
import com.jetug.chassis_core.client.model.HandModel;
import com.jetug.chassis_core.client.render.layers.EquipmentLayer;
import com.jetug.chassis_core.common.foundation.entity.WearableChassis;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.model.*;

import java.util.ArrayList;

import static com.jetug.chassis_core.client.render.utils.GeoUtils.*;

@SuppressWarnings("unchecked")
public class CustomHandRenderer extends CustomGeoRenderer {
    protected static CustomHandRenderer handRenderer;
    protected static final HandModel handModel = new HandModel();
    protected WearableChassis currentChassis;

    static {
        AnimationController.addModelFetcher(animatable -> animatable instanceof HandEntity ? handModel : null);
    }

    public CustomHandRenderer(AnimatedGeoModel<HandEntity> model) {
        super(model);
        addLayer(new EquipmentLayer<>(this));
    }

    @Override
    public void render(WearableChassis chassisEntity, PoseStack poseStack,
                       @Nullable MultiBufferSource bufferSource, int packedLight) {
        currentChassis = chassisEntity;
        super.render(chassisEntity, poseStack, bufferSource, packedLight);
    }

    @Override
    public void renderRecursively(GeoBone bone, PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.renderRecursively(bone, poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void renderChildBones(GeoBone bone, PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay,
                                 float red, float green, float blue, float alpha) {
        if (bone.childBonesAreHiddenToo())
            return;

        var bonesToRender = new ArrayList<>(bone.childBones);
        bonesToRender.addAll(getEquipmentBones(bone.name, currentChassis));

        for (GeoBone childBone : bonesToRender) {
            renderRecursively(childBone, poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        }
    }

    public static void registerHandRenderer(){
        handRenderer = new CustomHandRenderer(handModel);
    }

    public static CustomHandRenderer getHandRenderer(){
        return handRenderer;
    }
}
