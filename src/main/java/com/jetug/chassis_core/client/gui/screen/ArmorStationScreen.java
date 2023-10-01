package com.jetug.chassis_core.client.gui.screen;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.AbstractContainerMenu;

import static com.mojang.blaze3d.systems.RenderSystem.*;

public class ArmorStationScreen<T extends AbstractContainerMenu> extends GuiBase<T> {
    private final ResourceLocation guiResource;

    public ArmorStationScreen(T menu, Inventory pPlayerInventory,
                              Component pTitle, ResourceLocation guiResource) {
        super(menu, pPlayerInventory, pTitle);
        this.guiResource = guiResource;
        this.imageHeight = 187;
        this.inventoryLabelY = imageHeight - 94;
    }

    @Override
    public void render(PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
        renderBackground(pPoseStack);
        super.render(pPoseStack, mouseX, mouseY, delta);
        renderTooltip(pPoseStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        setShader(GameRenderer::getPositionTexShader);
        setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        setShaderTexture(0, guiResource);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        this.blit(pPoseStack, x, y, 0, 0, imageWidth, imageHeight);
        //blit(pPoseStack, x + 160, y + 5, 178, 6, 6, 6);
    }
}