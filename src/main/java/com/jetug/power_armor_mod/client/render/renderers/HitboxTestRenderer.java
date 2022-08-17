package com.jetug.power_armor_mod.client.render.renderers;

import com.jetug.power_armor_mod.PowerArmorMod;
import com.jetug.power_armor_mod.client.model.ArmorModel;
import com.jetug.power_armor_mod.client.model.PowerArmorModel;
import com.jetug.power_armor_mod.client.model.TestModel;
import com.jetug.power_armor_mod.client.render.ResourceHelper;
import com.jetug.power_armor_mod.client.render.layers.ArmorPartLayer;
import com.jetug.power_armor_mod.common.minecraft.entity.ArmorSlot;
import com.jetug.power_armor_mod.common.minecraft.entity.HitboxTestEntity;
import com.jetug.power_armor_mod.common.minecraft.entity.IPowerArmor;
import com.jetug.power_armor_mod.common.minecraft.entity.PowerArmorEntity;
import com.jetug.power_armor_mod.common.util.enums.BodyPart;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;


public class HitboxTestRenderer extends GeoEntityRenderer<HitboxTestEntity> {
    private final PowerArmorModel<HitboxTestEntity> powerArmorModel;
    private final ArmorModel<PowerArmorEntity> armorModel = new ArmorModel<>();

    public HitboxTestRenderer(EntityRendererManager renderManager) {
        super(renderManager,  new PowerArmorModel<>());

        powerArmorModel = (PowerArmorModel<HitboxTestEntity>)getGeoModelProvider();
    }
}