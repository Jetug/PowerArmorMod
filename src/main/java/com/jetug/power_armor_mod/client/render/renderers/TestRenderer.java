package com.jetug.power_armor_mod.client.render.renderers;

import com.jetug.power_armor_mod.PowerArmorMod;
import com.jetug.power_armor_mod.client.model.TestModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;

//

public class TestRenderer <Type extends LivingEntity> extends LivingRenderer<Type, TestModel<Type>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(PowerArmorMod.MOD_ID, "textures/entities/test_model.png");

    public TestRenderer(EntityRendererManager context) {
        super(context, new TestModel<>(), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(Type pEntity) {
        return TEXTURE;
    }
}