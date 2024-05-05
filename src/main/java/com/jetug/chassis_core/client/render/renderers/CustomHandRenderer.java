package com.jetug.chassis_core.client.render.renderers;

import com.jetug.chassis_core.client.model.HandModel;
import com.jetug.chassis_core.client.render.utils.GeoUtils;
import com.jetug.chassis_core.common.foundation.entity.HandEntity;
import com.jetug.chassis_core.common.util.helpers.PlayerUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mod.azure.azurelib.cache.object.GeoBone;
import mod.azure.azurelib.model.GeoModel;
import mod.azure.azurelib.renderer.GeoObjectRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;

import java.util.ArrayList;
import java.util.Objects;

import static com.jetug.chassis_core.common.data.constants.ChassisPart.RIGHT_ARM_ARMOR;
import static com.jetug.chassis_core.common.foundation.entity.ChassisBase.getAsChassisEquipment;

@SuppressWarnings("unchecked")
public class CustomHandRenderer extends GeoObjectRenderer<HandEntity> {
    protected static final HandModel handModel = new HandModel();
    protected static CustomHandRenderer handRenderer;
    protected HandEntity currentChassis;

//    static {
//        AnimationController.addModelFetcher(animatable -> animatable instanceof HandEntity ? handModel : null);
//    }

    public CustomHandRenderer(GeoModel<HandEntity> model) {
        super(model);
        //addRenderLayer(new EquipmentLayer<>(this));
    }

    public static void doSafe(Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public void render(HandEntity animatable, PoseStack poseStack, MultiBufferSource bufferSource, float partialTick, int packedLight) {
//        currentChassis = animatable;
//        var texture = getTextureLocation(animatable);
//        if(texture == null) return;
////        //poseStack.pushPose();
////        poseStack.scale(1.0f, 1.0f, 1.0f);
////        poseStack.translate(0.0d, 0.0d, 0.0d);
//        super.render(animatable, poseStack, bufferSource, partialTick,  packedLight);
//        //poseStack.popPose();
//
//        //super.render(animatable, poseStack, bufferSource, packedLight);
//    }

//    @Override
//    public void renderRecursively(GeoBone bone, PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
//        super.renderRecursively(bone, poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
//    }

//    @Override
//    public void renderChildBones(GeoBone bone, PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay,
//                                 float red, float green, float blue, float alpha) {
//        if (bone.childBonesAreHiddenToo())
//            return;
//
//        doSafe(() -> {
//            var bonesToRender = new ArrayList<>(bone.childBones);
//            var equipmentBones = currentChassis.getEquipmentBones(bone.name);
//            bonesToRender.addAll(equipmentBones);
//
//            for (GeoBone childBone : bonesToRender) {
//                renderRecursively(childBone, poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
//            }
//        });
//    }

    public static void registerHandRenderer() {
        handRenderer = new CustomHandRenderer(handModel);
    }

    public static CustomHandRenderer getHandRenderer() {
        return handRenderer;
    }

    @Override
    public void renderRecursively(PoseStack poseStack, HandEntity animatable, GeoBone bone,
                                  RenderType renderType, MultiBufferSource bufferSource,
                                  VertexConsumer buffer, boolean isReRender, float partialTick,
                                  int packedLight, int packedOverlay,
                                  float red, float green, float blue, float alpha) {

        if (Objects.equals(bone.getName(), "right_arm_pov")) {
            bone.setRotX(((float) Math.PI / 90));
        }

        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer,
                isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void renderChildBones(PoseStack poseStack, HandEntity animatable, GeoBone bone, RenderType renderType,
                                 MultiBufferSource bufferSource, VertexConsumer buffer,
                                 boolean isReRender, float partialTick, int packedLight, int packedOverlay,
                                 float red, float green, float blue, float alpha) {
        if (!bone.isHidingChildren()) {
            var bonesToRender = new ArrayList<>(bone.getChildBones());

            if(PlayerUtils.isLocalWearingChassis() && Objects.equals(bone.getName(), "right_arm_pov")){
                var chassis = PlayerUtils.getLocalPlayerChassis();
                if(chassis.isEquipmentVisible(RIGHT_ARM_ARMOR)){
                    var armor = getAsChassisEquipment(chassis.getEquipment(RIGHT_ARM_ARMOR));
                    var config = armor.getConfig();
                    var armorBone = GeoUtils.getBone(config.getModelLocation(), "pov_right_forearm_armor");
                    bonesToRender.add(armorBone);
                }
            }

            for (GeoBone childBone : bonesToRender) {
                this.renderRecursively(poseStack, animatable, childBone, renderType, bufferSource, buffer,
                        isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
            }
        }
    }
}
