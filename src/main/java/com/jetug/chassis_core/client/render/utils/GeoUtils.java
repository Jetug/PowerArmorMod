package com.jetug.chassis_core.client.render.utils;

import com.jetug.chassis_core.common.foundation.entity.*;
import com.mojang.math.Vector3f;
import net.minecraft.resources.*;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.*;
import software.bernie.geckolib3.geo.render.built.*;
import software.bernie.geckolib3.model.*;
import software.bernie.geckolib3.resource.*;
import java.util.*;

@SuppressWarnings({"rawtypes"})
public class GeoUtils {
    @Nullable
    public static Collection<GeoBone> getEquipmentBones(String boneName, WearableChassis animatable) {
        var result = new ArrayList<GeoBone>();
        var configs = animatable.getItemConfigs();
        for (var config: configs) {
            var boneNames = config.getArmorBone(boneName);

            for (var name : boneNames) {
                var armorBone = GeoUtils.getArmorBone(config.getModelLocation(), name);
                result.add(armorBone);
            }
        }
        return result;
    }

    public static GeoModel getModel(ResourceLocation location){
        return GeckoLibCache.getInstance().getGeoModels().get(location);
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
