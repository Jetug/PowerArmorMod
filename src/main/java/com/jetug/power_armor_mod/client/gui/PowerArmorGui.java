package com.jetug.power_armor_mod.client.gui;

import com.jetug.power_armor_mod.common.foundation.entity.PowerArmorEntity;
import com.jetug.power_armor_mod.common.util.Pos2D;
import com.jetug.power_armor_mod.common.util.constants.Global;
import com.jetug.power_armor_mod.common.util.enums.ActionType;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Inventory;

import java.awt.*;

import static com.jetug.power_armor_mod.common.foundation.registery.ItemRegistry.*;
import static com.jetug.power_armor_mod.common.network.PacketSender.doServerAction;
import static com.jetug.power_armor_mod.common.util.constants.Gui.*;
import static com.jetug.power_armor_mod.common.util.constants.Resources.*;
import static net.minecraft.world.item.Items.*;

@SuppressWarnings("DataFlowIssue")
public class PowerArmorGui extends AbstractContainerScreen<PowerArmorContainer> {
    public static final int ENTITY_POS_X = 41;
    public static final int ENTITY_POS_Y = 73;
    public static final float MIN_SCALE = 0.0001F;

    private float mousePosX;
    private float mousePosY;

    public PowerArmorGui(PowerArmorContainer container, Inventory inventory, Component name) {
        super(container, inventory, name);
    }
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int pButton) {
        var rect = new Rectangle(leftPos, topPos - TAB_HEIGHT, TAB_WIDTH, TAB_HEIGHT);

        if(rect.contains(mouseX, mouseY)){
            minecraft.setScreen(new InventoryScreen(minecraft.player));
        }

        return super.mouseClicked(mouseX, mouseY, pButton);
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY) {}

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        this.mousePosX = mouseX;
        this.mousePosY = mouseY;
        this.renderTooltip(matrixStack, mouseX, mouseY);
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        Pos2D playerIconPos = TOP_TAB_ICON_POS_1;
        Pos2D paIconPos     = TOP_TAB_ICON_POS_2;

        if(minecraft.player.isCreative()){
            playerIconPos = BOTTOM_TAB_ICON_POS_1;
            paIconPos     = BOTTOM_TAB_ICON_POS_2;
        }
        else{

        }

        this.itemRenderer.renderAndDecorateItem(CRAFTING_TABLE.getDefaultInstance(), leftPos + playerIconPos.x, topPos + playerIconPos.y);
        this.itemRenderer.renderAndDecorateItem(PA_FRAME.get().getDefaultInstance(), leftPos + paIconPos.x, topPos + paIconPos.y);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, ARMOR_INVENTORY_TEXTURE);
        this.blit(poseStack, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        if (Global.referenceMob instanceof PowerArmorEntity powerArmor) {
            renderEntity(powerArmor);
        }

        RenderSystem.setShaderTexture(0, PLAYER_INVENTORY_TABS);
        this.blit(poseStack, leftPos, topPos - 28, 0, 32, 57, 62);
    }

    private double getRight(){
        return leftPos + imageWidth;
    }

    private double getBottom(){
        return topPos + imageHeight;
    }

    private void renderEntity(PowerArmorEntity powerArmor) {
        var scale = 1F / Math.max(MIN_SCALE, powerArmor.getScale());

        InventoryScreen.renderEntityInInventory(
                leftPos + ENTITY_POS_X,
                topPos + ENTITY_POS_Y,
                (int)(scale * 23F),
                leftPos + 51 - mousePosX,
                topPos + 75 - 50 - mousePosY,
                powerArmor);
    }
}