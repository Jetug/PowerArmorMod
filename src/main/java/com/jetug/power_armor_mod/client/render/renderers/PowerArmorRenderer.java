package com.jetug.power_armor_mod.client.render.renderers;

import com.jetug.power_armor_mod.client.*;
import com.jetug.power_armor_mod.client.model.*;
import com.jetug.power_armor_mod.client.model.PowerArmorModel;
import com.jetug.power_armor_mod.common.json.*;
import com.jetug.power_armor_mod.client.render.layers.*;
import com.jetug.power_armor_mod.common.foundation.entity.*;
import com.jetug.power_armor_mod.common.foundation.item.*;
import com.jetug.power_armor_mod.common.util.enums.*;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import software.bernie.geckolib3.geo.render.built.*;
import software.bernie.geckolib3.renderers.geo.*;

import java.util.Map;

public class PowerArmorRenderer extends GeoEntityRenderer<PowerArmorEntity> {
    private final PowerArmorModel<PowerArmorEntity> powerArmorModel;
    private final ArmorModel<PowerArmorEntity> armorModel = new ArmorModel<>();

    private Map<Integer, Attachment> Attachments;

    public PowerArmorRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new PowerArmorModel<>());
        powerArmorModel = (PowerArmorModel<PowerArmorEntity>)getGeoModelProvider();
        initLayers();
    }

    private void initLayers(){
        for (int i = 0; i < 6; i++)
            addLayer(new ArmorPartLayer(this, BodyPart.getById(i)));
        addLayer(new PlayerHeadLayer(this));
    }

    @Override
    public void render(PowerArmorEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {
        try{
            updateArmor(entity);
            super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        } catch (Exception e) {
            throw e;
            //LOGGER.log(Level.ERROR, e);
        }
    }

    private void updateArmor(PowerArmorEntity entity){
        for (var part : entity.armorParts) {
            var itemStack = entity.getItemStack(part);
            if (!itemStack.isEmpty() && PowerArmorItem.hasArmor(itemStack)) {
                updateModel(part, itemStack, true);
            }
            else{
                updateModel(part, itemStack, false);
            }
        }
    }

    private void updateModel(BodyPart part, ItemStack slot, Boolean isAttaching){
        if(!slot.isEmpty()) {
            var item = (PowerArmorItem) slot.getItem();
            var settings = item.getPartSettings();
            if(settings == null) return;

            handleAttachments(settings, isAttaching);
        }
    }

    private void handleAttachments(ArmorPartSettings settings, Boolean isAttaching) {
        var attachments = settings.attachments;

        if(attachments == null || attachments.length == 0) return;

        for (Attachment attachment : attachments) {
            var frameBone = getFrameBone(attachment.frame);
            var armorBone = getArmorBone(settings.getModel(), attachment.armor);

            if (frameBone == null || armorBone == null) continue;

            if(isAttaching)
                addModelPart(attachment, frameBone, armorBone);
            else
                removeModelPart(attachment, frameBone, armorBone);
        }
    }

    private void addModelPart(Attachment attachment, GeoBone frameBone, GeoBone armorBone) {
        if(attachment.mode == null) return;

        switch (attachment.mode) {
            case ADD -> {
                if (!frameBone.childBones.contains(armorBone)) {
                    armorBone.parent = frameBone;
                    frameBone.childBones.add(armorBone);
                }
            }
            case REPLACE -> {
                var parentBone = frameBone.parent;
                if(!parentBone.childBones.contains(armorBone)){
                    parentBone.childBones.remove(frameBone);
                    parentBone.childBones.add(armorBone);
                    armorBone.parent = parentBone;
                }
            }
        }
    }

    private void removeModelPart(Attachment attachment, GeoBone frameBone, GeoBone armorBone) {
        if(attachment.mode == null) return;



//        var newBone = armorModel.getModel(POWER_ARMOR_MODEL_LOCATION).getBone(part.getName()).orElse(null);
//        var oldBone = (GeoBone)powerArmorModel.getBone(part.getName());
//        var parent = oldBone.parent;
//
//        parent.childBones.remove(oldBone);
//        parent.childBones.add(newBone);
//        newBone.parent = parent;

        switch (attachment.mode) {
            case ADD -> {
                frameBone.childBones.remove(armorBone);
            }
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
}