package com.jetug.power_armor_mod.client.render.renderers;

import com.jetug.power_armor_mod.client.ClientConfig;
import com.jetug.power_armor_mod.client.model.*;
import com.jetug.power_armor_mod.client.model.PowerArmorModel;
import com.jetug.power_armor_mod.common.data.enums.*;
import com.jetug.power_armor_mod.common.data.json.EquipmentAttachment;
import com.jetug.power_armor_mod.common.data.json.EquipmentSettings;

import com.jetug.power_armor_mod.client.render.layers.*;
import com.jetug.power_armor_mod.common.foundation.entity.*;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.resources.*;
import org.apache.logging.log4j.util.TriConsumer;
import software.bernie.geckolib3.geo.render.built.*;
import software.bernie.geckolib3.renderers.geo.*;

import static com.jetug.power_armor_mod.common.foundation.screen.menu.PowerArmorMenu.*;

public class PowerArmorRenderer extends GeoEntityRenderer<PowerArmorEntity> {
    private final PowerArmorModel<PowerArmorEntity> powerArmorModel;
    private final ArmorModel<PowerArmorEntity> armorModel = new ArmorModel<>();

    public PowerArmorRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new PowerArmorModel<>());
        powerArmorModel = (PowerArmorModel<PowerArmorEntity>)getGeoModelProvider();
        initLayers();
    }

    private void initLayers(){
        for (int i = 0; i < SIZE; i++)
            addLayer(new ArmorPartLayer(this, BodyPart.getById(i)));
        addLayer(new PlayerHeadLayer(this));
    }

    @Override
    public void render(PowerArmorEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {
        for (var part : entity.equipment)
            updateModel(entity, part);
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

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