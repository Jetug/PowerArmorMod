package com.jetug.chassis_core.client.render.utils;

import com.jetug.chassis_core.common.data.json.*;
import com.jetug.chassis_core.common.foundation.entity.WearableChassis;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.resource.GeckoLibCache;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("rawtypes")
public class GeoUtils {
    public static void renderEquipment(AnimatedGeoModel provider, WearableChassis entity, String part, boolean isPov){
        if(entity.isEquipmentVisible(part)) {
            var item = entity.getEquipmentItem(part);
            addModelPart(provider, item.getSettings(), isPov);
        }
        else {
            removeModelPart(provider, entity.getSettings(), part);
        }
    }

    public static void addModelPart(AnimatedGeoModel provider, EquipmentConfig settings, boolean isPov) {
        if(settings == null) return;
        var attachments = isPov ? settings.pov : settings.attachments;

        for (var equipmentAttachment : attachments) {
            var frameBone = getFrameBone(provider, equipmentAttachment.frame);
            var armorBone = getArmorBone(settings.getModelLocation(), equipmentAttachment.armor);
            if (frameBone == null || armorBone == null || equipmentAttachment.mode == null) continue;

            switch (equipmentAttachment.mode) {
                case ADD -> addBone(provider, frameBone, armorBone);
                case REPLACE -> replaceBone(provider, frameBone, armorBone);
            }
        }
    }

    public static void removeModelPart(AnimatedGeoModel provider, FrameConfig frameConfig, String part){
        if (frameConfig == null) return;
        var attachments =  frameConfig.getAttachments(part);
        if (attachments == null) return;

        for (var bone : attachments.bones)
            returnToDefault(provider, bone, List.of(attachments.bones));
    }

    public static GeoModel getModel(ResourceLocation location){
        return GeckoLibCache.getInstance().getGeoModels().get(location);
    }

    public static void returnToDefault(AnimatedGeoModel provider, String boneName, List bones){
        var bone = getFrameBone(provider, boneName);
        if(bone == null) return;
        var parentBone = getFrameBone(provider, bone.parent.name);
        if(parentBone == null) return;

        parentBone.childBones.remove(bone);
        var buffBone = getFrameBone(provider, boneName);

        //for (int i = 0; i < parentBone.childBones.size(); i++)
//
//        var toRemove = new ArrayList<GeoBone>();
//        for (var child : parentBone.childBones){
//            if(!bones.contains(child.name)){
//                toRemove.add(child);
//            }
//        }

        //parentBone.childBones = new ObjectArrayList<>();
        parentBone.childBones.add(buffBone);
    }

    public static void addBone(AnimatedGeoModel provider, GeoBone frameBone, GeoBone armorBone) {
        //returnToDefault(provider, frameBone.name);

        //return;
        frameBone.childBones.remove(armorBone);
        frameBone.childBones.add(armorBone);
    }

    public static void replaceBone(AnimatedGeoModel provider, GeoBone frameBone, GeoBone armorBone) {
        //returnToDefault(provider, frameBone.name);

        frameBone.parent.childBones.remove(frameBone);
        //if (frameBone.parent.childBones.contains(armorBone)) return;
        frameBone.parent.childBones.remove(armorBone);
        frameBone.parent.childBones.add(armorBone);
    }

    @Nullable
    public static GeoBone getFrameBone(AnimatedGeoModel provider, String name){
        return (GeoBone) provider.getAnimationProcessor().getBone(name);
    }

    @Nullable
    public static GeoBone getArmorBone(ResourceLocation resourceLocation, String name){
        return getModel(resourceLocation).getBone(name).orElse(null);
    }
}
