package com.jetug.power_armor_mod.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;

import static com.jetug.power_armor_mod.common.util.constants.Resources.*;
import static com.jetug.power_armor_mod.common.util.extensions.PlayerExtension.*;

public class HeatRenderer implements IIngameOverlay
{
    @SuppressWarnings("ConstantConditions")
    @Override
    public void render(ForgeIngameGui gui, PoseStack poseStack, float partialTick, int width, int height) {
        RenderSystem.setShaderTexture(0, ICONS_LOCATION);
        RenderSystem.enableBlend();

        var x = 0;
        var y = 0;

        gui.blit(poseStack, x, y, 0, 0, 9, 52);

        if(isWearingPowerArmor()){
            RenderSystem.setShaderTexture(0, ICONS_LOCATION);
            var armor = getLocalPlayerArmor();
            int heat = armor.getHeatInPercent() / 2;

            var offset = 50 - heat + 1;

            gui.blit(poseStack, x + 1, y + offset, 10,  offset, 7, heat);
        }

    }

}
