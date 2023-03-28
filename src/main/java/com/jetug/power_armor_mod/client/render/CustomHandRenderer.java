package com.jetug.power_armor_mod.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.provider.GeoModelProvider;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

import static com.jetug.power_armor_mod.client.render.renderers.item.HandRenderer.HAND_MODEL;

public class CustomHandRenderer implements IGeoRenderer {
    protected MultiBufferSource rtb = null;

    @Override
    public void setCurrentRTB(MultiBufferSource bufferSource) {
        this.rtb = bufferSource;
    }

    @Override
    public MultiBufferSource getCurrentRTB() {
        return this.rtb;
    }

    @Override
    public GeoModelProvider getGeoModelProvider() {
        return HAND_MODEL;
    }

    @Override
    public ResourceLocation getTextureLocation(Object animatable) {
        return HAND_MODEL.getTextureLocation(animatable);

    }

    public void render(Object animatable,
                       PoseStack poseStack,
                       @Nullable MultiBufferSource bufferSource,
                       int packedLight) {

        Color renderColor = getRenderColor(animatable, 0, poseStack, bufferSource, null, packedLight);
        RenderType renderType = getRenderType(animatable, 0, poseStack, bufferSource, null, packedLight,
                getTextureLocation(animatable));
        render(getModel(), animatable, 0, renderType, poseStack, bufferSource, null, packedLight, OverlayTexture.NO_OVERLAY,
                renderColor.getRed() / 255f, renderColor.getGreen() / 255f, renderColor.getBlue() / 255f,
                renderColor.getAlpha() / 255f);

    }

    public GeoModel getModel(){
        return getGeoModelProvider().getModel(getGeoModelProvider().getModelLocation(null));
    }
}
