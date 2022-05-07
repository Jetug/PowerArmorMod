package com.jetug.begining.client.render;

import com.jetug.begining.client.model.ArmorModel;
import com.jetug.begining.common.entity.entity_type.PowerArmorEntity;
import com.jetug.begining.client.model.PowerArmorModel;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.Optional;

import static com.jetug.begining.common.util.constants.Attributes.*;
import static com.jetug.begining.common.util.constants.Resources.*;

public class PowerArmorRenderer extends GeoEntityRenderer<PowerArmorEntity> {
    private static final PowerArmorModel powerArmorModel = new PowerArmorModel();
    private static final ArmorModel armorModel = new ArmorModel();

    public PowerArmorRenderer(EntityRendererManager renderManager) {
        super(renderManager, powerArmorModel);
    }

//    @Override
//    public RenderType getRenderType(PowerArmorEntity entity, float partialTicks, MatrixStack stack, @Nullable IRenderTypeBuffer renderTypeBuffer, @Nullable IVertexBuilder vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
//        ClientPlayerEntity clientPlayer = Minecraft.getInstance().player;
//        PointOfView pov = Minecraft.getInstance().options. getCameraType();
//
//        if(entity.isVehicle() && entity.hasPassenger(clientPlayer) && pov == PointOfView.FIRST_PERSON)
//            return RenderType.entityTranslucent(INVISIBLE_TEXTURE);
//        else
//            return RenderType.entityTranslucent(POWER_ARMOR_TEXTURE_LOCATION);
//    }

    @Override
    public void renderEarly(PowerArmorEntity entity, MatrixStack stackIn, float ticks, IRenderTypeBuffer renderTypeBuffer,
                            IVertexBuilder vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks)
    {
        super.renderEarly(entity, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
        //renderArmor(entity);
    }

    public void renderArmor(PowerArmorEntity entity){
        AttributeModifierManager attr = entity.getAttributes();
        if(attr.hasAttribute(BODY_ARMOR_HEALTH)){
            double bodyArmorHealth = entity.getAttributes().getValue(BODY_ARMOR_HEALTH);
            if(bodyArmorHealth > 0.0D){
                ArrayList<Tuple<String, String>> boneList = getBoneAttachments(entity);
                attachBones(boneList);
            }
        }
    }

    private ArrayList<Tuple<String, String>> getBoneAttachments(PowerArmorEntity entity){

        ArrayList<Tuple<String, String>> boneList = new ArrayList<>();

        if(entity.getAttribute(HEAD_ARMOR_HEALTH).getValue() > 0.0D)
            boneList.add(new Tuple<>("head", "helmet"));

        if(entity.getAttribute(BODY_ARMOR_HEALTH).getValue() > 0.0D)
            boneList.add(new Tuple<>("body", "body_top_armor"));

        if(entity.getAttribute(LEFT_ARM_ARMOR_HEALTH).getValue() > 0.0D) {
            boneList.add(new Tuple<>("left_upper_arm", "left_shoulder_armor"));
            boneList.add(new Tuple<>("left_upper_arm", "left_upper_arm_armor"));
            boneList.add(new Tuple<>("left_lower_arm", "left_forearm_armor"));
        }
        if(entity.getAttribute(RIGHT_ARM_ARMOR_HEALTH).getValue() > 0.0D) {
            boneList.add(new Tuple<>("right_upper_arm", "right_shoulder_armor"));
            boneList.add(new Tuple<>("right_upper_arm", "right_upper_arm_armor"));
            boneList.add(new Tuple<>("right_lower_arm", "right_forearm_armor"));
        }
        if(entity.getAttribute(LEFT_LEG_ARMOR_HEALTH).getValue() > 0.0D) {
            boneList.add(new Tuple<>("left_upper_leg", "left_upper_leg_armor"));
            boneList.add(new Tuple<>("left_lower_leg", "left_knee"));
            boneList.add(new Tuple<>("left_lower_leg", "left_lower_leg_armor"));
        }
        if(entity.getAttribute(RIGHT_LEG_ARMOR_HEALTH).getValue() > 0.0D) {
            boneList.add(new Tuple<>("right_upper_leg", "right_upper_leg_armor"));
            boneList.add(new Tuple<>("right_lower_leg", "right_knee"));
            boneList.add(new Tuple<>("right_lower_leg", "right_lower_leg_armor"));
        }
        return boneList;
    }

    private void attachBones(ArrayList<Tuple<String, String>> boneList){
        boneList.forEach(tuple ->{
            GeoBone body = (GeoBone)powerArmorModel.getAnimationProcessor().getBone(tuple.getA());
            armorModel.getModel(ARMOR_MODEL_LOCATION).getBone(tuple.getB()).ifPresent(bodyArmor -> body.childBones.add(bodyArmor));
        });
    }
}
