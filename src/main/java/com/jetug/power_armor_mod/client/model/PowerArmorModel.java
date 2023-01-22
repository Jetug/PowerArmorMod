package com.jetug.power_armor_mod.client.model;

import com.jetug.power_armor_mod.common.minecraft.entity.IPowerArmor;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import static com.jetug.power_armor_mod.common.util.constants.Bones.HEAD_BONE_NAME;
import static com.jetug.power_armor_mod.common.util.constants.Resources.*;

public class PowerArmorModel<Type extends IPowerArmor & IAnimatable> extends AnimatedGeoModel<Type>
{
    public PowerArmorModel(){
        super();
    }

    @Override
    public ResourceLocation getModelLocation(Type object) {
        return POWER_ARMOR_MODEL_LOCATION;
    }

    @Override
    public ResourceLocation getTextureLocation(Type object) {
        return POWER_ARMOR_TEXTURE_LOCATION;
    }

    @Override
    public ResourceLocation getAnimationFileLocation(Type object) {
        return POWER_ARMOR_ANIMATION_LOCATION;
    }

    @Override
    public void setCustomAnimations(Type animatable, int instanceId, AnimationEvent animationEvent) {
        super.setCustomAnimations(animatable, instanceId, animationEvent);
        setupHeadAnimation(animationEvent);
    }

    private void setupHeadAnimation(AnimationEvent customPredicate){
        IBone head = this.getAnimationProcessor().getBone(HEAD_BONE_NAME);
        //GeoBone bone = this.registerBone().getModel(POWER_ARMOR_MODEL_LOCATION)..getBone("head").get();

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
        head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
    }
}