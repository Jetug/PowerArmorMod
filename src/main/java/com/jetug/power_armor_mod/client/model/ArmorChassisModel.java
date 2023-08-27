package com.jetug.power_armor_mod.client.model;

import com.jetug.generated.resources.Animations;
import com.jetug.generated.resources.Models;
import com.jetug.generated.resources.Textures;
import com.jetug.power_armor_mod.common.foundation.entity.WearableChassis;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import static com.jetug.power_armor_mod.client.render.utils.ResourceHelper.getResourceLocation;
import static com.jetug.power_armor_mod.common.data.constants.Bones.*;

//@SuppressWarnings({"unchecked"})
//public class ArmorChassisModel<Type extends WearableChassis & IAnimatable> extends AnimatedGeoModel<Type>
//{
//    public ArmorChassisModel(){
//        super();
//    }
//
//    @Override
//    public ResourceLocation getModelLocation(Type object) {
//        if(object == null) return Models.ERROR;
//        return getResourceLocation(object,"geo/chassis/", ".geo.json");
//    }
//
//    @Override
//    public ResourceLocation getTextureLocation(Type object) {
//        if(object == null) return Textures.ENTITY_ERROR;
//        return getResourceLocation(object,"textures/entity/", ".png");
//    }
//
//    @Override
//    public ResourceLocation getAnimationFileLocation(Type object) {
//        if(object == null) return Animations.ERROR;
//        return getResourceLocation(object,"animations/", ".animation.json");
//    }
//
//    @Override
//    public void setCustomAnimations(Type animatable, int instanceId, AnimationEvent animationEvent) {
//        super.setCustomAnimations(animatable, instanceId, animationEvent);
//        var head = this.getAnimationProcessor().getBone(HEAD_BONE_NAME);
//        if(head == null) return;
//        var extraData = (EntityModelData) animationEvent.getExtraDataOfType(EntityModelData.class).get(0);
//        head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
//        head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
//    }
//}

@SuppressWarnings({"rawtypes", "unchecked"})
public class ArmorChassisModel<Type extends WearableChassis & IAnimatable> extends AnimatedGeoModel<Type>
{
    public ArmorChassisModel(){
        super();
    }

    @Override
    public ResourceLocation getModelLocation(Type object) {
        if(object == null) return Models.ERROR;
        return getResourceLocation(object,"geo/chassis/", ".geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(Type object) {
        if(object == null) return Textures.ENTITY_ERROR;
        return getResourceLocation(object,"textures/entity/", ".png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(Type object) {
        if(object == null) return Animations.ERROR;
        return getResourceLocation(object,"animations/", ".animation.json");
    }

    @Override
    public void setCustomAnimations(Type animatable, int instanceId, AnimationEvent animationEvent) {
        super.setCustomAnimations(animatable, instanceId, animationEvent);
        var head = this.getAnimationProcessor().getBone(HEAD_BONE_NAME);
        if(head == null) return;
        var extraData = (EntityModelData) animationEvent.getExtraDataOfType(EntityModelData.class).get(0);
        head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
        head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
    }
}