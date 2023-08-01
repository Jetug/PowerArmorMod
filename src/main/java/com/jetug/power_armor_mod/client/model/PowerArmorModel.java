package com.jetug.power_armor_mod.client.model;

import com.jetug.power_armor_mod.common.foundation.entity.PowerArmorEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import static com.jetug.power_armor_mod.common.data.constants.Bones.HEAD_BONE_NAME;
import static com.jetug.power_armor_mod.common.data.constants.Resources.*;

public class PowerArmorModel<Type extends PowerArmorEntity & IAnimatable> extends AnimatedGeoModel<Type>
{
    public PowerArmorModel(){
        super();
    }

    @Override
    public ResourceLocation getModelLocation(Type object) {
        return FRAME_MODEL_LOCATION;
    }

    @Override
    public ResourceLocation getTextureLocation(Type object) {
        return FRAME_TEXTURE_LOCATION;
    }

    @Override
    public ResourceLocation getAnimationFileLocation(Type object) {
        return FRAME_ANIMATION_LOCATION;
    }

    @Override
    public void setCustomAnimations(Type animatable, int instanceId, AnimationEvent animationEvent) {
        super.setCustomAnimations(animatable, instanceId, animationEvent);
        setupHeadAnimation(animationEvent);
    }

    private void setupHeadAnimation(AnimationEvent customPredicate){
        IBone head = this.getAnimationProcessor().getBone(HEAD_BONE_NAME);
        //GeoBone bone = this.registerBone().getModelLocation(FRAME_MODEL_LOCATION)..getBone("head").get();

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
        head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
    }
}