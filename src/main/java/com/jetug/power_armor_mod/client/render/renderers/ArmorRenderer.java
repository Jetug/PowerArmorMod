package com.jetug.power_armor_mod.client.render.renderers;

import com.jetug.power_armor_mod.client.ClientConfig;
import com.jetug.power_armor_mod.client.model.PowerArmorModel;
import com.jetug.power_armor_mod.client.render.layers.ArmorPartLayer;
import com.jetug.power_armor_mod.client.render.layers.PlayerHeadLayer;
import com.jetug.power_armor_mod.common.data.enums.BodyPart;
import com.jetug.power_armor_mod.common.data.json.EquipmentAttachment;
import com.jetug.power_armor_mod.common.data.json.EquipmentSettings;
import com.jetug.power_armor_mod.common.foundation.entity.PowerArmorEntity;
import com.jetug.power_armor_mod.common.foundation.particles.Pos3D;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3d;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoBone;

import java.util.HashMap;
import java.util.Random;

import static com.jetug.power_armor_mod.common.data.constants.Bones.LEFT_HAND;
import static com.jetug.power_armor_mod.common.data.constants.Bones.RIGHT_HAND;
import static com.jetug.power_armor_mod.common.data.enums.BodyPart.BACK;
import static net.minecraft.world.entity.EquipmentSlot.MAINHAND;
import static net.minecraft.world.entity.EquipmentSlot.OFFHAND;

public class ArmorRenderer extends ModGeoRenderer<PowerArmorEntity> {
    public static PowerArmorModel<PowerArmorEntity> powerArmorModel;

    public ArmorRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new PowerArmorModel<>());
        powerArmorModel = (PowerArmorModel<PowerArmorEntity>)getGeoModelProvider();
    }

    @Override
    public void render(PowerArmorEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}