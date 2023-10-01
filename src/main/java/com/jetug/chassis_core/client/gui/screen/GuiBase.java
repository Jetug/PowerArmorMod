package com.jetug.chassis_core.client.gui.screen;

import com.google.common.collect.Lists;
import com.jetug.chassis_core.client.gui.ui.IconButton;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

import java.util.List;

public abstract class GuiBase<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {
    private final List<IconButton> buttons = Lists.newArrayList();

    public GuiBase(T pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    protected void renderLabels(PoseStack pPoseStack, int pX, int pY) {
        for(var button : this.buttons) {
            if (button.isShowingTooltip()) {
                button.renderToolTip(pPoseStack, pX - this.leftPos, pY - this.topPos);
                break;
            }
        }
    }

    protected <B extends IconButton> void addButton(B button) {
        this.addRenderableWidget(button);
        this.buttons.add(button);
    }

}
