/*
 * Copyright (c) 2019-2022 Team Galacticraft
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.jetug.chassis_core.mixin;

import java.awt.*;

import com.jetug.chassis_core.Global;
import com.jetug.chassis_core.client.render.utils.GuiUtils;
import com.jetug.chassis_core.common.data.enums.ActionType;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.*;
import net.minecraft.client.gui.screens.recipebook.*;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraftforge.api.distmarker.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import static com.jetug.chassis_core.common.foundation.registery.ItemRegistry.*;
import static com.jetug.chassis_core.common.network.PacketSender.*;
import static com.jetug.chassis_core.common.data.constants.Gui.*;
import static com.jetug.chassis_core.common.data.constants.Resources.*;
import static com.jetug.chassis_core.common.util.helpers.PlayerUtils.*;
import static net.minecraft.world.item.Items.*;

@Mixin(InventoryScreen.class)
@OnlyIn(Dist.CLIENT)
public abstract class InventoryScreenMixin extends EffectRenderingInventoryScreen<InventoryMenu> {
    @Shadow public abstract boolean mouseClicked(double pMouseX, double pMouseY, int pButton);

    public InventoryScreenMixin(InventoryMenu screenHandler, Inventory playerInventory, Component textComponent) {
        super(screenHandler, playerInventory, textComponent);
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"))
    public void mouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> ci) {
        if(!isWearingChassis()) return;

        var rect = new Rectangle(leftPos + 30, topPos - TAB_HEIGHT, TAB_WIDTH, TAB_HEIGHT);
        if(rect.contains(mouseX, mouseY)){
            Global.saveMousePos();
            doServerAction(ActionType.OPEN_GUI);
        }
    }

    @Inject(method = "renderBg", at = @At("TAIL"))
    public void drawBackground(PoseStack matrices, float v, int i, int i1, CallbackInfo callbackInfo) {
        if(!isWearingChassis()) return;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, PLAYER_INVENTORY_TABS);
        blit(matrices, this.leftPos, this.topPos - 28, 0, 0, 57, 32);
    }

    @Inject(method = "render", at = @At("TAIL"))
    public void render(PoseStack poseStack, int mouseX, int mouseY, float v, CallbackInfo callbackInfo) {
        if(!isWearingChassis()) return;

        Lighting.setupFor3DItems();
        itemRenderer.renderAndDecorateItem(CRAFTING_TABLE.getDefaultInstance(), leftPos + TOP_TAB_ICON_POS_1.x,
                topPos + TOP_TAB_ICON_POS_1.y);
        Lighting.setupForFlatItems();
        GuiUtils.drawChassisIcon(this,poseStack, leftPos + TOP_TAB_ICON_POS_2.x, topPos + TOP_TAB_ICON_POS_2.y);
    }
}
