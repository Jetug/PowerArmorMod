package com.jetug.chassis_core.client.render.renderers;

import com.jetug.chassis_core.client.model.EquipmentModel;
import com.jetug.chassis_core.common.foundation.item.ChassisEquipment;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class EquipmentRenderer extends GeoItemRenderer<ChassisEquipment> {
    //private final EquipmentModel<T> model = new EquipmentModel<>();

    public EquipmentRenderer() {
        super(new EquipmentModel<>());
    }

    @Override
    public void render(ChassisEquipment animatable, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, ItemStack stack) {
        super.render(animatable, poseStack, bufferSource, packedLight, stack);
    }
}
