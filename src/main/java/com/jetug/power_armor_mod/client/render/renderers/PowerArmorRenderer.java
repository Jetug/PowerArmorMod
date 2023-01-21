package com.jetug.power_armor_mod.client.render.renderers;

import com.jetug.power_armor_mod.client.model.ArmorModel;
import com.jetug.power_armor_mod.client.model.PowerArmorModel;
import com.jetug.power_armor_mod.client.render.ResourceHelper;
import com.jetug.power_armor_mod.client.render.layers.ArmorPartLayer;
import com.jetug.power_armor_mod.client.render.layers.PlayerHeadLayer;
import com.jetug.power_armor_mod.common.minecraft.entity.ArmorSlot;
import com.jetug.power_armor_mod.common.minecraft.entity.PowerArmorEntity;
import com.jetug.power_armor_mod.common.util.enums.BodyPart;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.apache.logging.log4j.Level;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;

import java.util.ArrayList;

import static com.jetug.power_armor_mod.common.util.constants.Global.LOGGER;

public class PowerArmorRenderer extends GeoEntityRenderer<PowerArmorEntity> {
    private final PowerArmorModel<PowerArmorEntity> powerArmorModel;
    private final ArmorModel<PowerArmorEntity> armorModel = new ArmorModel<>();

//    private final ArmorPartLayer headLayer      = new ArmorPartLayer(this, BodyPart.HEAD     );
//    private final ArmorPartLayer bodyLayer      = new ArmorPartLayer(this, BodyPart.BODY     );
//    private final ArmorPartLayer leftArmLayer   = new ArmorPartLayer(this, BodyPart.LEFT_ARM );
//    private final ArmorPartLayer rightArmLayer  = new ArmorPartLayer(this, BodyPart.RIGHT_ARM);
//    private final ArmorPartLayer leftLegLayer   = new ArmorPartLayer(this, BodyPart.LEFT_LEG );
//    private final ArmorPartLayer rightLegLayer  = new ArmorPartLayer(this, BodyPart.RIGHT_LEG);
//    private final PlayerHeadLayer playerHeadLayer = new PlayerHeadLayer(this);

//    private final GeoLayerRenderer<PowerArmorEntity>[] armorLayers
//            = new GeoLayerRenderer[]{headLayer, bodyLayer, leftArmLayer, rightArmLayer, leftLegLayer, rightLegLayer, playerHeadLayer};

    public PowerArmorRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager,  new PowerArmorModel<>());
        powerArmorModel = (PowerArmorModel<PowerArmorEntity>)getGeoModelProvider();

//        for (GeoLayerRenderer<PowerArmorEntity> armorLayer : armorLayers) {
//            addLayer(armorLayer);
//        }

        initLayers();
    }

    private void initLayers(){
        for (int i = 0; i < 6; i++){
            addLayer(new ArmorPartLayer(this, BodyPart.getById(i)));
        }
        addLayer(new PlayerHeadLayer(this));
    }

    @Override
    public void render(PowerArmorEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        try{
            updateArmor(entity);
            super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        } catch (Exception e) {
            LOGGER.log(Level.ERROR, e);
        }
    }

    private void updateArmor(PowerArmorEntity entity){
        for (ArmorSlot armorPart : entity.armorParts) {
            if (armorPart.hasArmor())
                attachBones(armorPart);
            else if (!armorPart.hasArmor())
                detachBones(armorPart);
        }
    }

    private void attachBones(ArmorSlot slot){
        handleModel(slot, true);
    }

    private void detachBones(ArmorSlot slot){
        handleModel(slot, false);
    }

    private void handleModel(ArmorSlot slot, Boolean isAttaching){
        slot.getAttachments().forEach(tuple ->{
            ResourceLocation model = ResourceHelper.getModel(slot.getArmorPart(), slot.getType());
            GeoBone frameBone = (GeoBone)powerArmorModel.getAnimationProcessor().getBone(tuple.getA());
            GeoBone armorBone = armorModel.getModel(model).getBone(tuple.getB()).orElse(null);

            if(frameBone != null && armorBone != null) {
                if (isAttaching) {
                    if(!frameBone.childBones.contains(armorBone)) {
                        armorBone.parent = frameBone;
                        frameBone.childBones.add(armorBone);
                    }
                }
                else frameBone.childBones.remove(armorBone);
            }
        });
    }
}