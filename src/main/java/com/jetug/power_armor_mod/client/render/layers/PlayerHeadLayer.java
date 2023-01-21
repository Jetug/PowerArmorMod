package com.jetug.power_armor_mod.client.render.layers;

import com.jetug.power_armor_mod.client.render.ResourceHelper;
import com.jetug.power_armor_mod.common.minecraft.entity.PowerArmorEntity;
import com.jetug.power_armor_mod.common.util.enums.BodyPart;
import com.jetug.power_armor_mod.common.util.enums.EquipmentType;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

import java.util.ArrayList;

import static com.jetug.power_armor_mod.client.render.ResourceHelper.getAttachments;
import static com.jetug.power_armor_mod.common.util.constants.Resources.POWER_ARMOR_MODEL_LOCATION;

public class PlayerHeadLayer extends GeoLayerRenderer<PowerArmorEntity> {

    public PlayerHeadLayer(IGeoRenderer<PowerArmorEntity> entityRenderer) {
        super(entityRenderer);
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, PowerArmorEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

        if(!entity.isInvisible() && entity.isVehicle() && entity.getControllingPassenger() instanceof AbstractClientPlayer clientPlayer ) {

            //var player = (AbstractClientPlayer)entity.getControllingPassenger();
            var texture = clientPlayer.getSkinTextureLocation();

            int overlay = OverlayTexture.NO_OVERLAY;
            var cameo = RenderType.armorCutoutNoCull(texture);
            matrixStackIn.pushPose();
            matrixStackIn.scale(1.0f, 1.0f, 1.0f);
            matrixStackIn.translate(0.0d, 0.0d, 0.0d);
            this.getRenderer().render(
                    this.getEntityModel().getModel(POWER_ARMOR_MODEL_LOCATION),
                    entity,
                    partialTicks,
                    cameo,
                    matrixStackIn,
                    bufferIn,
                    bufferIn.getBuffer(cameo),
                    packedLightIn,
                    overlay,
                    1f, 1f, 1f, 1f);
            matrixStackIn.popPose();
        }
    }
}