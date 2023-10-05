package com.jetug.chassis_core.client.render.renderers;

import com.jetug.chassis_core.common.foundation.entity.HandEntity;
import com.jetug.chassis_core.common.foundation.entity.WearableChassis;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.cache.object.BakedGeoModel;
import mod.azure.azurelib.core.object.Color;
import mod.azure.azurelib.model.GeoModel;
import mod.azure.azurelib.model.data.EntityModelData;
import mod.azure.azurelib.renderer.GeoRenderer;
import mod.azure.azurelib.renderer.layer.GeoRenderLayer;
import mod.azure.azurelib.renderer.layer.GeoRenderLayersContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.util.EModelRenderCycle;

import java.util.Collections;
import java.util.List;

public class CustomGeoRenderer<T extends GeoEntity > implements GeoRenderer<T> {
    protected MultiBufferSource rtb = null;
    public final GeoModel<T> model;
    protected final GeoRenderLayersContainer<T> renderLayers = new GeoRenderLayersContainer(this);
    protected final List<GeoLayerRenderer> layerRenderers = new ObjectArrayList<>();

    public CustomGeoRenderer(GeoModel<T> model){
        this.model = model;
    }

    public final boolean addLayer(GeoLayerRenderer layer) {
        return this.layerRenderers.add(layer);
    }

    public CustomGeoRenderer addRenderLayer(GeoRenderLayer<T> renderLayer) {
        this.renderLayers.addLayer(renderLayer);
        return this;
    }


    @Override
    public List<GeoRenderLayer<T>> getRenderLayers() {
        return this.renderLayers.getRenderLayers();
    }

//    public void setCurrentRTB(MultiBufferSource bufferSource) {
//        this.rtb = bufferSource;
//    }
//
//    public MultiBufferSource getCurrentRTB() {
//        return this.rtb;
//    }

    public GeoModel<T> getGeoModelProvider() {
        return model;
    }

    @Override
    public GeoModel<T> getGeoModel() {
        return model;
    }

    private T animatable;

    @Override
    public T getAnimatable() {
        return animatable;
    }

//    @Override
//    public ResourceLocation getTextureLocation(T animatable) {
//        return model.getTextureRec(animatable);
//    }

    @Override
    public void fireCompileRenderLayersEvent() {}

    @Override
    public boolean firePreRenderEvent(PoseStack poseStack, BakedGeoModel bakedGeoModel, MultiBufferSource multiBufferSource, float v, int i) {
        return false;
    }

    @Override
    public void firePostRenderEvent(PoseStack poseStack, BakedGeoModel bakedGeoModel, MultiBufferSource multiBufferSource, float v, int i) {}

    @Override
    public void updateAnimatedTextureFrame(T handEntity) {}

    public void render(T animatable, PoseStack poseStack, MultiBufferSource bufferSource,
                       RenderType renderType, VertexConsumer buffer,
                       float partialTick, int packedLight) {
//        GeoRenderer.super.defaultRender(poseStack, animatable, bufferSource, renderType, buffer, 0, partialTick, packedLight);

        // var hand = animatable.getHandEntity();
        this.animatable = animatable;

        poseStack.pushPose();
        var renderColor = this.getRenderColor(animatable, partialTick, packedLight);
        var red = renderColor.getRedFloat();
        var green = renderColor.getGreenFloat();
        var blue = renderColor.getBlueFloat();
        var alpha = renderColor.getAlphaFloat();
        var packedOverlay = this.getPackedOverlay(animatable, 0.0F);

        BakedGeoModel model = this.getGeoModel().getBakedModel(this.getGeoModel().getModelResource(animatable));
        if (renderType == null) {
            renderType = this.getRenderType(animatable, this.getTextureLocation(animatable), bufferSource, partialTick);
        }

        if (buffer == null) {
            buffer = bufferSource.getBuffer(renderType);
        }

        this.preRender(poseStack, animatable, model, bufferSource, buffer, false, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        if (this.firePreRenderEvent(poseStack, model, bufferSource, partialTick, packedLight)) {
            this.preApplyRenderLayers(poseStack, animatable, model, renderType, bufferSource, buffer, (float)packedLight, packedLight, packedOverlay);
            this.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, false, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
            this.applyRenderLayers(poseStack, animatable, model, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
            this.postRender(poseStack, animatable, model, bufferSource, buffer, false, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
            this.firePostRenderEvent(poseStack, model, bufferSource, partialTick, packedLight);
        }

        poseStack.popPose();
    }

//    public void render(WearableChassis chassisEntity, PoseStack poseStack,
//                       @Nullable MultiBufferSource bufferSource,
//                       int packedLight) {
//
//        this.baseRender(chassisEntity.getHandEntity(), poseStack, bufferSource, packedLight);
//        this.renderLayers(chassisEntity, poseStack, bufferSource, packedLight);
//    }
//
//    private void renderLayers(WearableChassis chassisEntity, PoseStack poseStack, @Nullable MultiBufferSource bufferSource, int packedLight) {
//        var partialTick = Minecraft.getInstance().getFrameTime();
//        for (var layerRenderer : this.layerRenderers) {
//            renderLayer(poseStack, bufferSource, packedLight,
//                    chassisEntity, 0, 0, partialTick, 0,
//                    0, 0, layerRenderer);
//        }
//    }

//    protected void renderLayer(PoseStack poseStack, MultiBufferSource bufferSource,
//                               int packedLight, WearableChassis animatable,
//                               float limbSwing, float limbSwingAmount,
//                               float partialTick, float rotFloat, float netHeadYaw,
//                               float headPitch, GeoLayerRenderer layerRenderer) {
//        layerRenderer.render(poseStack, bufferSource, packedLight, animatable, limbSwing,
//                limbSwingAmount, partialTick, rotFloat,
//                netHeadYaw, headPitch);
//    }
//
//    private void baseRender(HandEntity animatable, PoseStack poseStack, @Nullable MultiBufferSource bufferSource, int packedLight) {
//        var animationEvent = new AnimationEvent<>(animatable, 0, 0,
//                Minecraft.getInstance().getFrameTime(), false,
//                Collections.singletonList(new EntityModelData()));
//        setCurrentModelRenderCycle(EModelRenderCycle.INITIAL);
//        model.setCustomAnimations(animatable, getInstanceId(animatable), animationEvent);
//
//        var renderColor = getRenderColor(animatable, 0, poseStack, bufferSource, null, packedLight);
//        var renderType = getRenderType(animatable, 0, poseStack, bufferSource, null, packedLight,
//                getTextureLocation(animatable));
//
//        render(getModel(), animatable, 0, renderType, poseStack, bufferSource, null, packedLight, OverlayTexture.NO_OVERLAY,
//                renderColor.getRed() / 255f, renderColor.getGreen() / 255f, renderColor.getBlue() / 255f,
//                renderColor.getAlpha() / 255f);
//    }
//
//    public GeoModel getModel(){
//        return getGeoModelProvider().getModel(getGeoModelProvider().getModelLocation(null));
//    }
}
