package com.jetug.chassis_core.client.render.utils;

import com.jetug.chassis_core.common.data.enums.BodyPart;
import com.jetug.chassis_core.common.data.json.EquipmentSettings;
import com.jetug.chassis_core.common.data.json.FrameSettings;
import com.jetug.chassis_core.common.foundation.entity.WearableChassis;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3d;
import com.mojang.math.Vector4f;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.resource.GeckoLibCache;

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

    public static GeoModel getModel(ResourceLocation location){
        return GeckoLibCache.getInstance().getGeoModels().get(location);
    }

    public static void returnToDefault(AnimatedGeoModel provider, String boneName){
        var bone = getFrameBone(provider, boneName);
        if(bone == null) return;
        var parentBone = getFrameBone(provider, bone.parent.name);
        if(parentBone == null) return;

        parentBone.childBones.remove(bone);
        parentBone.childBones.add(getFrameBone(provider, boneName));
    }

    public static void addBone(GeoBone frameBone, GeoBone armorBone) {
        if (frameBone.childBones.contains(armorBone)) return;
        frameBone.childBones.add(armorBone);
    }

    public static void replaceBone(GeoBone frameBone, GeoBone armorBone) {
        frameBone.parent.childBones.remove(frameBone);
        if (frameBone.parent.childBones.contains(armorBone)) return;
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
