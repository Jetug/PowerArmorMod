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

package com.jetug.chassis_core.mixin.client;

import com.jetug.chassis_core.Global;
import com.jetug.chassis_core.client.render.utils.GuiUtils;
import com.jetug.chassis_core.common.data.enums.ActionType;
import com.jetug.chassis_core.common.util.helpers.PlayerUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;

import static com.jetug.chassis_core.client.gui.screen.ChassisScreen.TABS_WIDTH;
import static com.jetug.chassis_core.common.data.constants.Gui.TAB_HEIGHT;
import static com.jetug.chassis_core.common.data.constants.Resources.PLAYER_INVENTORY_BOTTOM_TABS;
import static com.jetug.chassis_core.common.network.PacketSender.doServerAction;

/**
 * @author <a href="https://github.com/TeamGalacticraft">TeamGalacticraft</a>
 */
@SuppressWarnings("DataFlowIssue")
@Mixin(CreativeModeInventoryScreen.class)
@OnlyIn(Dist.CLIENT)
public abstract class CreativeInventoryScreenMixin extends EffectRenderingInventoryScreen<InventoryMenu> {
    public CreativeInventoryScreenMixin(InventoryMenu screenHandler, Inventory playerInventory, Component textComponent) {
        super(screenHandler, playerInventory, textComponent);
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"))
    public void mouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> ci) {
        if(!PlayerUtils.isWearingChassis()) return;
        var rect = new Rectangle(getRight() - 51, getBottom(), 25, TAB_HEIGHT);
        if(rect.contains(mouseX, mouseY)){
            Global.saveMousePos();
            doServerAction(ActionType.OPEN_GUI);
        }
    }

    @Inject(method = "renderBg", at = @At("TAIL"))
    public void drawBackground(PoseStack poseStack, float partialTicks, int mouseX, int mouseY, CallbackInfo callbackInfo) {
        if(!PlayerUtils.isWearingChassis()) return;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        RenderSystem.setShaderTexture(0, PLAYER_INVENTORY_BOTTOM_TABS);
        this.blit(poseStack, getRight() - TABS_WIDTH, getBottom() - 4, 0, 0, TABS_WIDTH, 32);

        GuiUtils.drawChassisIcon(this,poseStack, getRight() - 30 - 16, getBottom() + 4);
//
//        RenderSystem.setShaderTexture(0, PLAYER_INVENTORY_TABS);
//        this.blit(poseStack, this.leftPos, this.topPos - 28, 0, 0, 57, 32);
    }

//    @Inject(method = "render", at = @At("TAIL"))
//    public void render(PoseStack poseStack, int mouseX, int mouseY, float v, CallbackInfo callbackInfo) {
//        if(!PlayerUtils.isWearingChassis()) return;
////        Lighting.setupFor3DItems();
////        Lighting.setupForFlatItems();
//        GuiUtils.drawChassisIcon(this,poseStack, getRight() - 30 - 16, getBottom() + 4);
//
//    }

    @Unique
    private int getRight(){
        return leftPos + imageWidth;
    }

    @Unique
    private int getBottom(){
        return topPos + imageHeight;
    }
}
