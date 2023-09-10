package com.jetug.chassis_core.client.render.utils;

import com.jetug.chassis_core.common.data.json.*;
import com.jetug.chassis_core.common.foundation.entity.*;
import net.minecraft.client.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.*;
import software.bernie.geckolib3.geo.render.built.*;
import software.bernie.geckolib3.model.*;
import software.bernie.geckolib3.resource.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jetug.chassis_core.client.ClientConfig.*;

@SuppressWarnings({"rawtypes", "unchecked"})
public class GeoUtils {
    private static final Map<EntityType, GeoModel> models = new HashMap<>();

    public static void renderEquipment(AnimatedGeoModel provider, WearableChassis entity,
                                       String part, boolean isPov){
        if(entity.isEquipmentVisible(part)) {
            var item = entity.getEquipmentItem(part);
            addModelPart(provider, item.getConfig(), isPov);
        }
        else removeModelPart(provider, entity, part);
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

    public static void removeModelPart(AnimatedGeoModel provider, WearableChassis chassis, String part){
        var chassisConfig = chassis.getSettings();
        if (chassisConfig == null) return;
        var attachments =  chassisConfig.getAttachments(part);
        if (attachments == null) return;

        try{
            GeoModel rawModel = getGeoModel(provider, chassis);
            for (var bone : attachments.bones)
                returnToDefault(provider, bone, rawModel);
        }
        catch (Exception ignored){}
    }

    private static GeoModel getGeoModel(AnimatedGeoModel provider, WearableChassis chassis) {
//        var rawModel = models.get(chassis.getType());
//        if(rawModel == null){
//            var chassisModel = provider.getModelLocation(chassis);
//            var manager = Minecraft.getInstance().getResourceManager();
//            rawModel = modelLoader.loadModel(manager, chassisModel);
//
//            models.put(chassis.getType(), rawModel);
//        }

        var chassisModel = provider.getModelLocation(chassis);
        var manager = Minecraft.getInstance().getResourceManager();
        var rawModel = modelLoader.loadModel(manager, chassisModel);

        return rawModel;
    }

    public static void returnToDefault(AnimatedGeoModel provider, String boneName, GeoModel geoModel){
        var bone = getFrameBone(provider, boneName);
        if(bone == null) return;
        var parentBone = getFrameBone(provider, bone.parent.name);
        if(parentBone == null) return;

        parentBone.childBones.remove(bone);
        var buffBone = getFrameBone(provider, boneName);
        var toRemove = new ArrayList<GeoBone>();

        for (var child : parentBone.childBones){
            if(geoModel.getBone(child.name).isEmpty()){
                toRemove.add(child);
            }
        }

        parentBone.childBones.removeAll(toRemove);

        //parentBone.childBones = new ObjectArrayList<>();
        parentBone.childBones.add(buffBone);
    }

    public static List<GeoBone> getAllBones(List<GeoBone> inputList) {
        List<GeoBone> values = new ArrayList<>();

        for (GeoBone bone : inputList) {
            if (bone.childBones.size() > 0) {
                values.addAll(getAllBones(bone.childBones));
            } else {
                values.add(bone);
            }
        }

        return values;
    }

    public static GeoModel getModel(ResourceLocation location){
        return GeckoLibCache.getInstance().getGeoModels().get(location);
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
