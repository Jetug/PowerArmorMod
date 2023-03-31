package com.jetug.power_armor_mod.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimatableModel;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.GeoModelProvider;
import software.bernie.geckolib3.model.provider.data.EntityModelData;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;
import software.bernie.geckolib3.util.AnimationUtils;
import software.bernie.geckolib3.util.EModelRenderCycle;

import java.util.ArrayList;
import java.util.Collections;

import static com.jetug.power_armor_mod.client.render.renderers.item.HandRenderer.HAND_MODEL;

public class CustomGeoRenderer<T extends IAnimatable> implements IGeoRenderer<T> {
    protected MultiBufferSource rtb = null;
    public final AnimatedGeoModel<T> model;

    public CustomGeoRenderer(AnimatedGeoModel<T> model){
        this.model = model;
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

    public void render(T animatable,
                       PoseStack poseStack,
                       @Nullable MultiBufferSource bufferSource,
                       int packedLight) {

        var animationEvent = new AnimationEvent<>(animatable, 0, 0,
                Minecraft.getInstance().getFrameTime(), false, Collections.singletonList(new EntityModelData()));
        setCurrentModelRenderCycle(EModelRenderCycle.INITIAL);
        model.setCustomAnimations(animatable, getInstanceId(animatable), animationEvent);

        var renderColor = getRenderColor(animatable, 0, poseStack, bufferSource, null, packedLight);
        var renderType = getRenderType(animatable, 0, poseStack, bufferSource, null, packedLight,
                getTextureLocation(animatable));

        render(getModel(), animatable, 0, renderType, poseStack, bufferSource, null, packedLight, OverlayTexture.NO_OVERLAY,
                renderColor.getRed() / 255f, renderColor.getGreen() / 255f, renderColor.getBlue() / 255f,
                renderColor.getAlpha() / 255f);

    }

    public GeoModel getModel(){
        return getGeoModelProvider().getModel(getGeoModelProvider().getModelLocation(null));
    }
}
