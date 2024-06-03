package com.jetug.chassis_core.client.render.renderers;

import com.jetug.chassis_core.client.model.HandModel;
import com.jetug.chassis_core.client.render.layers.HandEquipmentLayer;
import com.jetug.chassis_core.client.render.utils.GeoUtils;
import com.jetug.chassis_core.common.foundation.entity.HandEntity;
import com.jetug.chassis_core.common.util.helpers.PlayerUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mod.azure.azurelib.cache.object.GeoBone;
import mod.azure.azurelib.cache.object.GeoCube;
import mod.azure.azurelib.model.GeoModel;
import mod.azure.azurelib.renderer.GeoObjectRenderer;
import mod.azure.azurelib.util.RenderUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.phys.Vec3;
import java.util.Objects;

import static com.jetug.chassis_core.common.data.constants.ChassisPart.*;
import static com.jetug.chassis_core.common.foundation.entity.ChassisBase.*;

@SuppressWarnings("unchecked")
public class CustomHandRenderer extends GeoObjectRenderer<HandEntity> {
    protected static final HandModel handModel = new HandModel();
    public static final String RIGHT_HAND_BONE = "right_arm_pov";
    protected static CustomHandRenderer handRenderer;

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
        if(PlayerUtils.isLocalWearingChassis() && Objects.equals(bone.getName(), RIGHT_HAND_BONE)){
            var chassis = PlayerUtils.getLocalPlayerChassis();
            if(chassis.isEquipmentVisible(RIGHT_ARM_ARMOR)){
                var armor = getAsChassisEquipment(chassis.getEquipment(RIGHT_ARM_ARMOR));
                if(armor.getConfig() == null) return;
                var armorBone = GeoUtils.getBone(armor.getConfig().getModelLocation(), "right_forearm_armor");
                if(armorBone == null) return;

                poseStack.pushPose();
                {
//                    armorBone.updatePivot(bone.getPivotX(), bone.getPivotY(), bone.getPivotZ());
//                    RenderUtils.translateMatrixToBone(poseStack, bone);
//                    RenderUtils.translateToPivotPoint(poseStack, bone);
                    RenderUtils.rotateMatrixAroundBone(poseStack, bone);
////                RenderUtils.scaleMatrixForBone(poseStack, bone);
//                    RenderUtils.translateAwayFromPivotPoint(poseStack, bone);

//                    poseStack.translate((armorBone.getPivotX() + 0.8 + X) / 16f, (-armorBone.getPivotY() + 0.35 + Y) / 16f, (armorBone.getPivotZ() - 1.05 + Z) / 16f);
                    poseStack.translate(
                            ( armorBone.getPivotX() + 0.8 ) / 16f,
                            (-armorBone.getPivotY() + 0.35) / 16f,
                            ( armorBone.getPivotZ() - 1.05) / 16f);

                    for (var cube : armorBone.getCubes()) {
                        poseStack.pushPose();
                        {
                            var newCube = new GeoCube(cube.quads(), new Vec3(0, 0, 0),
                                    cube.rotation(), cube.size(), cube.inflate(), cube.mirror());
                            renderCube(poseStack, newCube, buffer, packedLight, packedOverlay, red, green, blue, alpha);
                        }
                        poseStack.popPose();
                    }
                }
                poseStack.popPose();
            }
        }
        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer,
                    isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

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
