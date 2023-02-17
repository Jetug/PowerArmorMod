package com.jetug.power_armor_mod.client.gui;

import com.jetug.power_armor_mod.common.foundation.entity.PowerArmorEntity;
import com.jetug.power_armor_mod.common.foundation.registery.ItemRegistry;
import com.jetug.power_armor_mod.common.util.constants.Global;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Items;

import static com.jetug.power_armor_mod.common.util.constants.Resources.ARMOR_INVENTORY_TEXTURE;
import static com.jetug.power_armor_mod.common.util.constants.Resources.PLAYER_INVENTORY_TABS;

public class PowerArmorGui extends AbstractContainerScreen<PowerArmorContainer> {
    public static final int ENTITY_POS_X = 41;
    public static final int ENTITY_POS_Y = 73;

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
        this.itemRenderer.renderAndDecorateItem(Items.CRAFTING_TABLE.getDefaultInstance(), this.leftPos + 6, this.topPos - 20);
        this.itemRenderer.renderAndDecorateItem(ItemRegistry.PA_FRAME.get().getDefaultInstance(), this.leftPos + 35, this.topPos - 20);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, ARMOR_INVENTORY_TEXTURE);
        int x = (width  - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        if (Global.referenceMob instanceof PowerArmorEntity powerArmor) {
            float scale = 1F / Math.max(0.0001F, powerArmor.getScale());
            InventoryScreen.renderEntityInInventory(
                    x + ENTITY_POS_X,
                    y + ENTITY_POS_Y,
                    (int)(scale * 23F),
                    x + 51 - this.mousePosX,
                    y + 75 - 50 - this.mousePosY,
                    powerArmor);
        }

        RenderSystem.setShaderTexture(0, PLAYER_INVENTORY_TABS);

        this.blit(poseStack, x, y, 0, 0, imageWidth, imageHeight);
    }
}