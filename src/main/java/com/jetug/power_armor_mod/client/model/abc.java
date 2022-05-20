package com.jetug.power_armor_mod.client.model;

import com.jetug.power_armor_mod.client.render.layers.ArmorPartLayer;
import com.jetug.power_armor_mod.common.entity.entity_type.PowerArmorEntity;
import com.jetug.power_armor_mod.common.entity.entity_type.PowerArmorPartEntity;
import com.jetug.power_armor_mod.common.util.enums.BodyPart;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.Tuple;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoBone;

import java.util.ArrayList;
import java.util.Objects;

import static com.jetug.power_armor_mod.common.util.constants.Constants.BODY_BONE_NAME;
import static com.jetug.power_armor_mod.common.util.constants.Constants.HEAD_BONE_NAME;

public class abc {

    //Logger.log(JFR_SYSTEM, LogLevel.DEBUG, "" + entity.getDurability());


    //    private class ArmorLayers{
//        public ArmorPartLayer headLayer;
//        public ArmorPartLayer bodyLayer;
//        public ArmorPartLayer leftArmLayer;
//        public ArmorPartLayer rightArmLayer;
//        public ArmorPartLayer leftLegLayer ;
//        public ArmorPartLayer rightLegLayer;
//    }

//    @Override
//    public void render(PowerArmorEntity entity, float entityYaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer bufferIn, int packedLightIn) {
//
//        //initArmorParts(entity);
//        updateArmor(entity);
//        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
////        if(!armorAttached){
////            initArmorParts(entity);
////            //renderArmor(entity);
////            //addLayer(headLayer);
////            armorAttached = true;
////        }
//    }

//    @Override
//    public void renderEarly(PowerArmorEntity entity, MatrixStack stackIn, float ticks, IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder,
//                            int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks) {
//        updateArmor(entity);
//        super.renderEarly(entity, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
//    }


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
//    public ArmorPartLayer(IGeoRenderer entityRendererIn, ResourceLocation model, ResourceLocation texture) {
//        super(entityRendererIn);
//        this.model = model;
//        this.texture = texture;
//    }


//    private ArrayList<Tuple<String, String>> getAttachments(BodyPart bodyPart){
//        ArrayList<Tuple<String, String>> boneList = new ArrayList<>();
//        switch (bodyPart){
//            case HEAD:
//                boneList.add(new Tuple<>(HEAD_BONE_NAME, "helmet"));
//                break;
//            case BODY:
//                boneList.add(new Tuple<>(BODY_BONE_NAME, "body_top_armor"));
//                break;
//            case LEFT_ARM:
//                boneList.add(new Tuple<>("left_upper_arm", "left_shoulder_armor"    ));
//                boneList.add(new Tuple<>("left_upper_arm", "left_upper_arm_armor"   ));
//                boneList.add(new Tuple<>("left_lower_arm", "left_forearm_armor"     ));
//                break;
//            case RIGHT_ARM:
//                boneList.add(new Tuple<>("right_upper_arm", "right_shoulder_armor"  ));
//                boneList.add(new Tuple<>("right_upper_arm", "right_upper_arm_armor" ));
//                boneList.add(new Tuple<>("right_lower_arm", "right_forearm_armor"   ));
//                break;
//            case LEFT_LEG:
//                boneList.add(new Tuple<>("left_upper_leg", "left_upper_leg_armor"   ));
//                boneList.add(new Tuple<>("left_lower_leg", "left_knee"              ));
//                boneList.add(new Tuple<>("left_lower_leg", "left_lower_leg_armor"   ));
//                break;
//            case RIGHT_LEG:
//                boneList.add(new Tuple<>("right_upper_leg", "right_upper_leg_armor" ));
//                boneList.add(new Tuple<>("right_lower_leg", "right_knee"            ));
//                boneList.add(new Tuple<>("right_lower_leg", "right_lower_leg_armor" ));
//                break;
//        }
//        return boneList;
//    }


//    private void addArmorPart(ArmorPartLayer layer, PowerArmorPartEntity entity){
//        attachBones(layer, entity);
//        //addLayer(layer);
//    }
//
//    private void removeArmorPart(ArmorPartLayer layer, PowerArmorPartEntity entity){
//        detachBones(layer, entity);
//        //removeLayer(layer);
//    }

//    private void removeLayer(ArmorPartLayer layer){
//        layerRenderers.remove(layer);
//    }
//
//    private void handleLayer(ArmorPartLayer layer, PowerArmorPartEntity entity, Boolean isAttaching){
//        layer.boneAttachments.forEach(tuple ->{
//            //GeoBone bone1 = getBone(tuple.getA());
//            GeoBone bone1 = (GeoBone)getPowerArmorModel().getAnimationProcessor().getBone(tuple.getA());
//            GeoBone bone2 = armorModel.getModel(layer.model).getBone(tuple.getB()).orElse(null);
//
//            if(bone1 != null && bone2 != null) {
//                if (isAttaching && !bone1.childBones.contains(bone2)) {
//                    bone2.parent = bone1;
//                    bone1.childBones.add(bone2);
//                }
//                else bone1.childBones.remove(bone2);
//            }
//        });
//    }
//
//
//    private GeoBone getBone(String name){
//        ArrayList<IBone> list = (ArrayList<IBone>) getPowerArmorModel().getAnimationProcessor().getModelRendererList();
//        for (IBone item: list){
//            String buff = item.getName();
//            if (Objects.equals(buff, name))
//                return (GeoBone)item;
//        }
//        return null;
//    }
}
