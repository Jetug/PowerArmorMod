package com.jetug.chassis_core.client.gui.screen;

import com.jetug.chassis_core.Global;
import com.jetug.chassis_core.client.render.utils.GuiUtils;
import com.jetug.chassis_core.common.foundation.entity.WearableChassis;
import com.jetug.chassis_core.common.util.helpers.PlayerUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

import static com.jetug.chassis_core.common.data.constants.Gui.TAB_HEIGHT;
import static com.jetug.chassis_core.common.data.constants.Gui.TAB_WIDTH;
import static com.jetug.chassis_core.common.data.constants.Resources.PLAYER_INVENTORY_BOTTOM_TABS;
import static com.jetug.chassis_core.common.data.constants.Resources.PLAYER_INVENTORY_TABS;
import static net.minecraft.world.item.Items.CHEST;
import static net.minecraft.world.item.Items.CRAFTING_TABLE;

@SuppressWarnings({"DataFlowIssue", "ConstantConditions"})
public class ChassisScreen<T extends AbstractContainerMenu> extends GuiBase<T> {
    public static final int ENTITY_POS_X = 41;
    public static final int ENTITY_POS_Y = 73;
    public static final int TABS_WIDTH = 57;
    public static final float MIN_SCALE = 0.0001F;
    protected final ResourceLocation guiResource;

    protected float mousePosX;
    protected float mousePosY;
    protected int right;
    protected int bottom;

    public ChassisScreen(T container, Inventory inventory, Component name, ResourceLocation guiResource) {
        super(container, inventory, name);
        this.guiResource = guiResource;
    }

    @Override
    protected void init() {
        super.init();
        right = getRight();
        bottom = getBottom();
        var window = Minecraft.getInstance().getWindow().getWindow();
        if(Global.mouseX != null && Global.mouseY != null) {
            GLFW.glfwSetCursorPos(window, Global.mouseX, Global.mouseY);
            Global.mouseX = null;
            Global.mouseY = null;
        }
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY) {}

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int pButton) {
        var rect = new Rectangle(leftPos, topPos - TAB_HEIGHT, TAB_WIDTH, TAB_HEIGHT);

        if (minecraft.player.isCreative()) {
            rect = new Rectangle(right - 25, bottom, 25, TAB_HEIGHT);
        } else {
            rect = new Rectangle(leftPos, topPos - TAB_HEIGHT, TAB_WIDTH, TAB_HEIGHT);
        }
        if(rect.contains(mouseX, mouseY)){
            minecraft.setScreen(new InventoryScreen(minecraft.player));
        }

        return super.mouseClicked(mouseX, mouseY, pButton);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, guiResource);
        this.blit(poseStack, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        if (Global.referenceMob instanceof WearableChassis powerArmor) {
            renderEntity(powerArmor);
        }

        if(PlayerUtils.isLocalWearingChassis()) {
            renderTabs(poseStack);
            renderIcons(poseStack);
        }
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        this.mousePosX = mouseX;
        this.mousePosY = mouseY;

        this.renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(poseStack, mouseX, mouseY);
    }

    private int getRight(){
        return leftPos + imageWidth;
    }

    private int getBottom(){
        return topPos + imageHeight;
    }

    private void renderTabs(PoseStack poseStack) {
        if (minecraft.player.isCreative()) {
            RenderSystem.setShaderTexture(0, PLAYER_INVENTORY_BOTTOM_TABS);
            this.blit(poseStack, right - TABS_WIDTH, bottom - 4, 0, 32, TABS_WIDTH, 62);
        } else {
            RenderSystem.setShaderTexture(0, PLAYER_INVENTORY_TABS);
            this.blit(poseStack, leftPos, topPos - 28, 0, 32, TABS_WIDTH, 62);
        }
    }

    private void renderIcons(PoseStack poseStack) {
        if (minecraft.player.isCreative()) {
            this.itemRenderer.renderAndDecorateItem(CHEST.getDefaultInstance(), right - 6  - 16, bottom + 4);
            GuiUtils.drawChassisIcon(this, poseStack, right - 30 - 16, bottom + 4);
        } else {
            this.itemRenderer.renderAndDecorateItem(CRAFTING_TABLE.getDefaultInstance(), leftPos + 6, topPos - 20);
            GuiUtils.drawChassisIcon(this, poseStack, leftPos + 32, topPos - 20);
        }
    }

    protected void renderEntity(WearableChassis powerArmor) {
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