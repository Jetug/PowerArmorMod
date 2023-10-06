package com.jetug.chassis_core.client.render.layers;

import com.jetug.chassis_core.common.foundation.entity.WearableChassis;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mod.azure.azurelib.cache.object.BakedGeoModel;
import mod.azure.azurelib.renderer.GeoRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.HashMap;

import static com.jetug.chassis_core.common.util.helpers.TextureHelper.*;

public class PlayerHeadLayer<T extends WearableChassis> extends LayerBase<T>  {
    private static final HashMap<String, ResourceLocation> playerHeads = new HashMap<>();
    private int textureWidth;
    private int textureHeight;

    public PlayerHeadLayer(GeoRenderer<T> entityRenderer) {
        super(entityRenderer);
    }

    @Override
    public void render(PoseStack poseStack, T entity, BakedGeoModel bakedModel, RenderType renderType,
                       MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        if(!entity.isInvisible() && entity.isVehicle() && entity.getControllingPassenger() instanceof Player clientPlayer ) {
            var texture = getHeadLayerRL(clientPlayer, entity);
            if (texture == null) return;
            renderLayer(poseStack, entity, bakedModel, bufferSource, partialTick, packedLight, texture);
        }
    }

//    @Override
//    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T entity,
//                       float limbSwing, float limbSwingAmount, float partialTicks,
//                       float ageInTicks, float netHeadYaw, float headPitch) {
//        if(!entity.isInvisible() && entity.isVehicle() && entity.getControllingPassenger() instanceof Player clientPlayer ) {
//            var texture = getHeadLayerRL(clientPlayer, entity);
//            if (texture == null) return;
//
//            var cameo = RenderType.armorCutoutNoCull(texture);
//            int overlay = OverlayTexture.NO_OVERLAY;
//
//            poseStack.pushPose();
//            poseStack.scale(1.0f, 1.0f, 1.0f);
//            poseStack.translate(0.0d, 0.0d, 0.0d);
//            var model = getRenderer().getGeoModelProvider().getModelLocation(entity);
//
//            this.getRenderer().render(
//                    getEntityModel().getModel(model),
//                    entity,
//                    partialTicks,
//                    cameo,
//                    poseStack,
//                    buffer,
//                    buffer.getBuffer(cameo),
//                    packedLight,
//                    overlay,
//                    1f, 1f, 1f, 1f);
//            poseStack.popPose();
//        }
//    }

    private void setTextureSize(T entity){
        var size = getTextureSize(getRenderer().getTextureLocation(entity));
        textureWidth = size.getA();
        textureHeight = size.getB();
    }

    @Nullable
    private ResourceLocation getHeadLayerRL(Player clientPlayer, T entity) {
        var tag = clientPlayer.getUUID().toString();

        if (!playerHeads.containsKey(tag)) {
            setTextureSize(entity);
            var texture = getDefaultResizedHeadTexture(clientPlayer, textureWidth, textureHeight);
            if(texture == null) return null;
            var resource = createResource(tag, texture);
            playerHeads.put(tag, resource);

            downloadPlayerHeadTexture(clientPlayer);
        }
        return playerHeads.get(tag);
    }

    private void downloadPlayerHeadTexture(Player clientPlayer){
       var thread = new Thread(() -> {
            var tag = clientPlayer.getUUID().toString();
            var texture = getResizedHeadTexture(clientPlayer, textureWidth, textureHeight);
            if(texture == null) return;
            var resource = createResource(tag, texture);
            playerHeads.put(tag, resource);
        });
       thread.start();
    }
}