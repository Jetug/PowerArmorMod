package com.jetug.power_armor_mod.client.render.renderers;

import com.jetug.power_armor_mod.client.ClientConfig;
import com.jetug.power_armor_mod.client.model.*;
import com.jetug.power_armor_mod.client.model.PowerArmorModel;
import com.jetug.power_armor_mod.common.json.*;
import com.jetug.power_armor_mod.client.render.layers.*;
import com.jetug.power_armor_mod.common.foundation.entity.*;
import com.jetug.power_armor_mod.common.util.enums.*;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.model.TridentModel;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.TridentItem;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import org.apache.logging.log4j.util.TriConsumer;
import software.bernie.geckolib3.geo.render.built.*;
import software.bernie.geckolib3.renderers.geo.*;

import java.util.HashMap;
import java.util.Map;

import static com.jetug.power_armor_mod.client.gui.PowerArmorContainer.*;

public class PowerArmorRenderer extends GeoEntityRenderer<PowerArmorEntity> {
    private final PowerArmorModel<PowerArmorEntity> powerArmorModel;
    private final ArmorModel<PowerArmorEntity> armorModel = new ArmorModel<>();
    private final Map<Integer, Map<BodyPart, EquipmentSettings>> settingsHistory = new HashMap<>();

    public PowerArmorRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new PowerArmorModel<>());
        powerArmorModel = (PowerArmorModel<PowerArmorEntity>)getGeoModelProvider();
        initLayers();

        MinecraftForge.EVENT_BUS.addListener(this::onLivingDeath);
    }

    private void onLivingDeath(final LivingDeathEvent event) {
        if(event.getEntityLiving() instanceof PowerArmorEntity){
            settingsHistory.remove(event.getEntityLiving().getId());
        }
    }

    private void initLayers(){
        for (int i = 0; i < SIZE; i++)
            addLayer(new ArmorPartLayer(this, BodyPart.getById(i)));
        addLayer(new PlayerHeadLayer(this));
    }

    @Override
    public void render(PowerArmorEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {
        updateModel(entity);
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    private void updateModel(PowerArmorEntity entity){
        for (var part : entity.equipment) {
            updateModel(entity, part);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void updateModel(PowerArmorEntity entity, BodyPart part){
        if(entity.isEquipmentVisible(part)) {
            var item = entity.getEquipmentItem(part);
            var settings = item.getPartSettings();
            forAllAttachments(settings, this::addModelPart);
        }
        else {
            var frameSettings = ClientConfig.resourceManager.getFrameSettings("power_armor_frame");
            for (var bone : frameSettings.getAttachments(part).bones){
                returnToDefault(bone);
            }
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

    private void forAllAttachments(EquipmentSettings settings, TriConsumer<EquipmentAttachment, GeoBone, GeoBone> action) {
        if(settings == null || settings.attachments == null) return;

        for (EquipmentAttachment equipmentAttachment : settings.attachments) {
            var frameBone = getFrameBone(equipmentAttachment.frame);
            var armorBone = getArmorBone(settings.getModel(), equipmentAttachment.armor);
            if (frameBone == null || armorBone == null || equipmentAttachment.mode == null) continue;

            action.accept(equipmentAttachment, frameBone, armorBone);
        }
    }

    private void addModelPart(EquipmentAttachment equipmentAttachment, GeoBone frameBone, GeoBone armorBone) {
        switch (equipmentAttachment.mode) {
            case ADD -> {
                if (!frameBone.childBones.contains(armorBone)) {
                    armorBone.parent = frameBone;
                    frameBone.childBones.add(armorBone);
                }
            }
            case REPLACE -> {
                var parentBone = frameBone.parent;
                parentBone.childBones.remove(frameBone);
                if(!parentBone.childBones.contains(armorBone)){
                    parentBone.childBones.add(armorBone);
                    armorBone.parent = parentBone;
                }
            }
        }
    }

    private void removeModelPart(EquipmentAttachment equipmentAttachment, GeoBone frameBone, GeoBone armorBone) {
        switch (equipmentAttachment.mode) {
            case ADD -> frameBone.childBones.remove(armorBone);
            case REPLACE -> {
                var parentBone = frameBone.parent;
                parentBone.childBones.remove(armorBone);

                if(!parentBone.childBones.contains(frameBone)){
                    parentBone.childBones.add(frameBone);
                    frameBone.parent = parentBone;
                }
            }
        }
    }

    private GeoBone getFrameBone(String name){
        return (GeoBone)powerArmorModel.getAnimationProcessor().getBone(name);
    }

    private GeoBone getArmorBone(ResourceLocation resourceLocation, String name){
        return armorModel.getModel(resourceLocation).getBone(name).orElse(null);
    }
//    private void putSettings(Entity entity, EquipmentSettings settings){
//        var entry = settingsHistory.get(entity.getId());
//
//        if(entry == null){
//            var buff = new HashMap<BodyPart, EquipmentSettings>();
//            buff.put(settings.part, settings);
//            settingsHistory.put(entity.getId(), buff);
//        }
//        else entry.put(settings.part, settings);
//    }
//
//    private EquipmentSettings getSettings(Entity entity, BodyPart bodyPart){
//        var entry = settingsHistory.get(entity.getId());
//        if (entry == null) return null;
//        return entry.get(bodyPart);
//    }
}