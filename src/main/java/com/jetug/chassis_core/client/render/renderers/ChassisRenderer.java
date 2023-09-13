package com.jetug.chassis_core.client.render.renderers;

import com.jetug.chassis_core.client.model.*;
import com.jetug.chassis_core.client.render.layers.*;
import com.jetug.chassis_core.client.render.utils.GeoUtils;
import com.jetug.chassis_core.common.foundation.entity.*;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Vector3f;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.*;
import software.bernie.geckolib3.core.processor.*;
import software.bernie.geckolib3.geo.render.built.*;
import software.bernie.geckolib3.util.EModelRenderCycle;

import java.util.*;

import static com.jetug.chassis_core.client.render.utils.GeoUtils.getEquipmentBones;
import static com.jetug.chassis_core.common.data.constants.Bones.*;
import static java.util.Collections.*;
import static net.minecraft.world.entity.EquipmentSlot.MAINHAND;
import static net.minecraft.world.entity.EquipmentSlot.OFFHAND;

public class ChassisRenderer<T extends WearableChassis> extends ModGeoRenderer<T> {
    protected final ChassisModel<T> chassisModel;
    protected ItemStack mainHandItem, offHandItem;
    protected ArrayList<String> bonesToHide;

    public ChassisRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ChassisModel<>());
        chassisModel = (ChassisModel<T>)getGeoModelProvider();
        addLayer(new EquipmentLayer<>(this));
        addLayer(new PlayerHeadLayer<>(this));
    }

    @Override
    public void render(GeoModel model, T animatable,
                       float partialTick, RenderType type, PoseStack poseStack,
                       MultiBufferSource bufferSource, VertexConsumer buffer,
                       int packedLight, int packedOverlay,
                       float red, float green, float blue, float alpha) {
        if (isInvisible(animatable)) return;
        super.render(model, animatable, partialTick, type, poseStack, bufferSource, buffer,
                packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void renderRecursively(GeoBone bone, PoseStack poseStack, VertexConsumer buffer, int packedLight,
                                  int packedOverlay, float red, float green, float blue, float alpha) {
        if (getCurrentModelRenderCycle() != EModelRenderCycle.INITIAL)
            renderRecursivelyOriginal(bone, poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        else renderItemInHand(bone, poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    private void renderRecursivelyOriginal(GeoBone bone, PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.pushPose();
        setupBone(bone, poseStack);

        var bonesToRender = new ArrayList<>(bone.childBones);
        bonesToRender.addAll(getEquipmentBones(bone.name, animatable));
        bone.isHidden = bonesToHide.contains(bone.name);

        if (!bone.isHidden) {
            if (!bone.cubesAreHidden()) {
                for (GeoCube geoCube : bone.childCubes) {
                    poseStack.pushPose();
                    renderCube(geoCube, poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
                    poseStack.popPose();
                }
            }

            for (GeoBone childBone : bonesToRender) {
                renderRecursively(childBone, poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
            }
        }

        poseStack.popPose();
    }

    @Override
    public void renderEarly(T animatable, PoseStack poseStack, float partialTick, MultiBufferSource bufferSource,
                            VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float partialTicks) {
        super.renderEarly(animatable, poseStack, partialTick, bufferSource, buffer, packedLight, packedOverlay, red, green, blue, partialTicks);
        mainHandItem = animatable.getPlayerItem(MAINHAND);
        offHandItem  = animatable.getPlayerItem(OFFHAND);
        bonesToHide = new ArrayList<>();

        for (var config : animatable.getItemConfigs())
            addAll(bonesToHide, config.hide);
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, WearableChassis animatable) {
        if(!animatable.hasPlayerPassenger()) return null;

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
    protected void preRenderItem(PoseStack stack, ItemStack item, String boneName, WearableChassis currentEntity, IBone bone) {
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
        var clientPlayer = Minecraft.getInstance().player;
        var pov = Minecraft.getInstance().options.getCameraType();

        if (animatable.hasPassenger(clientPlayer) && pov == CameraType.FIRST_PERSON)
            return true;
        return false;
    }
}