package com.jetug.chassis_core.client.render.renderers;

import com.jetug.chassis_core.client.model.*;
import com.jetug.chassis_core.client.render.layers.*;
import com.jetug.chassis_core.common.foundation.entity.*;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Vector3f;
import mod.azure.azurelib.cache.object.BakedGeoModel;
import mod.azure.azurelib.cache.object.GeoBone;
import mod.azure.azurelib.cache.object.GeoCube;
import mod.azure.azurelib.util.RenderUtils;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.*;

import java.util.*;

import static com.jetug.chassis_core.common.data.constants.Bones.*;
import static net.minecraft.world.entity.EquipmentSlot.MAINHAND;
import static net.minecraft.world.entity.EquipmentSlot.OFFHAND;

public class ChassisRenderer<T extends WearableChassis> extends ModGeoRenderer<T> {
    //protected final ChassisModel<T> chassisModel;
    protected ItemStack mainHandItem, offHandItem;
    protected Collection<String> bonesToHide;

    public ChassisRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ChassisModel<>());
        //chassisModel = (ChassisModel<T>)getGeoModelProvider();
        addRenderLayer(new PlayerHeadLayer<>(this));
        addRenderLayer(new EquipmentLayer<>(this));

    }

    @Override
    public void render(T entity, float entityYaw, float partialTick,
                       PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    public void defaultRender(PoseStack poseStack, T animatable, MultiBufferSource bufferSource, @Nullable RenderType renderType, @Nullable VertexConsumer buffer, float yaw, float partialTick, int packedLight) {
        if (isInvisible(animatable)) return;
        super.defaultRender(poseStack, animatable, bufferSource, renderType, buffer, yaw, partialTick, packedLight);
    }

    //
//    @Override
//    public void render(GeoModel model, T animatable,
//                       float partialTick, RenderType type, PoseStack poseStack,
//                       MultiBufferSource bufferSource, VertexConsumer buffer,
//                       int packedLight, int packedOverlay,
//                       float red, float green, float blue, float alpha) {
//        if (isInvisible(animatable)) return;
//        super.render(model, animatable, partialTick, type, poseStack, bufferSource, buffer,
//                packedLight, packedOverlay, red, green, blue, alpha);
//    }


    @Override
    public void renderRecursively(PoseStack poseStack, T animatable, GeoBone bone, RenderType renderType,
                                  MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender,
                                  float partialTick, int packedLight, int packedOverlay,
                                  float red, float green, float blue, float alpha) {
        bone.setHidden(bonesToHide.contains(bone.getName()));

//        MultiBufferSource bufferSource = getCurrentRTB();
        //var currentTexture = getTextureLocation(this.currentEntityBeingRendered);
        poseStack.pushPose();

        var boneItem = getHeldItemForBone(bone.getName(), this.currentEntityBeingRendered);
        var boneBlock = getHeldBlockForBone(bone.getName(), this.currentEntityBeingRendered);

        if (boneItem != null || boneBlock != null) {
            handleItemAndBlockBoneRendering(poseStack, bone, boneItem, boneBlock, packedLight, packedOverlay);
            //buffer = bufferSource.getBuffer(RenderType.entityTranslucent(currentTexture));
        }
        poseStack.popPose();

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

//    @Override
//    public void renderRecursively(GeoBone bone, PoseStack poseStack, VertexConsumer buffer, int packedLight,
//                                  int packedOverlay, float red, float green, float blue, float alpha) {
//        bone.isHidden = bonesToHide.contains(bone.name);
//
//        if (getCurrentModelRenderCycle() != EModelRenderCycle.INITIAL)
//            renderRecursivelyOriginal(bone, poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
//        else renderItemInHand(bone, poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
//    }
//
//    private void renderRecursivelyOriginal(GeoBone bone, PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
//        poseStack.pushPose();
//        setupBone(bone, poseStack);
//
//        if (!bone.isHidden()) {
//            if (!bone.isHidingChildren()) for (GeoCube geoCube : bone.getCubes()) {
//                poseStack.pushPose();
//                renderCube(geoCube, poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
//                poseStack.popPose();
//            }
//
//            var bonesToRender = new ArrayList<>(bone.getChildBones());
//            var equipmentBones = animatable.getEquipmentBones(bone.getName());
//            bonesToRender.addAll(equipmentBones);
//
//            for (GeoBone childBone : bonesToRender)
//                renderRecursively(childBone, poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
//        }
//
//        poseStack.popPose();
//    }

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

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, WearableChassis animatable) {
        if(animatable == null || !animatable.hasPlayerPassenger()) return null;

        return switch (boneName) {
            case LEFT_HAND -> offHandItem;
            case RIGHT_HAND-> mainHandItem;
            default -> null;
        };
    }

    @Override
    protected ItemTransforms.TransformType getCameraTransformForItemAtBone(ItemStack stack, String boneName) {
        return switch (boneName) {
            case LEFT_HAND, RIGHT_HAND -> ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND;
            default -> ItemTransforms.TransformType.NONE;
        };
    }

    @Override
    protected void preRenderItem(PoseStack stack, ItemStack item, String boneName, WearableChassis currentEntity, GeoBone bone) {
        stack.translate(0, 0, -0.09);

        if (!(item.getItem() instanceof ShieldItem)) return;
        if (item == this.mainHandItem) {
            stack.translate(0, 0.125, -0.09);
        }
        else if (item == this.offHandItem) {
            stack.translate(0, 0.125, 0.37);
            stack.mulPose(Vector3f.YP.rotationDegrees(180));
        }
    }

    private boolean isInvisible(T animatable) {
        //if(animatable == null) return false;

        var clientPlayer = Minecraft.getInstance().player;
        var pov = Minecraft.getInstance().options.getCameraType();

        return animatable.hasPassenger(clientPlayer) && pov == CameraType.FIRST_PERSON;
    }
}