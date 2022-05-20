package com.jetug.power_armor_mod.client.render.renderers;

import com.jetug.power_armor_mod.client.model.*;
import com.jetug.power_armor_mod.client.render.layers.*;
import com.jetug.power_armor_mod.common.entity.entity_type.*;
import com.jetug.power_armor_mod.common.util.enums.*;
import com.mojang.blaze3d.matrix.*;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import jdk.jfr.internal.LogLevel;
import jdk.jfr.internal.LogTag;
import jdk.jfr.internal.Logger;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.util.*;
import software.bernie.geckolib3.core.processor.AnimationProcessor;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.*;
import software.bernie.geckolib3.renderers.geo.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static com.jetug.power_armor_mod.common.util.constants.Constants.*;
import static com.jetug.power_armor_mod.common.util.constants.Resources.*;
import static jdk.jfr.internal.LogTag.JFR_SYSTEM;

public class PowerArmorRenderer extends GeoEntityRenderer<PowerArmorEntity> {
    private final PowerArmorModel powerArmorModel;
    private final ArmorModel armorModel = new ArmorModel();
    private final ArmorPartLayer headLayer;
    private final ArmorPartLayer bodyLayer;
    private final ArmorPartLayer leftArmLayer;
    private final ArmorPartLayer rightArmLayer;
    private final ArmorPartLayer leftLegLayer ;
    private final ArmorPartLayer rightLegLayer;

    private final ArrayList<PowerArmorEntity> entities = new ArrayList<>();

    private PowerArmorModel getPowerArmorModel(){
        return (PowerArmorModel)getGeoModelProvider();
    }

    public PowerArmorRenderer(EntityRendererManager renderManager) {
        super(renderManager,  new PowerArmorModel());
        powerArmorModel = (PowerArmorModel)getGeoModelProvider();
        headLayer     = new ArmorPartLayer(this, BodyPart.HEAD      ,HELMET_MODEL_LOCATION,    HELMET_TEXTURE_LOCATION    );
        bodyLayer     = new ArmorPartLayer(this, BodyPart.BODY      ,BODY_MODEL_LOCATION,      BODY_TEXTURE_LOCATION      );
        leftArmLayer  = new ArmorPartLayer(this, BodyPart.LEFT_ARM  ,LEFT_ARM_MODEL_LOCATION,  LEFT_ARM_TEXTURE_LOCATION  );
        rightArmLayer = new ArmorPartLayer(this, BodyPart.RIGHT_ARM ,RIGHT_ARM_MODEL_LOCATION, RIGHT_ARM_TEXTURE_LOCATION );
        leftLegLayer  = new ArmorPartLayer(this, BodyPart.LEFT_LEG  ,LEFT_LEG_MODEL_LOCATION,  LEFT_LEG_TEXTURE_LOCATION  );
        rightLegLayer = new ArmorPartLayer(this, BodyPart.RIGHT_LEG ,RIGHT_LEG_MODEL_LOCATION, RIGHT_LEG_TEXTURE_LOCATION );

        addLayer(headLayer    );
        addLayer(bodyLayer    );
        addLayer(leftArmLayer );
        addLayer(rightArmLayer);
        addLayer(leftLegLayer );
        addLayer(rightLegLayer);
    }

    @Override
    public void render(PowerArmorEntity entity, float entityYaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer bufferIn, int packedLightIn) {
        updateArmor(entity);
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
    }

    private void updateArmor(PowerArmorEntity entity){
        handleArmorDamage(entity.head_   , headLayer,    entity.head_.getDurability());
        handleArmorDamage(entity.body    , bodyLayer,    entity.body.getDurability());
        handleArmorDamage(entity.leftArm , leftArmLayer, entity.leftArm.getDurability());
        handleArmorDamage(entity.rightArm, rightArmLayer,entity.rightArm.getDurability());
        handleArmorDamage(entity.leftLeg , leftLegLayer, entity.leftLeg.getDurability());
        handleArmorDamage(entity.rightLeg, rightLegLayer,entity.rightLeg.getDurability());
    }

    private void handleArmorDamage(PowerArmorPartEntity entity, ArmorPartLayer layer, double durability){
        if(entity.hasArmor()){
            attachBones(layer, entity);
        }
        else if(!entity.hasArmor()){
            detachBones(layer, entity);
        }
    }

    private void attachBones(ArmorPartLayer layer, PowerArmorPartEntity entity){
        handleLayer(layer, entity,true);
    }

    private void detachBones(ArmorPartLayer layer, PowerArmorPartEntity entity){
        handleLayer(layer, entity,false);
    }

    private void handleLayer(ArmorPartLayer layer, PowerArmorPartEntity entity, Boolean isAttaching){
        layer.boneAttachments.forEach(tuple ->{
            GeoBone bone1 = (GeoBone)powerArmorModel.getAnimationProcessor().getBone(tuple.getA());
            GeoBone bone2 = armorModel.getModel(layer.model).getBone(tuple.getB()).orElse(null);

            if(bone1 != null && bone2 != null) {
                if (isAttaching && !bone1.childBones.contains(bone2)) {
                    bone2.parent = bone1;
                    bone1.childBones.add(bone2);
                }
                else bone1.childBones.remove(bone2);
            }
        });
    }
}