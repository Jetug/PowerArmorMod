package com.jetug.power_armor_mod.client.render.layers;

import com.jetug.power_armor_mod.common.entity.entity_type.PowerArmorEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

import java.util.ArrayList;

import static com.jetug.power_armor_mod.common.util.constants.Resources.POWER_ARMOR_MODEL_LOCATION;

public class ArmorPartLayer extends GeoLayerRenderer {
    public ResourceLocation model = POWER_ARMOR_MODEL_LOCATION;
    public ResourceLocation texture;
    public ArrayList<Tuple<String, String>> boneAttachments = null;

    public ArmorPartLayer(IGeoRenderer entityRendererIn, ResourceLocation model, ResourceLocation texture,
                          ArrayList<Tuple<String, String>> boneAttachments)
    {
        super(entityRendererIn);
        this.model = model;
        this.texture = texture;
        this.boneAttachments = boneAttachments;
    }

//    public ArmorPartLayer(IGeoRenderer entityRendererIn, ResourceLocation model, ResourceLocation texture) {
//        super(entityRendererIn);
//        this.model = model;
//        this.texture = texture;
//    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, Entity entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        PowerArmorEntity PA = (PowerArmorEntity)entityLivingBaseIn;

        if(PA.body.getDurability() > 0) {
            RenderType cameo = RenderType.armorCutoutNoCull(texture);
            matrixStackIn.pushPose();
            //Move or scale the model as you see fit
            matrixStackIn.scale(1.0f, 1.0f, 1.0f);
            matrixStackIn.translate(0.0d, 0.0d, 0.0d);
            this.getRenderer().render(this.getEntityModel().getModel(model), entityLivingBaseIn, partialTicks, cameo, matrixStackIn, bufferIn,
                    bufferIn.getBuffer(cameo), packedLightIn, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);
            matrixStackIn.popPose();
        }
    }
}
