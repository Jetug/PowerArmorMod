package com.jetug.chassis_core.client.render.renderers;

import com.jetug.chassis_core.ChassisCore;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.*;
import mod.azure.azurelib.cache.object.BakedGeoModel;
import mod.azure.azurelib.cache.object.GeoBone;
import mod.azure.azurelib.core.animatable.GeoAnimatable;
import mod.azure.azurelib.model.GeoModel;
import mod.azure.azurelib.renderer.GeoEntityRenderer;
import mod.azure.azurelib.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import java.util.Objects;

import static net.minecraft.client.renderer.block.model.ItemTransforms.*;
import static software.bernie.geckolib3.util.RenderUtils.*;

public abstract class ModGeoRenderer<T extends LivingEntity & GeoAnimatable> extends GeoEntityRenderer<T> {
    protected float currentPartialTicks;
    protected T currentEntityBeingRendered;
    public MultiBufferSource rtb;

    protected ModGeoRenderer(EntityRendererProvider.Context renderManager, GeoModel<T> modelProvider) {
        super(renderManager, modelProvider);
    }

    @Override
    public void postRender(PoseStack poseStack, T animatable, BakedGeoModel model,
                           MultiBufferSource bufferSource, VertexConsumer buffer,
                           boolean isReRender, float partialTick, int packedLight,
                           int packedOverlay, float red, float green, float blue, float alpha) {
        super.postRender(poseStack, animatable, model, bufferSource, buffer, isReRender,
                partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void preRender(PoseStack poseStack, T animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        this.rtb = bufferSource;
        this.currentEntityBeingRendered = animatable;
        this.currentPartialTicks = partialTick;
    }

    public MultiBufferSource getCurrentRTB() {
        return this.rtb;
    }

    //    @Override
//    public void renderLate(T animatable, PoseStack poseStack, float partialTick, MultiBufferSource bufferSource,
//                           VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue,
//                           float partialTicks) {
//        super.renderLate(animatable, poseStack, partialTick, bufferSource, buffer, packedLight, packedOverlay, red,
//                green, blue, partialTicks);
//        this.currentEntityBeingRendered = animatable;
//        this.currentPartialTicks = partialTicks;
//
//    }

    protected Vector3d getWorldPos(GeoBone bone, PoseStack poseStack){
        poseStack.pushPose();
        RenderUtils.translateMatrixToBone(poseStack, bone);
        RenderUtils.translateToPivotPoint(poseStack, bone);

        boolean rotOverride = bone.getModelRotationMatrix() != null;

        if (rotOverride) {
            poseStack.last().pose().multiply(bone.getModelRotationMatrix());
            poseStack.last().normal().mul(new Matrix3f(bone.getModelRotationMatrix()));
        }
        else RenderUtils.rotateMatrixAroundBone(poseStack, bone);

        RenderUtils.scaleMatrixForBone(poseStack, bone);

        var poseState = poseStack.last().pose().copy();
        var localMatrix = RenderUtils.invertAndMultiplyMatrices(poseState, this.modelRenderTranslations);
        localMatrix.translate(new Vector3f(getRenderOffset(this.animatable, 1)));
        var worldState = localMatrix.copy();
        worldState.translate(new Vector3f(this.animatable.position()));
        var vec = new Vector4f(0, 0, 0, 1);
        vec.transform(worldState);
        RenderUtils.translateAwayFromPivotPoint(poseStack, bone);
        poseStack.popPose();

        return new Vector3d(vec.x(), vec.y(), vec.z());
    }

    protected void renderItemInHand(GeoBone bone, PoseStack poseStack, VertexConsumer buffer,
                                    int packedLight, int packedOverlay,
                                    float red, float green, float blue, float alpha) {
        MultiBufferSource bufferSource = getCurrentRTB();
        ResourceLocation currentTexture = getTextureLocation(this.currentEntityBeingRendered);
        poseStack.pushPose();

        var boneItem = getHeldItemForBone(bone.getName(), this.currentEntityBeingRendered);
        var boneBlock = getHeldBlockForBone(bone.getName(), this.currentEntityBeingRendered);

        if (boneItem != null || boneBlock != null) {
            handleItemAndBlockBoneRendering(poseStack, bone, boneItem, boneBlock, packedLight, packedOverlay);
            buffer = bufferSource.getBuffer(RenderType.entityTranslucent(currentTexture));
        }
        poseStack.popPose();

        poseStack.pushPose();
        RenderUtils.prepMatrixForBone(poseStack, bone);
        super.renderCubesOfBone(poseStack, bone, buffer, packedLight, packedOverlay, red, green, blue, alpha);


        var renderType = getRenderType(animatable,currentTexture,bufferSource, currentPartialTicks);

        super.renderChildBones( poseStack, animatable, bone, renderType, bufferSource, buffer, false,
                currentPartialTicks, packedLight, packedOverlay,
                red, green, blue, alpha);
        poseStack.popPose();
    }

    protected void handleItemAndBlockBoneRendering(PoseStack poseStack, GeoBone bone, @javax.annotation.Nullable ItemStack boneItem,
                                                   @javax.annotation.Nullable BlockState boneBlock, int packedLight, int packedOverlay) {
        RenderUtils.prepMatrixForBone(poseStack, bone);
        RenderUtils.translateAndRotateMatrixForBone(poseStack, bone);

        if (boneItem != null) {
            preRenderItem(poseStack, boneItem, bone.getName(), this.currentEntityBeingRendered, bone);
            renderItemStack(poseStack, getCurrentRTB(), packedLight, boneItem, bone.getName());
            postRenderItem(poseStack, boneItem, bone.getName(), this.currentEntityBeingRendered, bone);
        }

        if (boneBlock != null) {
            preRenderBlock(poseStack, boneBlock, bone.getName(), this.currentEntityBeingRendered);
            renderBlock(poseStack, getCurrentRTB(), packedLight, boneBlock);
            postRenderBlock(poseStack, boneBlock, bone.getName(), this.currentEntityBeingRendered);
        }
    }

    protected void renderBlock(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight,
                               BlockState state) {
        if (state.getRenderShape() != RenderShape.MODEL)
            return;

        poseStack.pushPose();
        poseStack.translate(-0.25f, -0.25f, -0.25f);
        poseStack.scale(0.5F, 0.5F, 0.5F);
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(state, poseStack, bufferSource, packedLight,
                OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
    }

    protected void renderItemStack(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, ItemStack stack,
                                   String boneName) {
        Minecraft.getInstance().getItemRenderer().renderStatic(this.currentEntityBeingRendered, stack,
                getCameraTransformForItemAtBone(stack, boneName), false, poseStack, bufferSource, null, packedLight,
                LivingEntityRenderer.getOverlayCoords(this.currentEntityBeingRendered, 0.0F),
                currentEntityBeingRendered.getId());
    }

//    protected void setupBone(GeoBone bone, PoseStack poseStack) {
//        translateMatrixToBone(poseStack, bone);
//        translateToPivotPoint(poseStack, bone);
//
//        boolean rotOverride = bone.getModelRotationMatrix() != null;
//
//        if (rotOverride) {
//            poseStack.last().pose().multiply(bone.getModelRotationMatrix());
//            poseStack.last().normal().mul(new Matrix3f(bone.getModelRotationMatrix()));
//        }
//        else rotateMatrixAroundBone(poseStack, bone);
//
//        scaleMatrixForBone(poseStack, bone);
//
//        if (bone.isTrackingXform()) {
//            Matrix4f poseState = poseStack.last().pose().copy();
//            Matrix4f localMatrix = invertAndMultiplyMatrices(poseState, this.dispatchedMat);
//
//            bone.setModelSpaceMatrix(invertAndMultiplyMatrices(poseState, this.renderEarlyMat));
//            localMatrix.translate(new Vector3f(getRenderOffset(this.animatable, 1)));
//            bone.setLocalSpaceMatrix(localMatrix);
//
//            Matrix4f worldState = localMatrix.copy();
//
//            worldState.translate(new Vector3f(this.animatable.position()));
//            bone.setWorldSpaceMatrix(worldState);
//        }
//
//        translateAwayFromPivotPoint(poseStack, bone);
//    }


    protected ItemStack getHeldItemForBone(String boneName, T animatable) { return null; }
    protected TransformType getCameraTransformForItemAtBone(ItemStack stack, String boneName) { return null; }
    protected BlockState getHeldBlockForBone(String boneName, T animatable) { return null; }
    protected void preRenderItem(PoseStack poseStack, ItemStack stack, String boneName, T animatable, GeoBone bone) {}
    protected void preRenderBlock(PoseStack poseStack, BlockState state, String boneName, T animatable) {}
    protected void postRenderItem(PoseStack poseStack, ItemStack stack, String boneName, T animatable, GeoBone bone) {}
    protected void postRenderBlock(PoseStack poseStack, BlockState state, String boneName, T animatable) {}
}
