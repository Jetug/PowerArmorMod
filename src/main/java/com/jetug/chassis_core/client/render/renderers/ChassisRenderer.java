package com.jetug.chassis_core.client.render.renderers;

import com.jetug.chassis_core.client.model.*;
import com.jetug.chassis_core.client.render.layers.*;
import com.jetug.chassis_core.common.foundation.entity.*;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.*;
import mod.azure.azurelib.cache.object.*;
import mod.azure.azurelib.renderer.*;
import mod.azure.azurelib.renderer.layer.*;
import mod.azure.azurelib.util.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.*;
import java.util.*;

import static com.jetug.chassis_core.common.data.constants.Bones.*;
import static net.minecraft.world.entity.EquipmentSlot.*;

public class ChassisRenderer<T extends WearableChassis> extends GeoEntityRenderer<T> {
    protected ItemStack mainHandItem, offHandItem;
    protected Collection<String> bonesToHide;

    public ChassisRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ChassisModel<>());
        addRenderLayer(new PlayerHeadLayer<>(this));
        addRenderLayer(new EquipmentLayer<>(this));
        addRenderLayer(new BlockAndItemGeoLayer<>(this, this::getItemForBone, (i, g) -> null){
            @Override
            protected ItemTransforms.TransformType getTransformTypeForStack(GeoBone bone, ItemStack stack, T animatable) {
                return switch (bone.getName()) {
                    case LEFT_HAND, RIGHT_HAND -> ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND;
                    default -> ItemTransforms.TransformType.NONE;
                };
            }
        });
    }

    @Override
    public void render(T entity, float entityYaw, float partialTick,
                       PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        for(var name : entity.getMods())
            getGeoModel().getBone(name).ifPresent((bone) -> bone.setHidden(true));
        for(var name : entity.getVisibleMods())
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
        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
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

            for (GeoBone childBone : bonesToRender) {
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
        mainHandItem = animatable.getPlayerItem(MAINHAND);
        offHandItem  = animatable.getPlayerItem(OFFHAND);
        bonesToHide = animatable.getBonesToHide();
    }

    protected ItemStack getItemForBone(GeoBone bone, T animatable) {
        if(animatable == null || !animatable.hasPlayerPassenger()) return null;

        return switch (bone.getName()) {
            case LEFT_HAND -> offHandItem;
            case RIGHT_HAND-> mainHandItem;
            default -> null;
        };
    }

//    @Override
//    protected void preRenderItem(PoseStack stack, ItemStack item, String boneName, WearableChassis currentEntity, GeoBone bone) {
//        stack.translate(0, 0, -0.09);
//
//        if (!(item.getItem() instanceof ShieldItem)) return;
//        if (item == this.mainHandItem) {
//            stack.translate(0, 0.125, -0.09);
//        }
//        else if (item == this.offHandItem) {
//            stack.translate(0, 0.125, 0.37);
//            stack.mulPose(Vector3f.YP.rotationDegrees(180));
//        }
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

    private boolean isInvisible(T animatable) {
        var clientPlayer = Minecraft.getInstance().player;
        var pov = Minecraft.getInstance().options.getCameraType();

        return animatable.hasPassenger(clientPlayer) && pov == CameraType.FIRST_PERSON;
    }
}