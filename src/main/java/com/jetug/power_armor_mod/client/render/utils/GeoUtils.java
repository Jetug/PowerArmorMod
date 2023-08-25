package com.jetug.power_armor_mod.client.render.utils;

import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3d;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;
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
        frameBone.childBones.add(armorBone);
    }

    public static void replaceBone(GeoBone frameBone, GeoBone armorBone) {
//        var x = frameBone.getPivotX();
//        var y = frameBone.getPivotY();
//        var z = frameBone.getPivotZ();
//
//        armorBone.setPivotX(x);
//        armorBone.setPivotY(y);
//        armorBone.setPivotZ(z);

//        armorBone.childCubes.forEach((cube) -> {
//            cube.pivot = new Vector3f(x,y,z);
//        });
//
//        armorBone.setWorldSpaceXform(frameBone.getWorldSpaceXform());
//        armorBone.setModelPosition(frameBone.getModelPosition());

//        var orig = armorBone.getPosition();
//        var vec = frameBone.getPosition();
//        var base = new Vector3d(0,0,0);
//        base.add(vec);
//        armorBone.setPosition(base);
//        orig = armorBone.getPosition();
//
//        var t = orig;
//        setModelPosition(armorBone, frameBone.getModelPosition());

        frameBone.parent.childBones.remove(frameBone);
        if (frameBone.parent.childBones.contains(armorBone)) return;
        frameBone.parent.childBones.add(armorBone);
    }

    public static void setModelPosition(GeoBone geoBone, Vector3d pos) {
        /* Doesn't work on bones with parent transforms */
        GeoBone parent = geoBone.getParent();
        Matrix4f identity = new Matrix4f();
        identity.setIdentity();
        Matrix4f matrix = parent.getModelSpaceXform().copy(); // parent == null ? identity : parent.getModelSpaceXform().copy();
        matrix.invert();
        Vector4f vec = new Vector4f(-(float) pos.x / 16f, (float) pos.y / 16f, (float) pos.z / 16f, 1);
        vec.transform(matrix);
        geoBone.setPosition(-vec.x() * 16f, vec.y() * 16f, vec.z() * 16f);
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
