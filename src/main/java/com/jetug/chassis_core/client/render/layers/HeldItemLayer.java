package com.jetug.chassis_core.client.render.layers;

import mod.azure.azurelib.cache.object.GeoBone;
import mod.azure.azurelib.core.animatable.GeoAnimatable;
import mod.azure.azurelib.renderer.GeoRenderer;
import mod.azure.azurelib.renderer.layer.BlockAndItemGeoLayer;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.item.ItemStack;

import java.util.function.BiFunction;

import static com.jetug.chassis_core.common.data.constants.Bones.LEFT_HAND;
import static com.jetug.chassis_core.common.data.constants.Bones.RIGHT_HAND;

public class HeldItemLayer<T extends GeoAnimatable> extends BlockAndItemGeoLayer<T> {
    public HeldItemLayer(GeoRenderer<T> renderer, BiFunction<GeoBone, T, ItemStack> stackForBone) {
        super(renderer, stackForBone, (i, g) -> null);
    }

    @Override
    protected ItemTransforms.TransformType getTransformTypeForStack(GeoBone bone, ItemStack stack, GeoAnimatable animatable) {
        return switch (bone.getName()) {
            case LEFT_HAND, RIGHT_HAND -> ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND;
            default -> ItemTransforms.TransformType.NONE;
        };
    }
}
