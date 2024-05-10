package com.jetug.chassis_core.client.render.renderers;

import com.jetug.chassis_core.client.model.HandModel;
import com.jetug.chassis_core.client.render.layers.EquipmentLayer;
import com.jetug.chassis_core.client.render.layers.HandEquipmentLayer;
import com.jetug.chassis_core.client.render.utils.GeoUtils;
import com.jetug.chassis_core.client.utils.Rgba;
import com.jetug.chassis_core.common.foundation.entity.HandEntity;
import com.jetug.chassis_core.common.util.helpers.PlayerUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mod.azure.azurelib.cache.object.GeoBone;
import mod.azure.azurelib.cache.object.GeoCube;
import mod.azure.azurelib.cache.object.GeoQuad;
import mod.azure.azurelib.model.GeoModel;
import mod.azure.azurelib.renderer.GeoObjectRenderer;
import mod.azure.azurelib.renderer.GeoRenderer;
import mod.azure.azurelib.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.phys.Vec3;
import org.checkerframework.checker.units.qual.C;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Objects;

import static com.jetug.chassis_core.common.data.constants.ChassisPart.RIGHT_ARM_ARMOR;
import static com.jetug.chassis_core.common.foundation.entity.ChassisBase.getAsChassisEquipment;

@SuppressWarnings("unchecked")
public class CustomHandRenderer extends GeoObjectRenderer<HandEntity> {
    protected static final HandModel handModel = new HandModel();
    public static final String RIGHT_HAND_BONE = "right_arm_pov";
    protected static CustomHandRenderer handRenderer;
    protected HandEntity currentChassis;

//    static {
//        AnimationController.addModelFetcher(animatable -> animatable instanceof HandEntity ? handModel : null);
//    }

    public CustomHandRenderer(GeoModel<HandEntity> model) {
        super(model);
        addRenderLayer(new HandEquipmentLayer<>(this));
    }

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

        if (Objects.equals(bone.getName(), RIGHT_HAND_BONE)) {
            bone.setRotX(((float) Math.PI / 90));
        }

        if(PlayerUtils.isLocalWearingChassis() && Objects.equals(bone.getName(), RIGHT_HAND_BONE)){
            var chassis = PlayerUtils.getLocalPlayerChassis();
            if(chassis.isEquipmentVisible(RIGHT_ARM_ARMOR)){
                var armor = getAsChassisEquipment(chassis.getEquipment(RIGHT_ARM_ARMOR));
                var config = armor.getConfig();
                var armorBone = GeoUtils.getBone(config.getModelLocation(), "right_forearm_armor");

                if(armorBone == null) return;

                poseStack.pushPose();
                {
                    armorBone.updatePivot(bone.getPivotX(), bone.getPivotY(), bone.getPivotZ());
                    RenderUtils.translateMatrixToBone(poseStack, bone);
                    RenderUtils.translateToPivotPoint(poseStack, bone);
                    RenderUtils.rotateMatrixAroundBone(poseStack, bone);
//                RenderUtils.scaleMatrixForBone(poseStack, bone);
                    RenderUtils.translateAwayFromPivotPoint(poseStack, bone);

//                    poseStack.translate((8.8 - 5) / 16f, (-22.36776 - 5) / 16f, (-3.53463) / 16f);
//                poseStack.translate(-5 / 16f, 0 / 16f, 0 / 16f);

                    for (var cube : armorBone.getCubes()) {
                        poseStack.pushPose();

                        var newCube = new GeoCube(cube.quads(), new Vec3(0,0,0),
                                cube.rotation(), cube.size(), cube.inflate(), cube.mirror());

                        renderCube(poseStack, newCube, buffer, packedLight, packedOverlay, red, green, blue, alpha);
                        poseStack.popPose();
                    }

//                    renderRecursively(poseStack, animatable, armorBone, renderType, bufferSource, buffer,
//                            isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
                }
                poseStack.popPose();
            }
        }
//        else {
//            super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer,
//                    isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
//        }
        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer,
                    isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);

//        customRenderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer,
//                isReRender, partialTick, packedLight, packedOverlay, new Rgba(red, green, blue, alpha));
    }

    public void customRenderRecursively(PoseStack poseStack, HandEntity animatable, GeoBone bone, RenderType renderType,
                                        MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender,
                                        float partialTick, int packedLight, int packedOverlay, Rgba rgba) {
//        if (bone.isTrackingMatrices()) {
//            var poseState = new Matrix4f(poseStack.last().pose());
//            var localMatrix = RenderUtils.invertAndMultiplyMatrices(poseState, this.objectRenderTranslations);
//
//            bone.setModelSpaceMatrix(RenderUtils.invertAndMultiplyMatrices(poseState, this.modelRenderTranslations));
//            bone.setLocalSpaceMatrix(RenderUtils.translateMatrix(localMatrix, getRenderOffset(this.animatable, 1).toVector3f()));
//        }

        originalRenderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer,
                isReRender, partialTick, packedLight, packedOverlay, rgba);
    }

    private void originalRenderRecursively(PoseStack poseStack, HandEntity animatable, GeoBone bone, RenderType renderType,
                                           MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender,
                                           float partialTick, int packedLight, int packedOverlay, Rgba rgba) {
        poseStack.pushPose();
//        RenderUtils.prepMatrixForBone(poseStack, bone);
        renderCubesOfBone(poseStack, bone, buffer, packedLight, packedOverlay, rgba.r(), rgba.g(), rgba.b(), rgba.a());

        if (!isReRender) applyRenderLayersForBone(poseStack, getAnimatable(), bone, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);

        renderChildBones(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick,
                packedLight, packedOverlay, rgba.r(), rgba.g(), rgba.b(), rgba.a());
        poseStack.popPose();
    }

    @Override
    public void renderCubesOfBone(PoseStack poseStack, GeoBone bone, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (bone.isHidden())
            return;

        for (var cube : bone.getCubes()) {
            poseStack.pushPose();

            var newCube = new GeoCube(cube.quads(), new Vec3(0,0,0),
                    cube.rotation(), cube.size(), cube.inflate(), cube.mirror());

            renderCube(poseStack, newCube, buffer, packedLight, packedOverlay, red, green, blue, alpha);
            poseStack.popPose();
        }
    }

//    @Override
//    public void renderCube(PoseStack poseStack, GeoCube cube, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
////        RenderUtils.translateToPivotPoint(poseStack, cube);
////        RenderUtils.rotateMatrixAroundCube(poseStack, cube);
////        RenderUtils.translateAwayFromPivotPoint(poseStack, cube);
//
//        var normalisedPoseState = poseStack.last().normal();
//        var poseState = new Matrix4f(poseStack.last().pose());
//
//        for (var quad : cube.quads()) {
//            if (quad == null) continue;
//
//            var normal = normalisedPoseState.transform(new Vector3f(quad.normal()));
//
//            RenderUtils.fixInvertedFlatCube(cube, normal);
//            createVerticesOfQuad(quad, poseState, normal, buffer, packedLight, packedOverlay, red, green, blue, alpha);
//        }
//    }

    //    @Override
//    public void renderChildBones(PoseStack poseStack, HandEntity animatable, GeoBone bone, RenderType renderType,
//                                 MultiBufferSource bufferSource, VertexConsumer buffer,
//                                 boolean isReRender, float partialTick, int packedLight, int packedOverlay,
//                                 float red, float green, float blue, float alpha) {
//        if (!bone.isHidingChildren()) {
//            var bonesToRender = new ArrayList<>(bone.getChildBones());
//
//            if(PlayerUtils.isLocalWearingChassis() && Objects.equals(bone.getName(), RIGHT_HAND_BONE)){
//                var chassis = PlayerUtils.getLocalPlayerChassis();
//                if(chassis.isEquipmentVisible(RIGHT_ARM_ARMOR)){
//                    var armor = getAsChassisEquipment(chassis.getEquipment(RIGHT_ARM_ARMOR));
//                    var config = armor.getConfig();
//                    var armorBone = GeoUtils.getBone(config.getModelLocation(), "pov_right_forearm_armor");
//                    bonesToRender.add(armorBone);
//                }
//            }
//
//            for (GeoBone childBone : bonesToRender) {
//                this.renderRecursively(poseStack, animatable, childBone, renderType, bufferSource, buffer,
//                        isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
//            }
//        }
//    }
}
