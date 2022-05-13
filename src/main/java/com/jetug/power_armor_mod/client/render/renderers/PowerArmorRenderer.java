package com.jetug.power_armor_mod.client.render.renderers;

import com.jetug.power_armor_mod.client.model.ArmorModel;
import com.jetug.power_armor_mod.client.model.HelmetModel;
import com.jetug.power_armor_mod.client.render.layers.HeadLayer;
import com.jetug.power_armor_mod.common.entity.entity_type.PowerArmorEntity;
import com.jetug.power_armor_mod.client.model.PowerArmorModel;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.Tuple;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;

import java.util.ArrayList;

import static com.jetug.power_armor_mod.common.util.constants.Constants.*;
import static com.jetug.power_armor_mod.common.util.constants.Resources.*;

public class PowerArmorRenderer extends GeoEntityRenderer<PowerArmorEntity> {
    public final PowerArmorModel POWER_ARMOR_MODEL;
    private final ArmorModel armorModel = new ArmorModel();
    private final HelmetModel helmetModel = new HelmetModel();

    private boolean armorAttached = false;

    public PowerArmorRenderer(EntityRendererManager renderManager) {
        super(renderManager,  new PowerArmorModel());
        POWER_ARMOR_MODEL = (PowerArmorModel)getGeoModelProvider();
        HeadLayer headLayer = new HeadLayer(this);
        addLayer(headLayer);
    }

    @Override
    public void renderEarly(PowerArmorEntity entity, MatrixStack stackIn, float ticks, IRenderTypeBuffer renderTypeBuffer,
                            IVertexBuilder vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks)
    {
        super.renderEarly(entity, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
    }

    @Override
    public void render(PowerArmorEntity entity, float entityYaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer bufferIn, int packedLightIn) {
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
//        if(!armorAttached){
//            renderArmor(entity);
//            armorAttached = true;
//        }
    }

    public void renderArmor(PowerArmorEntity entity){
        ArrayList<Tuple<String, String>> boneList = getBoneAttachments(entity);

        attachBones(boneList);
    }

    private ArrayList<Tuple<String, String>> getBoneAttachments(PowerArmorEntity entity){
        ArrayList<Tuple<String, String>> boneList = new ArrayList<>();

        if(entity.head.getDurability() > 0.0D)
            boneList.add(new Tuple<>(HEAD_BONE_NAME, "helmet"));

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

    private void attachBones(ArrayList<Tuple<String, String>> boneList){
        boneList.forEach(tuple ->{
            GeoBone bone = POWER_ARMOR_MODEL.getModel(POWER_ARMOR_MODEL_LOCATION).getBone(tuple.getA()).orElse(null);
            armorModel.getModel(ARMOR_MODEL_LOCATION).getBone(tuple.getB()).ifPresent(bodyArmor -> bone.childBones.add(bodyArmor));
        });
    }

    private void removeLayer(GeoLayerRenderer layer){
        layerRenderers.remove(layer);
    }
}
