package com.jetug.power_armor_mod.client.gui;

import com.jetug.power_armor_mod.common.minecraft.entity.PowerArmorEntity;
import com.jetug.power_armor_mod.common.util.constants.Global;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;

import static com.jetug.power_armor_mod.common.util.constants.Resources.ARMOR_INVENTORY_TEXTURE;

public class PowerArmorGui extends AbstractContainerScreen<PowerArmorContainer> {
    private float mousePosX;
    private float mousePosY;

    public PowerArmorGui(PowerArmorContainer container, Inventory inventory, Component name) {
        super(container, inventory, name);
        //this.imageHeight = 214;
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
    protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, ARMOR_INVENTORY_TEXTURE);
        int x = (width  - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        this.blit(poseStack, x, y, 0, 0, imageWidth, imageHeight);

        if (Global.referenceMob instanceof PowerArmorEntity powerArmor) {
            float scale = 1F / Math.max(0.0001F, powerArmor.getScale());
            InventoryScreen.renderEntityInInventory(x + 88, y + 55, (int) (scale * 23F), x + 51 - this.mousePosX, y + 75 - 50 - this.mousePosY, powerArmor);
        }
    }
}