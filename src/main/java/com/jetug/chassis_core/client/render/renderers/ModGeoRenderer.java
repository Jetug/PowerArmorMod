package com.jetug.chassis_core.client.render.renderers;

import com.jetug.chassis_core.ChassisCore;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.ExtendedGeoEntityRenderer;
import software.bernie.geckolib3.util.RenderUtils;

import java.util.Objects;

import static software.bernie.geckolib3.util.RenderUtils.*;

public abstract class ModGeoRenderer<T extends LivingEntity & IAnimatable> extends ExtendedGeoEntityRenderer<T> {
    protected float currentPartialTicks;

    protected ModGeoRenderer(EntityRendererProvider.Context renderManager, AnimatedGeoModel<T> modelProvider) {
        super(renderManager, modelProvider);
    }

    @Override
    public void renderLate(T animatable, PoseStack poseStack, float partialTick, MultiBufferSource bufferSource,
                           VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue,
                           float partialTicks) {
        super.renderLate(animatable, poseStack, partialTick, bufferSource, buffer, packedLight, packedOverlay, red,
                green, blue, partialTicks);
        this.currentPartialTicks = partialTicks;
    }


    protected Vector3d getWorldPos(GeoBone bone, PoseStack poseStack){
        poseStack.pushPose();
        RenderUtils.translateMatrixToBone(poseStack, bone);
        RenderUtils.translateToPivotPoint(poseStack, bone);

        boolean rotOverride = bone.rotMat != null;

        if (rotOverride) {
            poseStack.last().pose().multiply(bone.rotMat);
            poseStack.last().normal().mul(new Matrix3f(bone.rotMat));
        }
        else RenderUtils.rotateMatrixAroundBone(poseStack, bone);

        RenderUtils.scaleMatrixForBone(poseStack, bone);

        var poseState = poseStack.last().pose().copy();
        var localMatrix = RenderUtils.invertAndMultiplyMatrices(poseState, this.dispatchedMat);
        localMatrix.translate(new Vector3f(getRenderOffset(this.animatable, 1)));
        var worldState = localMatrix.copy();
        worldState.translate(new Vector3f(this.animatable.position()));
        var vec = new Vector4f(0, 0, 0, 1);
        vec.transform(worldState);
        RenderUtils.translateAwayFromPivotPoint(poseStack, bone);
        poseStack.popPose();

        return new Vector3d(vec.x(), vec.y(), vec.z());
    }

    protected void renderItemInHand(GeoBone bone, PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        MultiBufferSource bufferSource = getCurrentRTB();
        ResourceLocation currentTexture = getTextureLocation(this.currentEntityBeingRendered);
        boolean useCustomTexture = this.textureForBone != null;

        poseStack.pushPose();
        // Render armor
        if (isArmorBone(bone)) {
            handleArmorRenderingForBone(bone, poseStack, buffer, packedLight, packedOverlay, currentTexture);
        }
        else {
            ItemStack boneItem = getHeldItemForBone(bone.getName(), this.currentEntityBeingRendered);
            BlockState boneBlock = getHeldBlockForBone(bone.getName(), this.currentEntityBeingRendered);

            if (boneItem != null || boneBlock != null) {
                handleItemAndBlockBoneRendering(poseStack, bone, boneItem, boneBlock, packedLight, packedOverlay);

                buffer = bufferSource.getBuffer(RenderType.entityTranslucent(currentTexture));
            }
        }

        poseStack.popPose();

        customBoneSpecificRenderingHook(bone, poseStack, buffer, packedLight, packedOverlay, red, green, blue,
                alpha, useCustomTexture, currentTexture);

        poseStack.pushPose();
        RenderUtils.prepMatrixForBone(poseStack, bone);
        super.renderCubesOfBone(bone, poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);

        // Reset buffer
        if (useCustomTexture) {
            buffer = bufferSource.getBuffer(this.getRenderType(this.currentEntityBeingRendered,
                    this.currentPartialTicks, poseStack, bufferSource, buffer, packedLight, currentTexture));
            // Reset the marker...
            this.textureForBone = null;
        }

        super.renderChildBones(bone, poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        poseStack.popPose();
    }

    protected void setupBone(GeoBone bone, PoseStack poseStack) {
        translateMatrixToBone(poseStack, bone);
        translateToPivotPoint(poseStack, bone);

        if(Objects.equals(bone.name, "body_top"))
            ChassisCore.LOGGER.error(bone.name);

        boolean rotOverride = bone.rotMat != null;

        if (rotOverride) {
            poseStack.last().pose().multiply(bone.rotMat);
            poseStack.last().normal().mul(new Matrix3f(bone.rotMat));
        }
        else rotateMatrixAroundBone(poseStack, bone);

        scaleMatrixForBone(poseStack, bone);

        if (bone.isTrackingXform()) {
            Matrix4f poseState = poseStack.last().pose().copy();
            Matrix4f localMatrix = invertAndMultiplyMatrices(poseState, this.dispatchedMat);

            bone.setModelSpaceXform(invertAndMultiplyMatrices(poseState, this.renderEarlyMat));
            localMatrix.translate(new Vector3f(getRenderOffset(this.animatable, 1)));
            bone.setLocalSpaceXform(localMatrix);

            Matrix4f worldState = localMatrix.copy();

            worldState.translate(new Vector3f(this.animatable.position()));
            bone.setWorldSpaceXform(worldState);
        }

        translateAwayFromPivotPoint(poseStack, bone);
    }

    protected ModGeoRenderer(EntityRendererProvider.Context renderManager, AnimatedGeoModel<T> modelProvider, float widthScale, float heightScale, float shadowSize) {
        super(renderManager, modelProvider, widthScale, heightScale, shadowSize);
    }

    @Override
    protected boolean isArmorBone(GeoBone bone) {
        return false;
    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, T animatable) {
        return null;
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, T animatable) {
        return null;
    }

    @Override
    protected ItemTransforms.TransformType getCameraTransformForItemAtBone(ItemStack stack, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected BlockState getHeldBlockForBone(String boneName, T animatable) {
        return null;
    }

    @Override
    protected void preRenderItem(PoseStack poseStack, ItemStack stack, String boneName, T animatable, IBone bone) {

    }

    @Override
    protected void preRenderBlock(PoseStack poseStack, BlockState state, String boneName, T animatable) {

    }

    @Override
    protected void postRenderItem(PoseStack poseStack, ItemStack stack, String boneName, T animatable, IBone bone) {

    }

    @Override
    protected void postRenderBlock(PoseStack poseStack, BlockState state, String boneName, T animatable) {

    }
}
