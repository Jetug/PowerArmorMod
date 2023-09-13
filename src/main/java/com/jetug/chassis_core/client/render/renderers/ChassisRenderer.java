package com.jetug.chassis_core.client.render.renderers;

import com.ibm.icu.impl.Pair;
import com.jetug.chassis_core.ChassisCore;
import com.jetug.chassis_core.client.model.*;
import com.jetug.chassis_core.client.render.layers.*;
import com.jetug.chassis_core.client.render.utils.GeoUtils;
import com.jetug.chassis_core.common.data.json.EquipmentAttachment;
import com.jetug.chassis_core.common.foundation.entity.*;
import com.jetug.chassis_core.common.foundation.item.ChassisEquipment;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
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
import software.bernie.geckolib3.util.RenderUtils;

import java.util.*;

import static com.jetug.chassis_core.common.data.constants.Bones.*;
import static java.util.Collections.*;
import static net.minecraft.world.entity.EquipmentSlot.MAINHAND;
import static net.minecraft.world.entity.EquipmentSlot.OFFHAND;

public class ChassisRenderer<T extends WearableChassis> extends ModGeoRenderer<T> {
    protected final ChassisModel<T> chassisModel;
    protected ItemStack mainHandItem, offHandItem;

    public ChassisRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ChassisModel<>());
        chassisModel = (ChassisModel<T>)getGeoModelProvider();
        initLayers();
    }

    private void initLayers(){
        addLayer(new EquipmentLayer(this));
        addLayer(new PlayerHeadLayer(this));
    }

    @Override
    public void render(GeoModel model, T animatable,
                       float partialTick, RenderType type, PoseStack poseStack,
                       MultiBufferSource bufferSource, VertexConsumer buffer,
                       int packedLight, int packedOverlay,
                       float red, float green, float blue, float alpha) {

        if (isInvisible(animatable)) return;
//        for (var part : animatable.getEquipment())
//            renderEquipment(chassisModel, animatable, part, false);

        super.render(model, animatable, partialTick, type, poseStack, bufferSource, buffer,
                packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Nullable
    protected Collection<GeoBone> getEquipmentBones(String boneName, T animatable) {
        var result = new ArrayList<GeoBone>();
        var configs = animatable.getItemConfigs();
        for (var config: configs) {
            var boneNames = config.getArmorBone(boneName);

            for (var name : boneNames) {
                var armorBone = getArmorBone(config.getModelLocation(), name);
                result.add(armorBone);
            }
        }
        return result;
    }

    @Nullable
    protected ItemStack getItemForBone(String boneName, T animatable) {
        var part = getChassisPart(boneName, animatable);
        return part == null ? null : animatable.getEquipment(part);
    }

    @Nullable
    protected String getChassisPart(String boneName, T animatable) {
        return animatable.getConfig().getPartForBone(boneName);
    }

//    @Override
    public void renderRecursively2(GeoBone bone, PoseStack poseStack, VertexConsumer buffer,
                                  int packedLight, int packedOverlay,
                                  float red, float green, float blue, float alpha) {
        bone.isHidden = bonesToHide.contains(bone.name);
        super.renderRecursively(bone, poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);

        var itemStack = getItemForBone(bone.name, currentEntityBeingRendered);
        if(itemStack != null && itemStack.getItem() instanceof ChassisEquipment item){
            var config = item.getConfig();
            var armorBoneNames = config.getArmorBone(bone.name);

            for (var name : armorBoneNames) {
                var armorBone = getArmorBone(item.getConfig().getModelLocation(), name);
                super.renderRecursively(armorBone, poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
            }
        }

    }

    @Override
    public void renderRecursively(GeoBone bone, PoseStack poseStack, VertexConsumer buffer, int packedLight,
                                  int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.pushPose();
        RenderUtils.translateMatrixToBone(poseStack, bone);
        RenderUtils.translateToPivotPoint(poseStack, bone);

        bone.isHidden = bonesToHide.contains(bone.name);

        if(Objects.equals(bone.name, "body_top"))
            ChassisCore.LOGGER.error(bone.name);

        boolean rotOverride = bone.rotMat != null;

        if (rotOverride) {
            poseStack.last().pose().multiply(bone.rotMat);
            poseStack.last().normal().mul(new Matrix3f(bone.rotMat));
        }
        else {
            RenderUtils.rotateMatrixAroundBone(poseStack, bone);
        }

        RenderUtils.scaleMatrixForBone(poseStack, bone);

        if (bone.isTrackingXform()) {
            Matrix4f poseState = poseStack.last().pose().copy();
            Matrix4f localMatrix = RenderUtils.invertAndMultiplyMatrices(poseState, this.dispatchedMat);

            bone.setModelSpaceXform(RenderUtils.invertAndMultiplyMatrices(poseState, this.renderEarlyMat));
            localMatrix.translate(new Vector3f(getRenderOffset(this.animatable, 1)));
            bone.setLocalSpaceXform(localMatrix);

            Matrix4f worldState = localMatrix.copy();

            worldState.translate(new Vector3f(this.animatable.position()));
            bone.setWorldSpaceXform(worldState);
        }

        RenderUtils.translateAwayFromPivotPoint(poseStack, bone);

        var bonesToRender = new ArrayList<>(bone.childBones);

//        var itemStack = getItemForBone(bone.name, currentEntityBeingRendered);
//        if(itemStack != null && itemStack.getItem() instanceof ChassisEquipment item){
//            var config = item.getConfig();
//            var armorBoneNames = config.getArmorBone(bone.name);
//
//            for (var name : armorBoneNames) {
//                var armorBone = getArmorBone(item.getConfig().getModelLocation(), name);
//                bonesToRender.add(armorBone);
//            }
//        }


        bonesToRender.addAll(getEquipmentBones(bone.name, animatable));

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


    private final Map<String, Pair<GeoBone, EquipmentAttachment>> attachmentForBone = new HashMap<>();
    private ArrayList<String> bonesToHide;

    @Override
    public void renderEarly(T animatable, PoseStack poseStack, float partialTick, MultiBufferSource bufferSource,
                            VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float partialTicks) {
        super.renderEarly(animatable, poseStack, partialTick, bufferSource, buffer, packedLight, packedOverlay, red, green, blue, partialTicks);
        mainHandItem = animatable.getPlayerItem(MAINHAND);
        offHandItem  = animatable.getPlayerItem(OFFHAND);
        bonesToHide = new ArrayList<>();

        for (var part : animatable.getEquipment()) {
            if(animatable.isEquipmentVisible(part)) {
                var item = animatable.getEquipmentItem(part);
                var config = item.getConfig();

                for(var att : config.attachments){
                    var equipmentBone = getArmorBone(config.getModelLocation(), att.armor);
                    attachmentForBone.put(att.frame, Pair.of(equipmentBone, att));
                }

                addAll(bonesToHide, config.hide);
            }
        }
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

    @Nullable
    public GeoBone getFrameBone(String name){
        return GeoUtils.getFrameBone(chassisModel, name);
    }

    @Nullable
    public GeoBone getArmorBone(ResourceLocation resourceLocation, String name){
        return GeoUtils.getArmorBone(resourceLocation, name);
    }

    private boolean isInvisible(T animatable) {
        var clientPlayer = Minecraft.getInstance().player;
        var pov = Minecraft.getInstance().options.getCameraType();

        if (animatable.hasPassenger(clientPlayer) && pov == CameraType.FIRST_PERSON)
            return true;
        return false;
    }
}