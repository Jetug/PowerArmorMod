package com.jetug.chassis_core.client.render.renderers;

import com.jetug.chassis_core.common.foundation.entity.HandEntity;
import com.jetug.chassis_core.client.model.HandModel;
import com.jetug.chassis_core.client.render.layers.EquipmentLayer;
import com.jetug.chassis_core.common.foundation.entity.WearableChassis;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mod.azure.azurelib.cache.object.GeoBone;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.client.renderer.MultiBufferSource;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

@SuppressWarnings("unchecked")
public class CustomHandRenderer extends CustomGeoRenderer {
    protected static CustomHandRenderer handRenderer;
    protected static final HandModel handModel = new HandModel();
    protected WearableChassis currentChassis;

//    static {
//        AnimationController.addModelFetcher(animatable -> animatable instanceof HandEntity ? handModel : null);
//    }

    public CustomHandRenderer(GeoModel<HandEntity> model) {
        super(model);
        addRenderLayer(new EquipmentLayer<>(this));
    }

    public void render(WearableChassis chassisEntity, PoseStack poseStack,
                       @Nullable MultiBufferSource bufferSource, int packedLight) {
        currentChassis = chassisEntity;
        //super.render(chassisEntity, poseStack, bufferSource, packedLight);
    }

//    @Override
//    public void renderRecursively(GeoBone bone, PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
//        super.renderRecursively(bone, poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
//    }

//    @Override
//    public void renderChildBones(GeoBone bone, PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay,
//                                 float red, float green, float blue, float alpha) {
//        if (bone.childBonesAreHiddenToo())
//            return;
//
//        doSafe(() -> {
//            var bonesToRender = new ArrayList<>(bone.childBones);
//            var equipmentBones = currentChassis.getEquipmentBones(bone.name);
//            bonesToRender.addAll(equipmentBones);
//
//            for (GeoBone childBone : bonesToRender) {
//                renderRecursively(childBone, poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
//            }
//        });
//    }

    public static void doSafe(Runnable runnable){
        try{ runnable.run(); }
        catch (Exception e){ e.printStackTrace(); }
    }

    public static void registerHandRenderer(){
        handRenderer = new CustomHandRenderer(handModel);
    }

    public static CustomHandRenderer getHandRenderer(){
        return handRenderer;
    }
}
