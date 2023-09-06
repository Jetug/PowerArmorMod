package com.jetug.chassis_core.client.render.renderers;

import com.jetug.chassis_core.client.model.*;
import com.jetug.chassis_core.client.render.layers.*;
import com.jetug.chassis_core.client.render.utils.GeoUtils;
import com.jetug.chassis_core.common.data.enums.*;
import com.jetug.chassis_core.common.foundation.entity.*;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Vector3f;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.*;
import software.bernie.geckolib3.core.processor.*;
import software.bernie.geckolib3.geo.render.built.*;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static com.jetug.chassis_core.client.render.utils.GeoUtils.*;
import static com.jetug.chassis_core.common.data.constants.Bones.*;
import static net.minecraft.world.entity.EquipmentSlot.MAINHAND;
import static net.minecraft.world.entity.EquipmentSlot.OFFHAND;

public class ChassisRenderer<T extends WearableChassis> extends ModGeoRenderer<T> {
    protected final ChassisModel<T> armorChassisModel;
    protected ItemStack mainHandItem, offHandItem;

    public ChassisRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ChassisModel<>());
        armorChassisModel = (ChassisModel<T>)getGeoModelProvider();
        initLayers();
    }

    private void initLayers(){
//        for (int i = 0; i < ChassisPart.values().length; i++)
        addLayer(new EquipmentLayer(this));
        addLayer(new PlayerHeadLayer(this));
    }

    @Override
    public void render(GeoModel model, T animatable,
                       float partialTick, RenderType type, PoseStack poseStack,
                       MultiBufferSource bufferSource, VertexConsumer buffer,
                       int packedLight, int packedOverlay,
                       float red, float green, float blue, float alpha) {

        if (isInvisible(animatable)) return;
        for (var part : animatable.getEquipment())
            renderEquipment(armorChassisModel ,animatable, part, false);

        super.render(model, animatable, partialTick, type, poseStack, bufferSource, buffer,
                packedLight, packedOverlay, red, green, blue, alpha);
    }


    public boolean isInvisible(T animatable) {
        var clientPlayer = Minecraft.getInstance().player;
        var pov = Minecraft.getInstance().options.getCameraType();

        if (animatable.hasPassenger(clientPlayer) && pov == CameraType.FIRST_PERSON)
            return true;
        return false;
    }

    @Override
    public void renderEarly(T animatable, PoseStack poseStack, float partialTick, MultiBufferSource bufferSource,
                            VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float partialTicks) {
        super.renderEarly(animatable, poseStack, partialTick, bufferSource, buffer, packedLight, packedOverlay, red, green, blue, partialTicks);
        mainHandItem = animatable.getPlayerItem(MAINHAND);
        offHandItem  = animatable.getPlayerItem(OFFHAND);
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, WearableChassis animatable) {
        if(!animatable.hasPlayerPassenger()) return null;

        return switch (boneName) {
            case LEFT_HAND -> offHandItem;
            case RIGHT_HAND-> mainHandItem;
            default -> null;
        };
    }

    @Override
    protected ItemTransforms.TransformType getCameraTransformForItemAtBone(ItemStack stack, String boneName) {
        return switch (boneName) {
            case LEFT_HAND, RIGHT_HAND -> ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND;
            default -> ItemTransforms.TransformType.NONE;
        };
    }

    @Override
    protected void preRenderItem(PoseStack stack, ItemStack item, String boneName, WearableChassis currentEntity, IBone bone) {
        stack.translate(0, 0, -0.09);

        if (!(item.getItem() instanceof ShieldItem)) return;
        if (item == this.mainHandItem) {
            stack.translate(0, 0.125, -0.09);
        }
        else if (item == this.offHandItem) {
            stack.translate(0, 0.125, 0.37);
            stack.mulPose(Vector3f.YP.rotationDegrees(180));
        }
    }

    @Nullable
    public GeoBone getFrameBone(String name){
        return GeoUtils.getFrameBone(armorChassisModel, name);
    }

    @Nullable
    public GeoBone getArmorBone(ResourceLocation resourceLocation, String name){
        return GeoUtils.getArmorBone(resourceLocation, name);
    }
}