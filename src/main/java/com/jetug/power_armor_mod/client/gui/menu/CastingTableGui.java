package com.jetug.power_armor_mod.client.gui.menu;

import com.jetug.power_armor_mod.common.data.constants.Resources;
import com.jetug.power_armor_mod.common.foundation.screen.menu.*;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import static com.jetug.generated.resources.Textures.GUI_CASTING_TABLE;
import static com.mojang.blaze3d.systems.RenderSystem.*;

public class CastingTableGui extends AbstractContainerScreen<CastingTableMenu> {
    private final CastingTableMenu menu;

    public CastingTableGui(CastingTableMenu menu, Inventory pPlayerInventory, Component pTitle) {
        super(menu, pPlayerInventory, pTitle);
        this.menu = menu;
        this.imageHeight = 166;
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
        setShaderTexture(0, GUI_CASTING_TABLE);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        this.blit(pPoseStack, x, y, 0, 0, imageWidth, imageHeight);
    }
}
