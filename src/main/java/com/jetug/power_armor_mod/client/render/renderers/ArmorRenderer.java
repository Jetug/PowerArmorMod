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
        var bone = (GeoBone)modelProvider.getAnimationProcessor().getBone("left_jet");
        showJetpackParticles(entity, bone);
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    private void showJetpackParticles(PowerArmorEntity entity, GeoBone bone) {
        if(bone == null || !entity.hasPlayerPassenger()) return;

        var minecraft = Minecraft.getInstance();
        var particle = ParticleTypes.FLAME;
        var rand = new Random();
        var random = (rand.nextFloat() - 0.5F) * 0.1F;

        var pos3D = new Pos3D(bone.getWorldPosition())
                .rotate(bone.getRotationX(), bone.getRotationY(), bone.getRotationZ())
                .translate(0, 0, 0);

        var vLeft  = new Pos3D(0, 0, 0);
        var vRight = new Pos3D(0, 0, 0);
        var vCenter = new Pos3D(0,0,0);

        var v = pos3D.translate(vLeft).translate(new Pos3D(minecraft.player.getDeltaMovement()));
        minecraft.level.addParticle(particle, v.x, v.y, v.z, random, -0.2D, random);

        v = pos3D.translate(vRight).translate(new Pos3D(minecraft.player.getDeltaMovement()));
        minecraft.level.addParticle(particle, v.x, v.y, v.z, random, -0.2D, random);

        v = pos3D.translate(vCenter).translate(new Pos3D(minecraft.player.getDeltaMovement()));
        minecraft.level.addParticle(particle, v.x, v.y, v.z, random, -0.2D, random);
    }
}