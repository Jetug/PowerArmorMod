package com.jetug.power_armor_mod.client.render.layers;

import com.jetug.power_armor_mod.common.foundation.entity.PowerArmorEntity;
import com.jetug.power_armor_mod.common.data.constants.Global;
import com.jetug.power_armor_mod.common.util.helpers.texture.PlayerSkins;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

import javax.annotation.Nullable;
import java.util.HashMap;

import static com.jetug.power_armor_mod.common.data.constants.Resources.*;
import static com.jetug.power_armor_mod.common.util.helpers.TextureHelper.*;
import static java.lang.System.out;

public class PlayerHeadLayer extends GeoLayerRenderer<PowerArmorEntity> {
    private final IGeoRenderer<PowerArmorEntity> entityRenderer;
    private static final HashMap<String, ResourceLocation> playerTextures = new HashMap<>();
    private final TextureManager textureManager;
    private final int textureWidth;
    private final int textureHeight;

    public PlayerHeadLayer(IGeoRenderer<PowerArmorEntity> entityRenderer) {
        super(entityRenderer);
        this.entityRenderer = entityRenderer;
        this.textureManager =  Minecraft.getInstance().getTextureManager();
        var size = getTextureSize(entityRenderer.getTextureLocation(null));
        textureWidth = size.getA();
        textureHeight = size.getB();
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, PowerArmorEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if(!entity.isInvisible() && entity.isVehicle() && entity.getControllingPassenger() instanceof Player clientPlayer ) {
            var test = entity.getControllingPassenger();
            out.println(test);

            var texture = getHeadLayerRL(clientPlayer, entity);
            if (texture == null) return;

            var cameo = RenderType.armorCutoutNoCull(texture);
            int overlay = OverlayTexture.NO_OVERLAY;

            matrixStackIn.pushPose();
            matrixStackIn.scale(1.0f, 1.0f, 1.0f);
            matrixStackIn.translate(0.0d, 0.0d, 0.0d);
            this.getRenderer().render(
                    getEntityModel().getModel(FRAME_MODEL_LOCATION),
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
    private ResourceLocation getHeadLayerRL(Player clientPlayer, PowerArmorEntity entity) {
        var tag = clientPlayer.getUUID().toString();

        if (!playerTextures.containsKey(tag)) {
            var texture = getDefaultResizedHeadTexture(clientPlayer, textureWidth, textureHeight);
            if(texture == null) return null;
            var resource = createResource(tag, texture);
            playerTextures.put(tag, resource);

            downloadPlayerHeadTexture( clientPlayer, entity);
        }
        return playerTextures.get(tag);
    }

    private void downloadPlayerHeadTexture(Player clientPlayer, PowerArmorEntity entity){
       var thread = new Thread(() -> {
            var tag = clientPlayer.getUUID().toString();
            var texture = getResizedHeadTexture(clientPlayer, textureWidth, textureHeight);
            if(texture == null) return;
            var resource = createResource(tag, texture);
            playerTextures.put(tag, resource);
        });
       thread.start();
    }

    private ResourceLocation createResource(String playerUUID, AbstractTexture abstractTexture){
        var headTextureLocation = new ResourceLocation(Global.MOD_ID, playerUUID);
        textureManager.register(headTextureLocation, abstractTexture);
        return headTextureLocation;
    }
}