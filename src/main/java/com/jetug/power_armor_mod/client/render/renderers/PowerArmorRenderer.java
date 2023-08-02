package com.jetug.power_armor_mod.client.render.renderers;

import com.jetug.power_armor_mod.client.ClientConfig;
import com.jetug.power_armor_mod.client.model.*;
import com.jetug.power_armor_mod.client.model.PowerArmorModel;
import com.jetug.power_armor_mod.common.data.enums.*;
import com.jetug.power_armor_mod.common.data.json.EquipmentAttachment;
import com.jetug.power_armor_mod.common.data.json.EquipmentSettings;

import com.jetug.power_armor_mod.client.render.layers.*;
import com.jetug.power_armor_mod.common.foundation.entity.*;

import com.jetug.power_armor_mod.common.foundation.particles.Pos3D;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.Nullable;
import software.bernie.example.client.DefaultBipedBoneIdents;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.*;

import java.util.Random;

import static com.jetug.power_armor_mod.common.data.constants.Bones.*;
import static com.jetug.power_armor_mod.common.data.enums.BodyPart.BACK;
import static net.minecraft.world.entity.EquipmentSlot.*;

public class PowerArmorRenderer extends ModGeoRenderer<PowerArmorEntity> {
    private final PowerArmorModel<PowerArmorEntity> powerArmorModel;
    private final PowerArmorModel<PowerArmorEntity> armorModel = new PowerArmorModel<>();

    protected ItemStack mainHandItem, offHandItem, backItem;

    public PowerArmorRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new PowerArmorModel<>());
        powerArmorModel = (PowerArmorModel<PowerArmorEntity>)getGeoModelProvider();
        initLayers();
    }

    private void initLayers(){
        for (int i = 0; i < BodyPart.values().length; i++)
            addLayer(new ArmorPartLayer(this, BodyPart.getById(i)));
        addLayer(new PlayerHeadLayer(this));
    }

    @Override
    public void render(PowerArmorEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {
        showJetpackParticles(entity, getFrameBone("left_jet2_frame"));

        for (var part : entity.equipment)
            updateModel(entity, part);

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    private void showJetpackParticles(PowerArmorEntity entity, GeoBone bone) {
        if(bone == null) return;

        var minecraft = Minecraft.getInstance();
        var particle = ParticleTypes.FLAME;
        var rand = new Random();
        var random = (rand.nextFloat() - 0.5F) * 0.1F;
        var pos = bone.getWorldPosition();

        var playerPos = new Pos3D(pos.x,  pos.y, pos.z).translate(0, 0, 0);
        var vLeft = new Pos3D(-0.1, 0, -0.30).rotate(entity.yBodyRot, 0);
        var vRight = new Pos3D(0.1, 0, -0.30).rotate(entity.yBodyRot, 0);
        var vCenter = new Pos3D((rand.nextFloat() - 0.5F) * 0.25F, 0, -0.30).rotate(entity.yBodyRot, 0);

        var v = playerPos.translate(vLeft).translate(new Pos3D(entity.getDeltaMovement()));
        minecraft.level.addParticle(particle, v.x, v.y, v.z, random, -0.2D, random);

         v = playerPos.translate(vRight).translate(new Pos3D(entity.getDeltaMovement()));
        minecraft.level.addParticle(particle, v.x, v.y, v.z, random, -0.2D, random);

         v = playerPos.translate(vCenter).translate(new Pos3D(entity.getDeltaMovement()));
        minecraft.level.addParticle(particle, v.x, v.y, v.z, random, -0.2D, random);

       //minecraft.particleEngine.createParticle(particle, v.x, v.y, v.z, random, -0.2D, random); // alternative method
    }

    @Override
    public void renderEarly(PowerArmorEntity animatable, PoseStack poseStack, float partialTick, MultiBufferSource bufferSource,
                            VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float partialTicks) {
        super.renderEarly(animatable, poseStack, partialTick, bufferSource, buffer, packedLight, packedOverlay, red, green, blue, partialTicks);
        mainHandItem = animatable.getPlayerItem(MAINHAND);
        offHandItem  = animatable.getPlayerItem(OFFHAND);
        backItem = animatable.getEquipment(BACK);
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, PowerArmorEntity animatable) {
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
    protected void preRenderItem(PoseStack stack, ItemStack item, String boneName, PowerArmorEntity currentEntity, IBone bone) {
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

//    @Override
//    protected ModelPart getArmorPartForBone(String name, HumanoidModel<?> armorModel) {
//        return switch (name) {
//            case "back_slot" -> powerArmorModel.getBone("");
//        };
//    }
//
//    @Override
//    protected boolean isArmorBone(GeoBone bone) {
//        return bone.getName().startsWith("jetpack");
//    }

    private void updateModel(PowerArmorEntity entity, BodyPart part){
        if(entity.isEquipmentVisible(part)) {
            var item = entity.getEquipmentItem(part);
            forAllAttachments(item.getSettings(), this::addModelPart);
        }
        else {
            var frameSettings = ClientConfig.modResourceManager.getFrameSettings(entity.frameId);
            var attachments =  frameSettings.getAttachments(part);
            if (attachments == null) return;
            for (var bone : attachments.bones) returnToDefault(bone);
        }
    }

    private void forAllAttachments(EquipmentSettings settings, TriConsumer<EquipmentAttachment, GeoBone, GeoBone> action) {
        if(settings == null || settings.attachments == null) return;

        for (var equipmentAttachment : settings.attachments) {
            var frameBone = getFrameBone(equipmentAttachment.frame);
            var armorBone = getArmorBone(settings.getModelLocation(), equipmentAttachment.armor);
            if (frameBone == null || armorBone == null || equipmentAttachment.mode == null) continue;

            action.accept(equipmentAttachment, frameBone, armorBone);
        }
    }

    private void addModelPart(EquipmentAttachment equipmentAttachment, GeoBone frameBone, GeoBone armorBone) {
        switch (equipmentAttachment.mode) {
            case ADD -> addBone(frameBone, armorBone);
            case REPLACE -> replaceBone(frameBone, armorBone);
        }
    }

    private void returnToDefault(String boneName){
        var bone = getFrameBone(boneName);
        if(bone == null) return;
        var parentBone = getFrameBone(bone.parent.name);
        if(parentBone == null) return;

        parentBone.childBones.remove(bone);
        parentBone.childBones.add(getFrameBone(boneName));
    }

    private static void addBone(GeoBone frameBone, GeoBone armorBone) {
        if (frameBone.childBones.contains(armorBone)) return;
        armorBone.parent = frameBone;
        frameBone.childBones.add(armorBone);
    }

    private static void replaceBone(GeoBone frameBone, GeoBone armorBone) {
        frameBone.parent.childBones.remove(frameBone);
        if (frameBone.parent.childBones.contains(armorBone)) return;
        frameBone.parent.childBones.add(armorBone);
        armorBone.parent = frameBone.parent;
    }

    private GeoBone getFrameBone(String name){
        return (GeoBone)powerArmorModel.getAnimationProcessor().getBone(name);
    }

    private GeoBone getArmorBone(ResourceLocation resourceLocation, String name){
        return armorModel.getModel(resourceLocation).getBone(name).orElse(null);
    }
}