package com.jetug.power_armor_mod.client.render.layers;

import com.jetug.power_armor_mod.common.minecraft.entity.PowerArmorEntity;
import com.jetug.power_armor_mod.common.util.constants.Global;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

import javax.annotation.Nullable;
import java.util.HashMap;

import static com.jetug.power_armor_mod.common.util.constants.Resources.POWER_ARMOR_MODEL_LOCATION;
import static com.jetug.power_armor_mod.common.util.helpers.TextureHelper.getHeadLayerTexture;
import static com.jetug.power_armor_mod.common.util.helpers.TextureHelper.getTextureSize;

public class PlayerHeadLayer extends GeoLayerRenderer<PowerArmorEntity> {
    private final IGeoRenderer<PowerArmorEntity> entityRenderer;
    private final HashMap<String, ResourceLocation> playerTextures = new HashMap<>();

    public PlayerHeadLayer(IGeoRenderer<PowerArmorEntity> entityRenderer) {
        super(entityRenderer);
        this.entityRenderer = entityRenderer;
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, PowerArmorEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if(!entity.isInvisible() && entity.isVehicle() && entity.getControllingPassenger() instanceof AbstractClientPlayer clientPlayer ) {

            var texture = getHeadLayerRL(clientPlayer, entity);
            if (texture == null) return;

            var cameo = RenderType.armorCutoutNoCull(texture);
            int overlay = OverlayTexture.NO_OVERLAY;

            matrixStackIn.pushPose();
            matrixStackIn.scale(1.0f, 1.0f, 1.0f);
            matrixStackIn.translate(0.0d, 0.0d, 0.0d);
            this.getRenderer().render(
                    getEntityModel().getModel(POWER_ARMOR_MODEL_LOCATION),
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

    @Nullable
    private ResourceLocation getHeadLayerRL(AbstractClientPlayer clientPlayer, PowerArmorEntity entity) {
        var tag = clientPlayer.getUUID().toString();
        var textureManager = Minecraft.getInstance().getTextureManager();

        if (playerTextures.containsKey(tag)) {
            return playerTextures.get(tag);
        } else {
            var size = getTextureSize(entityRenderer.getTextureLocation(entity));
            var dynamicTexture = getHeadLayerTexture(clientPlayer, size.getA(), size.getB());
            if (dynamicTexture == null) return null;
            var headTextureLocation = new ResourceLocation(Global.MOD_ID, tag);

            textureManager.register(headTextureLocation, dynamicTexture);
            playerTextures.put(tag, headTextureLocation);
            return headTextureLocation;
        }
    }
}