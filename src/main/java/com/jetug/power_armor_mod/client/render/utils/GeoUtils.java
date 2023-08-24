package com.jetug.power_armor_mod.client.render.utils;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.resource.GeckoLibCache;

public class GeoUtils {
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
        //armorBone.parent = frameBone;
        frameBone.childBones.add(armorBone);
    }

    public static void replaceBone(GeoBone frameBone, GeoBone armorBone) {
        frameBone.parent.childBones.remove(frameBone);
        if (frameBone.parent.childBones.contains(armorBone)) return;
        frameBone.parent.childBones.add(armorBone);
        //armorBone.parent = frameBone.parent;
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
