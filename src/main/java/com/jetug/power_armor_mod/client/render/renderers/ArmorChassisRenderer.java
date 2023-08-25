package com.jetug.power_armor_mod.client.render.renderers;

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
import net.minecraft.world.item.*;
import org.jetbrains.annotations.*;
import software.bernie.geckolib3.core.processor.*;
import software.bernie.geckolib3.geo.render.built.*;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import java.util.*;

import static com.jetug.power_armor_mod.client.render.utils.GeoUtils.*;
import static com.jetug.power_armor_mod.client.render.utils.ParticleUtils.showJetpackParticles;
import static com.jetug.power_armor_mod.common.data.constants.Bones.*;
import static net.minecraft.world.entity.EquipmentSlot.MAINHAND;
import static net.minecraft.world.entity.EquipmentSlot.OFFHAND;

public class ArmorChassisRenderer extends ModGeoRenderer<ArmorChassisEntity> {
    public static ArmorChassisModel<ArmorChassisEntity> armorChassisModel;
    public static final ArmorChassisModel<ArmorChassisEntity> armorModel = new ArmorChassisModel<>();
    protected ItemStack mainHandItem, offHandItem;

    public ArmorChassisRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ArmorChassisModel<>());
        armorChassisModel = (ArmorChassisModel<ArmorChassisEntity>)getGeoModelProvider();
        initLayers();
    }

    private void initLayers(){
        for (int i = 0; i < BodyPart.values().length; i++)
            addLayer(new EquipmentLayer(this, BodyPart.getById(i)));
        addLayer(new PlayerHeadLayer(this));
    }

    @Override
    public void render(GeoModel model, ArmorChassisEntity animatable,
                       float partialTick, RenderType type, PoseStack poseStack,
                       MultiBufferSource bufferSource, VertexConsumer buffer,
                       int packedLight, int packedOverlay,
                       float red, float green, float blue, float alpha) {

        if (animatable.isInvisible()) return;
        for (var part : animatable.equipment)
            renderEquipment(armorChassisModel ,animatable, part, false);

        super.render(model, animatable, partialTick, type, poseStack, bufferSource, buffer,
                packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void renderRecursively(GeoBone bone, PoseStack poseStack, VertexConsumer buffer,
                                  int packedLight, int packedOverlay,
                                  float red, float green, float blue, float alpha) {
        super.renderRecursively(bone, poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);

        if ((Objects.equals(bone.name, LEFT_JET_LOCATOR) || Objects.equals(bone.name, RIGHT_JET_LOCATOR))
                && animatable.isDashing()) {
            showJetpackParticles(getWorldPos(bone, poseStack));
        }
    }

    @Override
    public void renderEarly(ArmorChassisEntity animatable, PoseStack poseStack, float partialTick, MultiBufferSource bufferSource,
                            VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float partialTicks) {
        super.renderEarly(animatable, poseStack, partialTick, bufferSource, buffer, packedLight, packedOverlay, red, green, blue, partialTicks);
        mainHandItem = animatable.getPlayerItem(MAINHAND);
        offHandItem  = animatable.getPlayerItem(OFFHAND);
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, ArmorChassisEntity animatable) {
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
    protected void preRenderItem(PoseStack stack, ItemStack item, String boneName, ArmorChassisEntity currentEntity, IBone bone) {
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

    public static void renderEquipment(AnimatedGeoModel provider, ArmorChassisEntity entity, BodyPart part, boolean isPov){
        if(entity.isEquipmentVisible(part)) {
            var item = entity.getEquipmentItem(part);
            addModelPart(provider, item.getSettings(), isPov);
        }
        else {
            removeModelPart(provider, entity.getSettings(), part);
        }
    }

    public static void addModelPart(AnimatedGeoModel provider, EquipmentSettings settings, boolean isPov) {
        if(settings == null) return;
        var attachments = isPov ? settings.pov : settings.attachments;

        for (var equipmentAttachment : attachments) {
            var frameBone = getFrameBone(provider, equipmentAttachment.frame);
            var armorBone = getArmorBone(settings.getModelLocation(), equipmentAttachment.armor);
            if (frameBone == null || armorBone == null || equipmentAttachment.mode == null) continue;

            switch (equipmentAttachment.mode) {
                case ADD -> addBone(frameBone, armorBone);
                case REPLACE -> replaceBone(frameBone, armorBone);
            }
        }
    }

    public static void removeModelPart(AnimatedGeoModel provider, FrameSettings settings,
                                       BodyPart part){
        if (settings == null) return;
        var attachments =  settings.getAttachments(part);
        if (attachments == null) return;
        for (var bone : attachments.bones)
            returnToDefault(provider, bone);
    }
}