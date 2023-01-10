package com.jetug.power_armor_mod.client.gui;

import com.jetug.power_armor_mod.common.minecraft.entity.PowerArmorEntity;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;

public class GuiDragon extends AbstractContainerScreen<ContainerDragon> {
    private static final ResourceLocation texture = new ResourceLocation("power_armor_mod:textures/gui/dragon.png");
    private float mousePosX;
    private float mousePosY;

    public GuiDragon(ContainerDragon dragonInv, Inventory playerInv, Component name) {
        super(dragonInv, playerInv, name);
        this.imageHeight = 214;
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY) {}

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        this.mousePosX = mouseX;
        this.mousePosY = mouseY;
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, texture);
        int k = (this.width - this.imageWidth) / 2;
        int l = (this.height - this.imageHeight) / 2;
        this.blit(matrixStack, k, l, 0, 0, this.imageWidth, this.imageHeight);

        ////////////
        Entity entity = null;

        if (entity instanceof PowerArmorEntity) {
            PowerArmorEntity dragon = (PowerArmorEntity) entity;
            float dragonScale = 1F / Math.max(0.0001F, dragon.getScale());
            InventoryScreen.renderEntityInInventory(k + 88, l + 55, (int) (dragonScale * 23F), k + 51 - this.mousePosX, l + 75 - 50 - this.mousePosY, dragon);
        }
    }


}