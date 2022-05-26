package com.jetug.power_armor_mod.client.model;

import com.jetug.power_armor_mod.common.entity.entitytype.PowerArmorEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import static com.jetug.power_armor_mod.common.util.constants.Constants.HEAD_BONE_NAME;
import static com.jetug.power_armor_mod.common.util.constants.Resources.*;

public class PowerArmorModel<Type extends PowerArmorEntity & IAnimatable> extends AnimatedGeoModel<Type>
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

//    public GeoBone getBone(BodyPart bodyPart){
//
//    }

//    public GeoBone getBoneAP(String boneName){
//        return (GeoBone) powerArmorModel.getAnimationProcessor().getBone(boneName);
//    }

//    public GeoBone getBone(String boneName){
//        Optional<GeoBone> opt = this.getModel(ARMOR_MODEL_LOCATION).getBone(boneName);
//        return opt.orElse(null);
//    }

    @Override
    public void setLivingAnimations(Type entity, Integer uniqueID, AnimationEvent customPredicate) {
//        AttributeModifierManager attr = entity.getAttributes();
//        if(attr.hasAttribute(BODY_ARMOR_HEALTH)){
//            double value = entity.getAttributes().getValue(BODY_ARMOR_HEALTH);
//            if(value == 0.0D){
//                GeoBone bone = this.getModel(POWER_ARMOR_MODEL_LOCATION).getBone("body").get();
//                bone.setHidden(true);
//            }
//        }
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        setupHeadAnimation(customPredicate);
    }

    private void setupHeadAnimation(AnimationEvent customPredicate){
        IBone head = this.getAnimationProcessor().getBone(HEAD_BONE_NAME);
        //GeoBone bone = this.registerBone().getModel(POWER_ARMOR_MODEL_LOCATION)..getBone("head_").get();

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
        head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
    }
}