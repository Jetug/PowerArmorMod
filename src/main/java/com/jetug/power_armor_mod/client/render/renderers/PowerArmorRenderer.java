package com.jetug.power_armor_mod.client.render.renderers;

import com.jetug.power_armor_mod.client.model.*;
import com.jetug.power_armor_mod.client.render.ResourceHelper;
import com.jetug.power_armor_mod.client.render.layers.*;
import com.jetug.power_armor_mod.common.minecraft.entity.ArmorSlot;
import com.jetug.power_armor_mod.common.minecraft.entity.PowerArmorEntity;
import com.jetug.power_armor_mod.common.util.enums.*;
import com.mojang.blaze3d.matrix.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Level;
import software.bernie.geckolib3.geo.render.built.*;
import software.bernie.geckolib3.renderers.geo.*;

import static com.jetug.power_armor_mod.PowerArmorMod.LOGGER;

public class PowerArmorRenderer extends GeoEntityRenderer<PowerArmorEntity> {
    private final PowerArmorModel<PowerArmorEntity> powerArmorModel;
    private final ArmorModel<PowerArmorEntity> armorModel = new ArmorModel<>();
    private final ArmorPartLayer headLayer      = new ArmorPartLayer(this, BodyPart.HEAD     );
    private final ArmorPartLayer bodyLayer      = new ArmorPartLayer(this, BodyPart.BODY     );
    private final ArmorPartLayer leftArmLayer   = new ArmorPartLayer(this, BodyPart.LEFT_ARM );
    private final ArmorPartLayer rightArmLayer  = new ArmorPartLayer(this, BodyPart.RIGHT_ARM);
    private final ArmorPartLayer leftLegLayer   = new ArmorPartLayer(this, BodyPart.LEFT_LEG );
    private final ArmorPartLayer rightLegLayer  = new ArmorPartLayer(this, BodyPart.RIGHT_LEG);
    private final ArmorPartLayer[] armorLayers = new ArmorPartLayer[]{headLayer, bodyLayer, leftArmLayer, rightArmLayer, leftLegLayer, rightLegLayer};

    public PowerArmorRenderer(EntityRendererManager renderManager) {
        super(renderManager,  new PowerArmorModel<>());

        powerArmorModel = (PowerArmorModel<PowerArmorEntity>)getGeoModelProvider();

        for (ArmorPartLayer armorLayer : armorLayers) {
            addLayer(armorLayer);
        }
    }

    @Override
    public void render(PowerArmorEntity entity, float entityYaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer bufferIn, int packedLightIn) {
        try{
            updateArmor(entity);
            super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
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