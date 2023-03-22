package com.jetug.power_armor_mod.client.render.renderers;

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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import org.apache.logging.log4j.util.TriConsumer;
import software.bernie.geckolib3.geo.render.built.*;
import software.bernie.geckolib3.renderers.geo.*;

import java.util.HashMap;
import java.util.Map;

public class PowerArmorRenderer extends GeoEntityRenderer<PowerArmorEntity> {
    private final PowerArmorModel<PowerArmorEntity> powerArmorModel;
    private final ArmorModel<PowerArmorEntity> armorModel = new ArmorModel<>();
    private final Map<Integer, Map<BodyPart, ArmorPartSettings>> settingsHistory = new HashMap<>();

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
        for (int i = 0; i < 6; i++)
            addLayer(new ArmorPartLayer(this, BodyPart.getById(i)));
        addLayer(new PlayerHeadLayer(this));
    }

    @Override
    public void render(PowerArmorEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {
        updateArmor(entity);
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    private void updateArmor(PowerArmorEntity entity){
        for (var part : entity.armorParts) {
            //var isAttaching = !itemStack.isEmpty() && PowerArmorItem.hasArmor(itemStack);
            updateModel(entity, part, entity.getItemStack(part));
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void updateModel(PowerArmorEntity entity, BodyPart part, ItemStack itemStack){
        if(!itemStack.isEmpty()) {
            var item = (PowerArmorItem) itemStack.getItem();
            var settings = item.getPartSettings();
            putSettings(entity, settings);
            forAllAttachments(settings, this::addModelPart);
        }
        else {
            var settings = getSettings(entity, part);
            forAllAttachments(settings, this::removeModelPart);
        }
    }


    private void forAllAttachments(ArmorPartSettings settings, TriConsumer<Attachment, GeoBone, GeoBone> action) {
        if(settings == null || settings.attachments == null) return;

        for (Attachment attachment : settings.attachments) {
            var frameBone = getFrameBone(attachment.frame);
            var armorBone = getArmorBone(settings.getModel(), attachment.armor);
            if (frameBone == null || armorBone == null || attachment.mode == null) continue;

            action.accept(attachment, frameBone, armorBone);
        }
    }

    private void addModelPart(Attachment attachment, GeoBone frameBone, GeoBone armorBone) {
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
        switch (attachment.mode) {
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
    private void putSettings(Entity entity, ArmorPartSettings settings){
        var entry = settingsHistory.get(entity.getId());

        if(entry == null){
            var buff = new HashMap<BodyPart, ArmorPartSettings>();
            buff.put(settings.part, settings);
            settingsHistory.put(entity.getId(), buff);
        }
        else entry.put(settings.part, settings);
    }

    private ArmorPartSettings getSettings(Entity entity, BodyPart bodyPart){
        var entry = settingsHistory.get(entity.getId());
        if (entry == null) return null;
        return entry.get(bodyPart);
    }
}