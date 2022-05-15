package com.jetug.power_armor_mod.client.render.renderers;

import com.ibm.icu.impl.Pair;
import com.jetug.power_armor_mod.client.model.*;
import com.jetug.power_armor_mod.client.render.layers.*;
import com.jetug.power_armor_mod.common.entity.entity_type.*;
import com.jetug.power_armor_mod.common.util.enums.*;
import com.mojang.blaze3d.matrix.*;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.util.*;
import software.bernie.geckolib3.geo.render.built.*;
import software.bernie.geckolib3.renderers.geo.*;

import javax.annotation.Nullable;
import java.util.ArrayList;

import static com.jetug.power_armor_mod.common.util.constants.Constants.*;
import static com.jetug.power_armor_mod.common.util.constants.Resources.*;

public class PowerArmorRenderer extends GeoEntityRenderer<PowerArmorEntity> {
    private class ArmorLayers{
        public ArmorPartLayer headLayer;
        public ArmorPartLayer bodyLayer;
        public ArmorPartLayer leftArmLayer;
        public ArmorPartLayer rightArmLayer;
        public ArmorPartLayer leftLegLayer ;
        public ArmorPartLayer rightLegLayer;
    }

    public final PowerArmorModel powerArmorModel;
//    private final ArmorModel armorModel = new ArmorModel();
//    private final HelmetModel helmetModel = new HelmetModel();
    private final ArmorPartLayer headLayer;
    private final ArmorPartLayer bodyLayer;
    private final ArmorPartLayer leftArmLayer;
    private final ArmorPartLayer rightArmLayer;
    private final ArmorPartLayer leftLegLayer ;
    private final ArmorPartLayer rightLegLayer;
    private boolean armorAttached = false;

    public PowerArmorRenderer(EntityRendererManager renderManager) {
        super(renderManager,  new PowerArmorModel());
        powerArmorModel = (PowerArmorModel)getGeoModelProvider();
        headLayer     = new ArmorPartLayer (this, HELMET_MODEL_LOCATION, HELMET_TEXTURE_LOCATION, getAttachments(BodyPart.HEAD));
        bodyLayer     = new ArmorPartLayer (this, BODY_MODEL_LOCATION, BODY_TEXTURE_LOCATION, getAttachments(BodyPart.BODY));
        leftArmLayer  = new ArmorPartLayer (this, LEFT_ARM_MODEL_LOCATION, LEFT_ARM_TEXTURE_LOCATION, getAttachments(BodyPart.LEFT_ARM));
        rightArmLayer = new ArmorPartLayer (this, RIGHT_ARM_MODEL_LOCATION, RIGHT_ARM_TEXTURE_LOCATION, getAttachments(BodyPart.RIGHT_ARM));
        leftLegLayer  = new ArmorPartLayer (this, LEFT_LEG_MODEL_LOCATION, LEFT_LEG_TEXTURE_LOCATION, getAttachments(BodyPart.LEFT_LEG));
        rightLegLayer = new ArmorPartLayer (this, RIGHT_LEG_MODEL_LOCATION, RIGHT_LEG_TEXTURE_LOCATION, getAttachments(BodyPart.RIGHT_LEG));
    }

    private void createLayers

    @Override
    public void renderEarly(PowerArmorEntity entity, MatrixStack stackIn, float ticks, IRenderTypeBuffer renderTypeBuffer,
                            IVertexBuilder vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks)
    {
        super.renderEarly(entity, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
    }

    @Override
    public void render(PowerArmorEntity entity, float entityYaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer bufferIn, int packedLightIn) {
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
        updateArmor(entity);
//        if(!armorAttached){
//            initArmorParts(entity);
//            //renderArmor(entity);
//            //addLayer(headLayer);
//            armorAttached = true;
//        }
    }

    public void renderArmor(PowerArmorEntity entity){
        //attachBones(getBoneAttachments(entity));
    }

    private void initArmorParts(PowerArmorEntity entity){
        entity.head.subscribeEvents(durability ->     handleArmorDamage(headLayer, durability));
        entity.body.subscribeEvents(durability ->     handleArmorDamage(bodyLayer, durability));
        entity.leftArm.subscribeEvents(durability ->  handleArmorDamage(leftArmLayer, durability));
        entity.rightArm.subscribeEvents(durability -> handleArmorDamage(rightArmLayer, durability));
        entity.leftLeg.subscribeEvents(durability ->  handleArmorDamage(leftLegLayer, durability));
        entity.rightLeg.subscribeEvents(durability -> handleArmorDamage(rightLegLayer, durability));

        updateArmor(entity);
    }

    private void initArmor(PowerArmorEntity entity){
        if(entity.head.getDurability() > 0.0D) {
            addArmorPart(headLayer);
        }
        if(entity.body.getDurability() > 0.0D)
            addArmorPart(bodyLayer);

        if(entity.leftArm.getDurability() > 0.0D) {
            addArmorPart(leftArmLayer);
        }
        if(entity.rightArm.getDurability() > 0.0D) {
            addArmorPart(rightArmLayer);
        }
        if(entity.leftLeg.getDurability() > 0.0D) {
            addArmorPart(leftLegLayer);
        }
        if(entity.rightLeg.getDurability() > 0.0D) {
            addArmorPart(rightLegLayer);
        }
    }

    private void updateArmor(PowerArmorEntity entity){
        handleArmorDamage(headLayer, entity.head.getDurability());
        handleArmorDamage(bodyLayer, entity.body.getDurability());
        handleArmorDamage(leftArmLayer, entity.leftArm.getDurability());
        handleArmorDamage(rightArmLayer, entity.rightArm.getDurability());
        handleArmorDamage(leftLegLayer, entity.leftLeg.getDurability());
        handleArmorDamage(rightLegLayer, entity.rightLeg.getDurability());
    }

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
                boneList.add(new Tuple<>("left_upper_leg", "left_upper_leg_armor"));
                boneList.add(new Tuple<>("left_lower_leg", "left_knee"));
                boneList.add(new Tuple<>("left_lower_leg", "left_lower_leg_armor"));
                break;
            case RIGHT_LEG:
                boneList.add(new Tuple<>("right_upper_leg", "right_upper_leg_armor"));
                boneList.add(new Tuple<>("right_lower_leg", "right_knee"));
                boneList.add(new Tuple<>("right_lower_leg", "right_lower_leg_armor"));
                break;
        }
        return boneList;
    }

    private void handleArmorDamage(ArmorPartLayer layer, double durability){
        if(durability <= 0 && layerRenderers.contains(layer)){
            removeArmorPart(layer);
        }
        else if(durability > 0 && !layerRenderers.contains(layer)){
            addArmorPart(layer);
        }
    }

    private ArrayList<Tuple<String, String>> getBoneAttachments(PowerArmorEntity entity){
        ArrayList<Tuple<String, String>> boneList = new ArrayList<>();

        if(entity.head.getDurability() > 0.0D) {
            boneList.add(new Tuple<>(HEAD_BONE_NAME, "helmet"));
        }
        if(entity.body.getDurability() > 0.0D)
            boneList.add(new Tuple<>(BODY_BONE_NAME, "body_top_armor"));

        if(entity.leftArm.getDurability() > 0.0D) {
            boneList.add(new Tuple<>("left_upper_arm", "left_shoulder_armor"));
            boneList.add(new Tuple<>("left_upper_arm", "left_upper_arm_armor"));
            boneList.add(new Tuple<>("left_lower_arm", "left_forearm_armor"));
        }
        if(entity.rightArm.getDurability() > 0.0D) {
            boneList.add(new Tuple<>("right_upper_arm", "right_shoulder_armor"));
            boneList.add(new Tuple<>("right_upper_arm", "right_upper_arm_armor"));
            boneList.add(new Tuple<>("right_lower_arm", "right_forearm_armor"));
        }
        if(entity.leftLeg.getDurability() > 0.0D) {
            boneList.add(new Tuple<>("left_upper_leg", "left_upper_leg_armor"));
            boneList.add(new Tuple<>("left_lower_leg", "left_knee"));
            boneList.add(new Tuple<>("left_lower_leg", "left_lower_leg_armor"));
        }
        if(entity.rightLeg.getDurability() > 0.0D) {
            boneList.add(new Tuple<>("right_upper_leg", "right_upper_leg_armor"));
            boneList.add(new Tuple<>("right_lower_leg", "right_knee"));
            boneList.add(new Tuple<>("right_lower_leg", "right_lower_leg_armor"));
        }
        return boneList;
    }

//    private void attachBone(String boneName, ResourceLocation model){
//        GeoBone bone1 = powerArmorModel.getModel(POWER_ARMOR_MODEL_LOCATION).getBone(boneName).orElse(null);
//        GeoBone bone2 = helmetModel.getModel(HELMET_MODEL_LOCATION).topLevelBones.get(0);
//        bone1.childBones.add(bone2);
//    }

    private void removeArmorPart(ArmorPartLayer layer){
        detachBones(layer);
        removeLayer(layer);
    }

    private void addArmorPart(ArmorPartLayer layer){
        attachBones(layer);
        addLayer(layer);
    }

    private void detachBones(ArmorPartLayer layer){
        handleLayer(layer, false);
    }

    private void attachBones(ArmorPartLayer layer){
        handleLayer(layer, true);
    }

    private void handleLayer(ArmorPartLayer layer, Boolean isAttaching){
        layer.boneAttachments.forEach(tuple ->{
            //Tuple<GeoBone, GeoBone> bones = getBones(layer, tuple);
            GeoBone bone1 = powerArmorModel.getModel(POWER_ARMOR_MODEL_LOCATION).getBone(tuple.getA()).orElse(null);
            GeoBone bone2 = powerArmorModel.getModel(layer.model).getBone(tuple.getB()).orElse(null);
            if(isAttaching) bone1.childBones.add(bone2);
            else bone1.childBones.remove(bone2);
        });
    }

    private Tuple<GeoBone, GeoBone> getBones(ArmorPartLayer layer, Tuple<String, String> tuple){
        GeoBone bone1 = powerArmorModel.getModel(POWER_ARMOR_MODEL_LOCATION).getBone(tuple.getA()).orElse(null);
        GeoBone bone2 = powerArmorModel.getModel(layer.model).getBone(tuple.getB()).orElse(null);
        return new Tuple<>(bone1, bone2);
    }

    private void removeLayer(ArmorPartLayer layer){
        layerRenderers.remove(layer);
    }
}