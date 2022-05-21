package com.jetug.power_armor_mod.client.render.layers;

import com.jetug.power_armor_mod.common.entity.entity_type.PowerArmorEntity;
import com.jetug.power_armor_mod.common.util.enums.BodyPart;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

import java.util.ArrayList;

import static com.jetug.power_armor_mod.common.util.constants.Constants.BODY_BONE_NAME;
import static com.jetug.power_armor_mod.common.util.constants.Constants.HEAD_BONE_NAME;
import static com.jetug.power_armor_mod.common.util.constants.Resources.POWER_ARMOR_MODEL_LOCATION;

public class ArmorPartLayer extends GeoLayerRenderer {
    public BodyPart bodyPart;
    public ResourceLocation model = POWER_ARMOR_MODEL_LOCATION;
    public ResourceLocation texture;
    public ArrayList<Tuple<String, String>> boneAttachments = null;

    public ArmorPartLayer(IGeoRenderer<PowerArmorEntity> entityRendererIn, BodyPart bodyPart, ResourceLocation model, ResourceLocation texture)
    {
        super(entityRendererIn);
        this.model = model;
        this.texture = texture;
        this.bodyPart = bodyPart;
        this.boneAttachments = getAttachments(bodyPart);
    }


    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, Entity entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        PowerArmorEntity entity = (PowerArmorEntity)entityLivingBaseIn;
        if(entity.getArmorPart(bodyPart).hasArmor()) {
            RenderType cameo = RenderType.armorCutoutNoCull(texture);
            matrixStackIn.pushPose();
            //Move or scale the model as you see fit
            matrixStackIn.scale(1.0f, 1.0f, 1.0f);
            matrixStackIn.translate(0.0d, 0.0d, 0.0d);
            this.getRenderer().render(this.getEntityModel().getModel(POWER_ARMOR_MODEL_LOCATION), entityLivingBaseIn, partialTicks, cameo, matrixStackIn, bufferIn,
                    bufferIn.getBuffer(cameo), packedLightIn, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);
            matrixStackIn.popPose();
        }
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
                boneList.add(new Tuple<>("left_upper_arm", "left_shoulder_armor"   ));
                boneList.add(new Tuple<>("left_upper_arm", "left_upper_arm_armor"  ));
                boneList.add(new Tuple<>("left_lower_arm", "left_forearm_armor"    ));
                break;
            case RIGHT_ARM:
                boneList.add(new Tuple<>("right_upper_arm", "right_shoulder_armor" ));
                boneList.add(new Tuple<>("right_upper_arm", "right_upper_arm_armor"));
                boneList.add(new Tuple<>("right_lower_arm", "right_forearm_armor"  ));
                break;
            case LEFT_LEG:
                boneList.add(new Tuple<>("left_upper_leg", "left_upper_leg_armor"  ));
                boneList.add(new Tuple<>("left_lower_leg", "left_knee"             ));
                boneList.add(new Tuple<>("left_lower_leg", "left_lower_leg_armor"  ));
                break;
            case RIGHT_LEG:
                boneList.add(new Tuple<>("right_upper_leg", "right_upper_leg_armor"));
                boneList.add(new Tuple<>("right_lower_leg", "right_knee"           ));
                boneList.add(new Tuple<>("right_lower_leg", "right_lower_leg_armor"));
                break;
        }
        return boneList;
    }
}