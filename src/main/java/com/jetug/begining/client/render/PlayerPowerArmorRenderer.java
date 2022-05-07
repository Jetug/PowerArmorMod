package com.jetug.begining.client.render;

import com.google.common.collect.Lists;
import com.jetug.begining.ExampleMod;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.event.RenderNameplateEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.shadowed.eliotlash.mclib.utils.MathUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

public class PlayerPowerArmorRenderer<T extends PlayerEntity> extends EntityRenderer<PlayerEntity> {
    public PlayerPowerArmorRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }

//    @Override
//    public void render(PlayerPowerArmorEntity entity, float entityYaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer bufferIn, int packedLightIn) {
//        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
//    }

    @Override
    public ResourceLocation getTextureLocation(PlayerEntity p_110775_1_) {
        return null;
    }
}