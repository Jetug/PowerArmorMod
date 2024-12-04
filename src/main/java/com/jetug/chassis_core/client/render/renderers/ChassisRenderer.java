package com.jetug.chassis_core.client.render.renderers;

import com.jetug.chassis_core.client.render.layers.EquipmentLayer;
import com.jetug.chassis_core.client.render.layers.HeldItemLayer;
import com.jetug.chassis_core.common.foundation.entity.WearableChassis;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mod.azure.azurelib.cache.object.BakedGeoModel;
import mod.azure.azurelib.cache.object.GeoBone;
import mod.azure.azurelib.model.GeoModel;
import mod.azure.azurelib.renderer.DynamicGeoEntityRenderer;
import mod.azure.azurelib.util.RenderUtils;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import static com.jetug.chassis_core.common.data.constants.Bones.LEFT_HAND;
import static com.jetug.chassis_core.common.data.constants.Bones.RIGHT_HAND;
import static net.minecraft.world.entity.EquipmentSlot.MAINHAND;
import static net.minecraft.world.entity.EquipmentSlot.OFFHAND;

public class ChassisRenderer<T extends WearableChassis> extends DynamicGeoEntityRenderer<T> {
    protected ItemStack mainHandItem, offHandItem;
    protected Collection<String> bonesToHide;
    private MultiBufferSource bufferSource;

    public ChassisRenderer(EntityRendererProvider.Context renderManager, GeoModel<T> model) {
        super(renderManager, model);
        addRenderLayer(new EquipmentLayer<>(this));
        addRenderLayer(new HeldItemLayer<>(this, this::getItemForBone));
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

    @Override
    public void defaultRender(PoseStack poseStack, T animatable, MultiBufferSource bufferSource,
                              @Nullable RenderType renderType, @Nullable VertexConsumer buffer,
                              float yaw, float partialTick, int packedLight) {
        this.bufferSource = bufferSource;
        if (isInvisible(animatable)) return;
        super.defaultRender(poseStack, animatable, bufferSource, renderType, buffer, yaw, partialTick, packedLight);
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
    public void renderRecursively(PoseStack poseStack, T animatable, GeoBone bone, RenderType renderType,
                                  MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick,
                                  int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        bone.setHidden(bonesToHide.contains(bone.getName()));

        if(Objects.equals(bone.getName(), "head_frame")) {
            bone.setHidden(true);
            bone.setChildrenHidden(false);
            renderHead(poseStack, animatable, bone, packedLight, packedOverlay);
        }

        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource,
                this.bufferSource.getBuffer(renderType), isReRender,
                partialTick, packedLight, packedOverlay,
                red, green, blue, alpha);
    }

    @Override
    public void renderChildBones(PoseStack poseStack, T animatable, GeoBone bone, RenderType renderType,
                                 MultiBufferSource bufferSource, VertexConsumer buffer,
                                 boolean isReRender, float partialTick, int packedLight, int packedOverlay,
                                 float red, float green, float blue, float alpha) {
        if (!bone.isHidingChildren()) {
            var bonesToRender = new ArrayList<>(bone.getChildBones());
            var equipmentBones = animatable.getAttachmentForBone(bone.getName());
            bonesToRender.addAll(equipmentBones);

            for (var childBone : bonesToRender) {
                this.renderRecursively(poseStack, animatable, childBone, renderType, bufferSource, buffer,
                        isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
            }
        }
    }

    @Override
    protected @Nullable ResourceLocation getTextureOverrideForBone(GeoBone bone, T animatable, float partialTick) {
        var texture = animatable.getTextureForBone(bone.getName());
        return texture;
    }

    @Override
    protected void renderNameTag(T entity, Component displayName, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if(entity.getControllingPassenger() instanceof Player player) {
            super.renderNameTag(entity, player.getName(), poseStack, buffer, packedLight);
        }
    }

    protected void renderHead(PoseStack poseStack, T animatable, GeoBone bone, int packedLight, int packedOverlay) {
        if (!animatable.isInvisible() && animatable.hasPassenger()) {
            var passenger = animatable.getControllingPassenger();

            if (getEntityRenderDispatcher().getRenderer(passenger) instanceof LivingEntityRenderer humanoidRenderer &&
                    humanoidRenderer.getModel() instanceof HumanoidModel humanoidModel) {


                poseStack.pushPose();
                {
                    RenderUtils.prepMatrixForBone(poseStack, bone);
//                    translateMatrixToBone(poseStack, bone);
//                    translateToPivotPoint(poseStack, bone);
//                    rotateMatrixAroundBone(poseStack, bone);
//                    scaleMatrixForBone(poseStack, bone);
//                    translateAwayFromPivotPoint(poseStack, bone);
//                    poseStack.mulPose(Axis.ZP.rotationDegrees(180));
//                    poseStack.translate(0, -4, 0);

                    var skin = humanoidRenderer.getTextureLocation(passenger);
                    var head = this.bufferSource.getBuffer(RenderType.entitySolid(skin));
                    var hat = this.bufferSource.getBuffer(RenderType.entityTranslucent(skin));

                    renderHumanoidPart(poseStack, bone, packedLight, packedOverlay, humanoidModel, head);
                    renderHumanoidPart(poseStack, bone, packedLight, packedOverlay, humanoidModel, hat);
                }
                poseStack.popPose();
            }
        }
    }

    protected ItemStack getItemForBone(GeoBone bone, T animatable) {
        if (animatable == null || !animatable.hasPassenger()) return null;

        return switch (bone.getName()) {
            case LEFT_HAND -> offHandItem;
            case RIGHT_HAND -> mainHandItem;
            default -> null;
        };
    }

    protected boolean isInvisible(T animatable) {
        var clientPlayer = Minecraft.getInstance().player;
        var pov = Minecraft.getInstance().options.getCameraType();

        return animatable.hasPassenger(clientPlayer) && pov == CameraType.FIRST_PERSON;
    }

    @NotNull
    private static EntityRenderDispatcher getEntityRenderDispatcher() {
        return Minecraft.getInstance().getEntityRenderDispatcher();
    }

    private static void renderHumanoidPart(PoseStack poseStack, GeoBone bone, int packedLight, int packedOverlay,
                                           HumanoidModel humanoidModel, VertexConsumer head) {
        humanoidModel.head.setPos(bone.getPivotX(), bone.getPivotY(), bone.getPivotZ());
        humanoidModel.head.setRotation(0, 0, 0);
        humanoidModel.head.render(poseStack, head, packedLight, packedOverlay, 1, 1, 1, 1);
    }
}