package com.jetug.chassis_core.client.render.utils;

import com.jetug.chassis_core.common.foundation.entity.WearableChassis;
import mod.azure.azurelib.cache.AzureLibCache;
import mod.azure.azurelib.cache.object.BakedGeoModel;
import mod.azure.azurelib.cache.object.GeoBone;
import mod.azure.azurelib.constant.DataTickets;
import mod.azure.azurelib.core.animatable.model.CoreGeoBone;
import mod.azure.azurelib.core.animation.AnimationProcessor;
import mod.azure.azurelib.core.animation.AnimationState;
import mod.azure.azurelib.model.data.EntityModelData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;

@SuppressWarnings({"rawtypes"})
public class GeoUtils {
    @Nullable
    public static Collection<GeoBone> getEquipmentBones(String boneName, WearableChassis animatable) {
        var result = new ArrayList<GeoBone>();
        var configs = animatable.getItemConfigs();
        for (var config : configs) {
            var boneNames = config.getArmorBone(boneName);

            for (var name : boneNames) {
                var armorBone = GeoUtils.getBone(config.getModelLocation(), name);
                if (armorBone != null) result.add(armorBone);
            }
        }
        return result;
    }


    public static void setHeadAnimation(CoreGeoBone head, AnimationState animationState) {
        if (head == null) return;
        var data = (EntityModelData) animationState.getExtraData().get(DataTickets.ENTITY_MODEL_DATA);
        head.setRotX(data.headPitch() * ((float) Math.PI / 180F));
        head.setRotY(data.netHeadYaw() * ((float) Math.PI / 180F));
    }

    public static void setHeadAnimation(LivingEntity animatable, AnimationProcessor animationProcessor, AnimationState animationState) {
        var head = animationProcessor.getBone("head");
        if (head == null) return;
        setHeadAnimation(head, animationState);
    }

    public static BakedGeoModel getModel(ResourceLocation location) {
        return AzureLibCache.getBakedModels().get(location);
    }

//    @Nullable
//    public static GeoBone getFrameBone(AnimatedGeoModel provider, String name){
//        return (GeoBone) provider.getAnimationProcessor().getBone(name);
//    }

    @Nullable
    public static GeoBone getBone(ResourceLocation resourceLocation, String name) {
        var model = getModel(resourceLocation);
        return model == null ? null : model.getBone(name).orElse(null);
    }
}
