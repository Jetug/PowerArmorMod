package com.jetug.power_armor_mod.client.render.renderers;

import com.jetug.power_armor_mod.client.*;
import com.jetug.power_armor_mod.client.model.*;
import com.jetug.power_armor_mod.client.render.layers.*;
import com.jetug.power_armor_mod.common.data.enums.*;
import com.jetug.power_armor_mod.common.data.json.*;
import com.jetug.power_armor_mod.common.foundation.entity.*;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import org.apache.logging.log4j.util.*;
import org.jetbrains.annotations.*;
import software.bernie.geckolib3.core.processor.*;
import software.bernie.geckolib3.geo.render.built.*;

import java.util.*;

import static com.jetug.power_armor_mod.client.render.utils.ParticleUtils.showJetpackParticles;
import static com.jetug.power_armor_mod.common.data.constants.Bones.*;
import static net.minecraft.world.entity.EquipmentSlot.MAINHAND;
import static net.minecraft.world.entity.EquipmentSlot.OFFHAND;

public class PowerArmorRenderer extends ModGeoRenderer<PowerArmorEntity> {
    public static PowerArmorModel<PowerArmorEntity> powerArmorModel;
    public static final PowerArmorModel<PowerArmorEntity> armorModel = new PowerArmorModel<>();
    protected ItemStack mainHandItem, offHandItem;

    public PowerArmorRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new PowerArmorModel<>());
        //powerArmorModel = (PowerArmorModel<PowerArmorEntity>)getGeoModelProvider();
        //initLayers();
    }
//
//    private void initLayers(){
//        for (int i = 0; i < BodyPart.values().length; i++)
//            addLayer(new EquipmentLayer(this, BodyPart.getById(i)));
//        addLayer(new PlayerHeadLayer(this));
//    }

    @Override
    public void render(GeoModel model, PowerArmorEntity animatable,
                       float partialTick, RenderType type, PoseStack poseStack,
                       MultiBufferSource bufferSource, VertexConsumer buffer,
                       int packedLight, int packedOverlay,
                       float red, float green, float blue, float alpha) {
//
//        for (var part : animatable.equipment)
//            updateModel(animatable, part);
//
//        super.render(model, animatable, partialTick, type, poseStack, bufferSource, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

//    @Override
//    public void renderLate(PowerArmorEntity animatable, PoseStack poseStack, float partialTick, MultiBufferSource bufferSource, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float partialTicks) {
//        super.renderLate(animatable, poseStack, partialTick, bufferSource, buffer, packedLight, packedOverlay, red, green, blue, partialTicks);
//    }
//
//    @Override
//    public void renderRecursively(GeoBone bone, PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay,
//                                  float red, float green, float blue, float alpha) {
//        super.renderRecursively(bone, poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
//
//        if ((Objects.equals(bone.name, LEFT_JET_LOCATOR) ||
//                Objects.equals(bone.name, RIGHT_JET_LOCATOR))
//                && animatable.isDashing()) {
//            showJetpackParticles(getWorldPos(bone, poseStack));
//        }
//    }
//
//    @Override
//    public void renderEarly(PowerArmorEntity animatable, PoseStack poseStack, float partialTick, MultiBufferSource bufferSource,
//                            VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float partialTicks) {
//        super.renderEarly(animatable, poseStack, partialTick, bufferSource, buffer, packedLight, packedOverlay, red, green, blue, partialTicks);
//        mainHandItem = animatable.getPlayerItem(MAINHAND);
//        offHandItem  = animatable.getPlayerItem(OFFHAND);
//    }
//
//    @Nullable
//    @Override
//    protected ItemStack getHeldItemForBone(String boneName, PowerArmorEntity animatable) {
//        if(!animatable.hasPlayerPassenger()) return null;
//
//        return switch (boneName) {
//            case LEFT_HAND -> offHandItem;
//            case RIGHT_HAND-> mainHandItem;
//            default -> null;
//        };
//    }
//
//    @Override
//    protected ItemTransforms.TransformType getCameraTransformForItemAtBone(ItemStack stack, String boneName) {
//        return switch (boneName) {
//            case LEFT_HAND, RIGHT_HAND -> ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND;
//            default -> ItemTransforms.TransformType.NONE;
//        };
//    }
//
//    @Override
//    protected void preRenderItem(PoseStack stack, ItemStack item, String boneName, PowerArmorEntity currentEntity, IBone bone) {
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
//
////    @Override
////    protected ModelPart getArmorPartForBone(String name, HumanoidModel<?> armorModel) {
////        return switch (name) {
////            case "back_slot" -> powerArmorModel.getBone("");
////        };
////    }
////
////    @Override
////    protected boolean isArmorBone(GeoBone bone) {
////        return bone.getName().startsWith("jetpack");
////    }
//
//    private void updateModel(PowerArmorEntity entity, BodyPart part){
//        if(entity.isEquipmentVisible(part)) {
//            var item = entity.getEquipmentItem(part);
//            forAllAttachments(item.getSettings(), this::addModelPart);
//        }
//        else {
//            var frameSettings = ClientConfig.modResourceManager.getFrameSettings(entity.frameId);
//            if (frameSettings == null) return;
//            var attachments =  frameSettings.getAttachments(part);
//            if (attachments == null) return;
//            for (var bone : attachments.bones) returnToDefault(bone);
//        }
//    }
//
//    private void forAllAttachments(EquipmentSettings settings, TriConsumer<EquipmentAttachment, GeoBone, GeoBone> action) {
//        if(settings == null || settings.attachments == null) return;
//
//        for (var equipmentAttachment : settings.attachments) {
//            var frameBone = getFrameBone(equipmentAttachment.frame);
//            var armorBone = getArmorBone(settings.getModelLocation(), equipmentAttachment.armor);
//            if (frameBone == null || armorBone == null || equipmentAttachment.mode == null) continue;
//
//            action.accept(equipmentAttachment, frameBone, armorBone);
//        }
//    }
//
//    private void addModelPart(EquipmentAttachment equipmentAttachment, GeoBone frameBone, GeoBone armorBone) {
////        if(Objects.equals(frameBone.name, "left_upper_arm_frame"))
////            showJetpackParticles(frameBone);
//
//        switch (equipmentAttachment.mode) {
//            case ADD -> addBone(frameBone, armorBone);
//            case REPLACE -> replaceBone(frameBone, armorBone);
//        }
//    }
//
//    private void returnToDefault(String boneName){
//        var bone = getFrameBone(boneName);
//        if(bone == null) return;
//        var parentBone = getFrameBone(bone.parent.name);
//        if(parentBone == null) return;
//
//        parentBone.childBones.remove(bone);
//        parentBone.childBones.add(getFrameBone(boneName));
//    }
//
//    private static void addBone(GeoBone frameBone, GeoBone armorBone) {
//        if (frameBone.childBones.contains(armorBone)) return;
//        //armorBone.parent = frameBone;
//        frameBone.childBones.add(armorBone);
//    }
//
//    private static void replaceBone(GeoBone frameBone, GeoBone armorBone) {
//        frameBone.parent.childBones.remove(frameBone);
//        if (frameBone.parent.childBones.contains(armorBone)) return;
//        frameBone.parent.childBones.add(armorBone);
//        //armorBone.parent = frameBone.parent;
//    }
//
//    @Nullable
//    public static GeoBone getFrameBone(String name){
//        return (GeoBone)powerArmorModel.getAnimationProcessor().getBone(name);
//    }
//
//    @Nullable
//    private GeoBone getArmorBone(ResourceLocation resourceLocation, String name){
//        return armorModel.getModel(resourceLocation).getBone(name).orElse(null);
//    }
}