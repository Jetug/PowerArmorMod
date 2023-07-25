package com.jetug.power_armor_mod.client.render.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.ExtendedGeoEntityRenderer;

public abstract class ModGeoRenderer<T extends LivingEntity & IAnimatable> extends ExtendedGeoEntityRenderer<T> {

    protected ModGeoRenderer(EntityRendererProvider.Context renderManager, AnimatedGeoModel<T> modelProvider) {
        super(renderManager, modelProvider);
    }

    protected ModGeoRenderer(EntityRendererProvider.Context renderManager, AnimatedGeoModel<T> modelProvider, float widthScale, float heightScale, float shadowSize) {
        super(renderManager, modelProvider, widthScale, heightScale, shadowSize);
    }

    @Override
    protected boolean isArmorBone(GeoBone bone) {
        return false;
    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, T animatable) {
        return null;
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, T animatable) {
        return null;
    }

    @Override
    protected ItemTransforms.TransformType getCameraTransformForItemAtBone(ItemStack stack, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected BlockState getHeldBlockForBone(String boneName, T animatable) {
        return null;
    }

    @Override
    protected void preRenderItem(PoseStack poseStack, ItemStack stack, String boneName, T animatable, IBone bone) {

    }

    @Override
    protected void preRenderBlock(PoseStack poseStack, BlockState state, String boneName, T animatable) {

    }

    @Override
    protected void postRenderItem(PoseStack poseStack, ItemStack stack, String boneName, T animatable, IBone bone) {

    }

    @Override
    protected void postRenderBlock(PoseStack poseStack, BlockState state, String boneName, T animatable) {

    }
}
