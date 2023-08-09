package com.jetug.power_armor_mod.client.gui.menu;

import com.jetug.power_armor_mod.common.data.constants.*;
import com.jetug.power_armor_mod.common.foundation.screen.menu.*;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.screens.inventory.*;
import net.minecraft.client.renderer.*;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.*;

import static com.mojang.blaze3d.systems.RenderSystem.*;

public class ArmorStationGui extends AbstractContainerScreen<ArmorStationMenu2> {
    private final ArmorStationMenu2 menu;

    public ArmorStationGui(ArmorStationMenu2 menu, Inventory pPlayerInventory, Component pTitle) {
        super(menu, pPlayerInventory, pTitle);
        this.menu = menu;
        this.imageHeight = 187;
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
        setShaderTexture(0, Resources.ARMOR_STATION_GUI);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        this.blit(pPoseStack, x, y, 0, 0, imageWidth, imageHeight);

//        if(menu.blockEntity.frame != null)
//            blit(pPoseStack, x + 160, y + 5, 178, 0, 6, 6);
//        else{
            blit(pPoseStack, x + 160, y + 5, 178, 6, 6, 6);
//        }
    }
}