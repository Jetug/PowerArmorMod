package com.jetug.power_armor_mod.client.render.renderers;

import com.jetug.power_armor_mod.common.foundation.entity.ArmorChassisEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;
import software.bernie.geckolib3.util.EModelRenderCycle;

import java.util.Collections;
import java.util.List;

public class CustomGeoRenderer<T extends IAnimatable> implements IGeoRenderer<T> {
    protected MultiBufferSource rtb = null;
    public final AnimatedGeoModel<T> model;

    protected final List<GeoLayerRenderer> layerRenderers = new ObjectArrayList<>();

    public CustomGeoRenderer(AnimatedGeoModel<T> model){
        this.model = model;
    }

    public final boolean addLayer(GeoLayerRenderer layer) {
        return this.layerRenderers.add(layer);
    }

    @Override
    public void setCurrentRTB(MultiBufferSource bufferSource) {
        this.rtb = bufferSource;
    }

    @Override
    public MultiBufferSource getCurrentRTB() {
        return this.rtb;
    }

    @Override
    public AnimatedGeoModel<T> getGeoModelProvider() {
        return model;
    }

    @Override
    public ResourceLocation getTextureLocation(T animatable) {
        return model.getTextureLocation(animatable);
    }

    public void render(T animatable, ArmorChassisEntity chassisEntity, PoseStack poseStack,
                       @Nullable MultiBufferSource bufferSource,
                       int packedLight) {

        var animationEvent = new AnimationEvent<>(animatable, 0, 0,
                Minecraft.getInstance().getFrameTime(), false,
                Collections.singletonList(new EntityModelData()));
        setCurrentModelRenderCycle(EModelRenderCycle.INITIAL);
        model.setCustomAnimations(animatable, getInstanceId(animatable), animationEvent);

        var renderColor = getRenderColor(animatable, 0, poseStack, bufferSource, null, packedLight);
        var renderType = getRenderType(animatable, 0, poseStack, bufferSource, null, packedLight,
                getTextureLocation(animatable));

        render(getModel(), animatable, 0, renderType, poseStack, bufferSource, null, packedLight, OverlayTexture.NO_OVERLAY,
                renderColor.getRed() / 255f, renderColor.getGreen() / 255f, renderColor.getBlue() / 255f,
                renderColor.getAlpha() / 255f);

        var partialTick = Minecraft.getInstance().getFrameTime();
        for (var layerRenderer : this.layerRenderers) {
            renderLayer(poseStack, bufferSource, packedLight, chassisEntity, 0, 0, partialTick, 0,
                    0, 0, bufferSource, layerRenderer);
        }

    }

    protected void renderLayer(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, ArmorChassisEntity animatable,
                               float limbSwing, float limbSwingAmount, float partialTick, float rotFloat, float netHeadYaw,
                               float headPitch, MultiBufferSource bufferSource2, GeoLayerRenderer layerRenderer) {
        layerRenderer.render(poseStack, bufferSource, packedLight, animatable, limbSwing,
                limbSwingAmount, partialTick, rotFloat,
                netHeadYaw, headPitch);
    }
    public GeoModel getModel(){
        return getGeoModelProvider().getModel(getGeoModelProvider().getModelLocation(null));
    }
}
