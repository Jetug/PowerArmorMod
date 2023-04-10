package com.jetug.power_armor_mod.client.gui;

import com.jetug.power_armor_mod.common.foundation.screen.container.PowerArmorContainer;
import com.jetug.power_armor_mod.common.foundation.entity.PowerArmorEntity;
import com.jetug.power_armor_mod.common.util.Pos2D;
import com.jetug.power_armor_mod.common.data.constants.Global;
import com.jetug.power_armor_mod.common.data.enums.BodyPart;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.awt.*;

import static com.jetug.power_armor_mod.common.foundation.registery.ItemRegistry.*;
import static com.jetug.power_armor_mod.common.data.constants.Gui.*;
import static com.jetug.power_armor_mod.common.data.constants.Resources.*;
import static com.jetug.power_armor_mod.common.data.enums.BodyPart.*;
import static net.minecraft.world.item.Items.*;

@SuppressWarnings({"DataFlowIssue", "ConstantConditions"})
public class PowerArmorGui extends AbstractContainerScreen<PowerArmorContainer> {
    public static final int ENTITY_POS_X = 41;
    public static final int ENTITY_POS_Y = 73;
    public static final float MIN_SCALE = 0.0001F;
    public static final int TABS_WIDTH = 57;

    private float mousePosX;
    private float mousePosY;
    private int right;
    private int bottom;

    public PowerArmorGui(PowerArmorContainer container, Inventory inventory, Component name) {
        super(container, inventory, name);
    }

    @Override
    protected void init() {
        super.init();
        right = getRight();
        bottom = getBottom();

//        addWidget(new Button(leftPos, topPos, 50, 50, new TextComponent("TEST"), (c) -> {
//            minecraft.setScreen(new InventoryScreen(minecraft.player));
//        }));
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
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        this.mousePosX = mouseX;
        this.mousePosY = mouseY;


        if (minecraft.player.isCreative()) {
            this.itemRenderer.renderAndDecorateItem(CHEST.getDefaultInstance()         , right - 6  - 16, bottom + 4);
            this.itemRenderer.renderAndDecorateItem(PA_FRAME.get().getDefaultInstance(), right - 30 - 16, bottom + 4);
        } else {
            this.itemRenderer.renderAndDecorateItem(CRAFTING_TABLE.getDefaultInstance(), leftPos + 6, topPos + -20);
            this.itemRenderer.renderAndDecorateItem(PA_FRAME.get().getDefaultInstance(), leftPos + 35, topPos + -20);
        }

        this.renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTicks);

        //RenderSystem.setShaderTexture(0, ICONS_LOCATION);

        renderIcon(HEAD     , EMPTY_ARMOR_SLOT_HEAD     , poseStack, HEAD_SLOT_POS     );
        renderIcon(BODY     , EMPTY_ARMOR_SLOT_BODY     , poseStack, BODY_SLOT_POS     );
        renderIcon(LEFT_ARM , EMPTY_ARMOR_SLOT_LEFT_ARM , poseStack, RIGHT_ARM_SLOT_POS);
        renderIcon(RIGHT_ARM, EMPTY_ARMOR_SLOT_RIGHT_ARM, poseStack, LEFT_ARM_SLOT_POS );
        renderIcon(LEFT_LEG , EMPTY_ARMOR_SLOT_LEFT_LEG , poseStack, RIGHT_LEG_SLOT_POS);
        renderIcon(RIGHT_LEG, EMPTY_ARMOR_SLOT_RIGHT_LEG, poseStack, LEFT_LEG_SLOT_POS );

        this.renderTooltip(poseStack, mouseX, mouseY);
    }

    private void renderIcon(BodyPart bodyPart, PoseStack poseStack, Pos2D pos, Rectangle icon){
        //if(menu.slots == null || menu.slots.get(bodyPart.ordinal()).getEquipment().isEmpty())
            blit(poseStack, leftPos + pos.x, topPos + pos.y, icon.x, icon.y, icon.width, icon.height);
    }

    private void renderIcon(BodyPart bodyPart, ResourceLocation pTextureId, PoseStack poseStack, Pos2D pos){
        //if(menu.slots == null || menu.slots.get(bodyPart.ordinal()).getEquipment().isEmpty()) {
            RenderSystem.setShaderTexture(0, pTextureId);
            blit(poseStack, leftPos + pos.x, topPos + pos.y, 0, 0, 16, 16);
        //}
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

        if(minecraft.player.isCreative()){
            RenderSystem.setShaderTexture(0, PLAYER_INVENTORY_BOTTOM_TABS);
            this.blit(poseStack, right - TABS_WIDTH, bottom - 4, 0, 32, TABS_WIDTH, 62);
        }
        else{
            RenderSystem.setShaderTexture(0, PLAYER_INVENTORY_TABS);
            this.blit(poseStack, leftPos, topPos - 28, 0, 32, TABS_WIDTH, 62);
        }
    }

    private int getRight(){
        return leftPos + imageWidth;
    }

    private int getBottom(){
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