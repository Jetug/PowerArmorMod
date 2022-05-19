package com.jetug.power_armor_mod.client.render.renderers;

import com.jetug.power_armor_mod.client.model.*;
import com.jetug.power_armor_mod.client.render.layers.*;
import com.jetug.power_armor_mod.common.entity.entity_type.*;
import com.jetug.power_armor_mod.common.util.enums.*;
import com.mojang.blaze3d.matrix.*;
import com.mojang.blaze3d.vertex.IVertexBuilder;
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

public class PowerArmorRenderer extends GeoEntityRenderer<PowerArmorEntity> {
//    private class ArmorLayers{
//        public ArmorPartLayer headLayer;
//        public ArmorPartLayer bodyLayer;
//        public ArmorPartLayer leftArmLayer;
//        public ArmorPartLayer rightArmLayer;
//        public ArmorPartLayer leftLegLayer ;
//        public ArmorPartLayer rightLegLayer;
//    }

//    private final PowerArmorModel powerArmorModel;
    private final ArmorModel armorModel = new ArmorModel();
//    private final HelmetModel helmetModel = new HelmetModel();
    private final ArmorPartLayer headLayer;
    private final ArmorPartLayer bodyLayer;
    private final ArmorPartLayer leftArmLayer;
    private final ArmorPartLayer rightArmLayer;
    private final ArmorPartLayer leftLegLayer ;
    private final ArmorPartLayer rightLegLayer;
//    private boolean armorAttached = false;

   // private final HashMap<PowerArmorEntity, ArmorLayers> entityArmorLayers = new HashMap<>();

    //private final HashMap<PowerArmorEntity, AnimationProcessor> entityModels = new HashMap<>();

    private final ArrayList<PowerArmorEntity> entities = new ArrayList<>();

    private PowerArmorModel getPowerArmorModel(){
        return (PowerArmorModel)getGeoModelProvider();
    }

    public PowerArmorRenderer(EntityRendererManager renderManager) {
        super(renderManager,  new PowerArmorModel());
//        powerArmorModel = (PowerArmorModel)getGeoModelProvider();
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

        //initArmorParts(entity);
        updateArmor(entity);
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
//        if(!armorAttached){
//            initArmorParts(entity);
//            //renderArmor(entity);
//            //addLayer(headLayer);
//            armorAttached = true;
//        }
    }

//    @Override
//    public void renderEarly(PowerArmorEntity entity, MatrixStack stackIn, float ticks, IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder,
//                            int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks) {
//        updateArmor(entity);
//        super.renderEarly(entity, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
//    }

    private void updateArmor(PowerArmorEntity entity){
        //if(!entityModels.containsKey(entity)){
            //entityModels.put(entity, ((PowerArmorModel)getGeoModelProvider()).getAnimationProcessor());
        //}

//        double dur = entity.head_.getDurability();

        handleArmorDamage(entity.head_   , headLayer,    entity.head_.getDurability());
        handleArmorDamage(entity.body    , bodyLayer,    entity.body.getDurability());
        handleArmorDamage(entity.leftArm , leftArmLayer, entity.leftArm.getDurability());
        handleArmorDamage(entity.rightArm, rightArmLayer,entity.rightArm.getDurability());
        handleArmorDamage(entity.leftLeg , leftLegLayer, entity.leftLeg.getDurability());
        handleArmorDamage(entity.rightLeg, rightLegLayer,entity.rightLeg.getDurability());
    }

//    public void renderArmor(PowerArmorEntity entity){
//        //attachBones(getBoneAttachments(entity));
//    }
//
//    private void initArmorParts(PowerArmorEntity entity){
//        if(!entities.contains(entity)){
//            entities.add(entity);
//
//            entity.head_    .subscribeEvents(durability -> handleArmorDamage(entity.head_   , headLayer    , durability));
//            entity.body     .subscribeEvents(durability -> handleArmorDamage(entity.body    , bodyLayer    , durability));
//            entity.leftArm  .subscribeEvents(durability -> handleArmorDamage(entity.leftArm , leftArmLayer , durability));
//            entity.rightArm .subscribeEvents(durability -> handleArmorDamage(entity.rightArm, rightArmLayer, durability));
//            entity.leftLeg  .subscribeEvents(durability -> handleArmorDamage(entity.leftLeg , leftLegLayer , durability));
//            entity.rightLeg .subscribeEvents(durability -> handleArmorDamage(entity.rightLeg, rightLegLayer, durability));
//        }
//        updateArmor(entity);
//    }

    private ArrayList<Tuple<String, String>> getAttachments(BodyPart bodyPart){
        ArrayList<Tuple<String, String>> boneList = new ArrayList<>();
        switch (bodyPart){
            case HEAD:
                boneList.add(new Tuple<>(HEAD_BONE_NAME, "helmet"));
                break;
            case BODY:
                boneList.add(new Tuple<>(BODY_BONE_NAME, "body_top_armor"));
                break;
            case LEFT_ARM:
                boneList.add(new Tuple<>("left_upper_arm", "left_shoulder_armor"    ));
                boneList.add(new Tuple<>("left_upper_arm", "left_upper_arm_armor"   ));
                boneList.add(new Tuple<>("left_lower_arm", "left_forearm_armor"     ));
                break;
            case RIGHT_ARM:
                boneList.add(new Tuple<>("right_upper_arm", "right_shoulder_armor"  ));
                boneList.add(new Tuple<>("right_upper_arm", "right_upper_arm_armor" ));
                boneList.add(new Tuple<>("right_lower_arm", "right_forearm_armor"   ));
                break;
            case LEFT_LEG:
                boneList.add(new Tuple<>("left_upper_leg", "left_upper_leg_armor"   ));
                boneList.add(new Tuple<>("left_lower_leg", "left_knee"              ));
                boneList.add(new Tuple<>("left_lower_leg", "left_lower_leg_armor"   ));
                break;
            case RIGHT_LEG:
                boneList.add(new Tuple<>("right_upper_leg", "right_upper_leg_armor" ));
                boneList.add(new Tuple<>("right_lower_leg", "right_knee"            ));
                boneList.add(new Tuple<>("right_lower_leg", "right_lower_leg_armor" ));
                break;
        }
        return boneList;
    }

    private void handleArmorDamage(PowerArmorPartEntity entity, ArmorPartLayer layer, double durability){
        if(entity.hasArmor() /*&& !entity.isArmorAttached*/){
            //addArmorPart(layer, entity);
            attachBones(layer, entity);
            //entity.isArmorAttached = true;
        }
        if(!entity.hasArmor() /*&& entity.isArmorAttached*/){
            //removeArmorPart(layer, entity);
            detachBones(layer, entity);
            //entity.isArmorAttached = false;
        }
    }

//    private void addArmorPart(ArmorPartLayer layer, PowerArmorPartEntity entity){
//        attachBones(layer, entity);
//        //addLayer(layer);
//    }
//
//    private void removeArmorPart(ArmorPartLayer layer, PowerArmorPartEntity entity){
//        detachBones(layer, entity);
//        //removeLayer(layer);
//    }


    private void attachBones(ArmorPartLayer layer, PowerArmorPartEntity entity){
        handleLayer(layer, entity,true);
    }

    private void detachBones(ArmorPartLayer layer, PowerArmorPartEntity entity){
        handleLayer(layer, entity,false);
    }

    private void handleLayer(ArmorPartLayer layer, PowerArmorPartEntity entity, Boolean isAttaching){
        layer.boneAttachments.forEach(tuple ->{
            GeoBone bone1 = getBone(tuple.getA());
            //GeoBone bone1 = (GeoBone)getPowerArmorModel().getAnimationProcessor().getBone(tuple.getA());
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


    private GeoBone getBone(String name){
        ArrayList<IBone> list = (ArrayList<IBone>) getPowerArmorModel().getAnimationProcessor().getModelRendererList();
        for (IBone item: list){
            String buff = item.getName();
            if (Objects.equals(buff, name))
                return (GeoBone)item;
        }
        return null;
    }

    private void removeLayer(ArmorPartLayer layer){
        layerRenderers.remove(layer);
    }
}