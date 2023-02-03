package com.jetug.power_armor_mod.client.render.renderers;

import com.jetug.power_armor_mod.client.ClientConfig;
import com.jetug.power_armor_mod.client.model.ArmorModel;
import com.jetug.power_armor_mod.client.model.PowerArmorModel;
import com.jetug.power_armor_mod.client.render.Attachment;
import com.jetug.power_armor_mod.client.render.layers.ArmorPartLayer;
import com.jetug.power_armor_mod.client.render.layers.PlayerHeadLayer;
import com.jetug.power_armor_mod.common.minecraft.entity.PowerArmorEntity;
import com.jetug.power_armor_mod.common.minecraft.item.PowerArmorItem;
import com.jetug.power_armor_mod.common.util.enums.BodyPart;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class PowerArmorRenderer extends GeoEntityRenderer<PowerArmorEntity> {
    private final PowerArmorModel<PowerArmorEntity> powerArmorModel;
    private final ArmorModel<PowerArmorEntity> armorModel = new ArmorModel<>();

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
        for (var part : entity.parts) {
            var itemStack = entity.getItem(part);
            if (!itemStack.isEmpty() && PowerArmorItem.hasArmor(itemStack)) {
                updateModel(part, itemStack, true);
                //attachBones(itemStack);
            }
            else{
                updateModel(part, itemStack, false);
                //detachBones(part);
            }
        }
    }

//    private void attachBones(ItemStack slot){
//        updateModel(slot, true);
//    }
//
//    private void detachBones(ItemStack slot){
//        updateModel(slot, false);
//    }

    private void updateModel(BodyPart part, ItemStack slot, Boolean isAttaching){
        //var item = (PowerArmorItem)slot.getItem();
        //var settings = item.armorPartSettings;
        var settings = ClientConfig.resourceManager.getPartSettings(part);
        if(settings == null) return;

        var attachments = settings.attachments;

        if(attachments == null && attachments.length == 0) return;


        for (Attachment attachment : attachments) {

            var frameBone = getFrameBone(attachment.frame);
            var armorBone = getArmorBone(settings.getModel(), attachment.armor);

            if (frameBone == null || armorBone == null) continue;

            if(isAttaching)
                addModelPart(attachment, frameBone, armorBone);
            else {
                removeModelPart(attachment, frameBone, armorBone);
            }
        }
    }

    private static void addModelPart(Attachment attachment, GeoBone frameBone, GeoBone armorBone) {
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

    private static void removeModelPart(Attachment attachment, GeoBone frameBone, GeoBone armorBone) {
        if(attachment.mode == null) return;

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

//    private void handleModel(ItemStack slot, Boolean isAttaching){
//        var item = (PowerArmorItem)slot.getItem();
//        var attachments = item.armorPartSettings.attachments;
//
//        for (Attachment attachment : attachments) {
//
//            var model = item.armorPartSettings.getModel();
//
//            var frameBone = (GeoBone)powerArmorModel.getAnimationProcessor().getBone(attachment.frame);
//            var armorBone = armorModel.getModel(model).getBone(attachment.armor).orElse(null);
//
//            if(frameBone != null && armorBone != null) {
//                if (isAttaching) {
//                    if(!frameBone.childBones.contains(armorBone)) {
//                        armorBone.parent = frameBone;
//                        frameBone.childBones.add(armorBone);
//                    }
//                }
//                else frameBone.childBones.remove(armorBone);
//            }
//        }
//
//        slot.getAttachments().forEach(tuple ->{
//            ResourceLocation model = ResourceHelper.getModel(slot.getArmorPart(), slot.getType());
//            GeoBone frameBone = (GeoBone)powerArmorModel.getAnimationProcessor().getBone(tuple.getA());
//            GeoBone armorBone = armorModel.getModel(model).getBone(tuple.getB()).orElse(null);
//
//            if(frameBone != null && armorBone != null) {
//                if (isAttaching) {
//                    if(!frameBone.childBones.contains(armorBone)) {
//                        armorBone.parent = frameBone;
//                        frameBone.childBones.add(armorBone);
//                    }
//                }
//                else frameBone.childBones.remove(armorBone);
//            }
//        });
//    }
}