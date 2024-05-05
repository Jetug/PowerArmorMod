package com.jetug.chassis_core.client.render.renderers;

import com.jetug.chassis_core.client.model.ChassisModel;
import com.jetug.chassis_core.client.render.layers.EquipmentLayer;
import com.jetug.chassis_core.client.render.layers.HeldItemLayer;
import com.jetug.chassis_core.client.render.layers.ScaledPlayerSkinLayer;
import com.jetug.chassis_core.common.foundation.entity.WearableChassis;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mod.azure.azurelib.cache.object.BakedGeoModel;
import mod.azure.azurelib.cache.object.GeoBone;
import mod.azure.azurelib.model.GeoModel;
import mod.azure.azurelib.renderer.GeoEntityRenderer;
import mod.azure.azurelib.util.RenderUtils;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.joml.*;

import java.util.ArrayList;
import java.util.Collection;

import static com.jetug.chassis_core.common.data.constants.Bones.LEFT_HAND;
import static com.jetug.chassis_core.common.data.constants.Bones.RIGHT_HAND;
import static net.minecraft.world.entity.EquipmentSlot.MAINHAND;
import static net.minecraft.world.entity.EquipmentSlot.OFFHAND;

public class ChassisRenderer<T extends WearableChassis> extends GeoEntityRenderer<T> {
    protected ItemStack mainHandItem, offHandItem;
    protected Collection<String> bonesToHide;

    public ChassisRenderer(EntityRendererProvider.Context renderManager) {
        this(renderManager, new ChassisModel<>());
    }

    public ChassisRenderer(EntityRendererProvider.Context renderManager, GeoModel<T> model) {
        super(renderManager, model);
        addRenderLayer(new ScaledPlayerSkinLayer<>(this));
        addRenderLayer(new EquipmentLayer<>(this));
        addRenderLayer(new HeldItemLayer<>(this, this::getItemForBone));
    }

    @Override
    public void render(T entity, float entityYaw, float partialTick,
                       PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        for (var name : entity.getMods())
            getGeoModel().getBone(name).ifPresent((bone) -> bone.setHidden(true));
        for (var name : entity.getVisibleMods())
            getGeoModel().getBone(name).ifPresent((bone) -> bone.setHidden(false));

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    public void defaultRender(PoseStack poseStack, T animatable, MultiBufferSource bufferSource,
                              @Nullable RenderType renderType, @Nullable VertexConsumer buffer,
                              float yaw, float partialTick, int packedLight) {
        if (isInvisible(animatable)) return;
        super.defaultRender(poseStack, animatable, bufferSource, renderType, buffer, yaw, partialTick, packedLight);
    }

    @Override
    public void renderRecursively(PoseStack poseStack, T animatable, GeoBone bone, RenderType renderType,
                                  MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender,
                                  float partialTick, int packedLight, int packedOverlay,
                                  float red, float green, float blue, float alpha) {
        bone.setHidden(bonesToHide.contains(bone.getName()));
//        if(bone.getName().equals("head")) {
//            CHASSIS_HEAD_RENDERER.render(
//                    poseStack, animatable, bufferSource,
//                    null, null, packedLight);
//        }

        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick,
                packedLight, packedOverlay, red, green, blue, alpha);

    }

    @Override
    public void renderChildBones(PoseStack poseStack, T animatable, GeoBone bone, RenderType renderType,
                                 MultiBufferSource bufferSource, VertexConsumer buffer,
                                 boolean isReRender, float partialTick, int packedLight, int packedOverlay,
                                 float red, float green, float blue, float alpha) {
        if (!bone.isHidingChildren()) {
            var bonesToRender = new ArrayList<>(bone.getChildBones());
            var equipmentBones = animatable.getEquipmentBones(bone.getName());
            bonesToRender.addAll(equipmentBones);

            for (var childBone : bonesToRender) {
                this.renderRecursively(poseStack, animatable, childBone, renderType, bufferSource, buffer,
                        isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
            }
        }
    }

    @Override
    public void preRender(PoseStack poseStack, T animatable, BakedGeoModel model,
                          MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender,
                          float partialTick, int packedLight, int packedOverlay,
                          float red, float green, float blue, float alpha) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        mainHandItem = animatable.getPassengerItem(MAINHAND);
        offHandItem = animatable.getPassengerItem(OFFHAND);
        bonesToHide = animatable.getBonesToHide();
    }

    protected ItemStack getItemForBone(GeoBone bone, T animatable) {
        if (animatable == null || !animatable.hasPassenger()) return null;

        return switch (bone.getName()) {
            case LEFT_HAND -> offHandItem;
            case RIGHT_HAND -> mainHandItem;
            default -> null;
        };
    }

//    protected Vector3d getWorldPos(GeoBone bone, PoseStack poseStack) {
//        poseStack.pushPose();
//        RenderUtils.translateMatrixToBone(poseStack, bone);
//        RenderUtils.translateToPivotPoint(poseStack, bone);
//
//        boolean rotOverride = bone.getModelRotationMatrix() != null;
//
//        if (rotOverride) {
//            poseStack.last().pose().mul(bone.getModelRotationMatrix());
//            poseStack.last().normal().mul(new Matrix3f(bone.getModelRotationMatrix()));
//        } else RenderUtils.rotateMatrixAroundBone(poseStack, bone);
//
//        RenderUtils.scaleMatrixForBone(poseStack, bone);
//
//        var poseState = (Matrix4f)poseStack.last().pose().clone();
//        var localMatrix = RenderUtils.invertAndMultiplyMatrices(poseState, this.modelRenderTranslations);
//        localMatrix.translate(new Vector3f(getRenderOffset(this.animatable, 1)));
//        var worldState = localMatrix.clone();
//        worldState.translate(new Vector3f(this.animatable.position()));
//        var vec = new Vector4f(0, 0, 0, 1);
//        vec.add(worldState);
//        RenderUtils.translateAwayFromPivotPoint(poseStack, bone);
//        poseStack.popPose();
//
//        return new Vector3d(vec.x(), vec.y(), vec.z());
//    }

    private boolean isInvisible(T animatable) {
        var clientPlayer = Minecraft.getInstance().player;
        var pov = Minecraft.getInstance().options.getCameraType();

        return animatable.hasPassenger(clientPlayer) && pov == CameraType.FIRST_PERSON;
    }
}