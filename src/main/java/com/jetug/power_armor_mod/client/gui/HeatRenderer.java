package com.jetug.power_armor_mod.client.gui;

import com.jetug.power_armor_mod.common.util.extensions.PlayerExtension;
import com.jetug.power_armor_mod.common.util.helpers.MathHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;

import static com.jetug.power_armor_mod.common.util.constants.Resources.*;
import static com.jetug.power_armor_mod.common.util.extensions.PlayerExtension.*;

public class HeatRenderer implements IIngameOverlay
{

    public static final int BAR_HEIGHT = 52;

    @SuppressWarnings("ConstantConditions")
    @Override
    public void render(ForgeIngameGui gui, PoseStack poseStack, float partialTick, int width, int height) {
        if (!isWearingPowerArmor()) return;

        RenderSystem.setShaderTexture(0, ICONS_LOCATION);
        RenderSystem.enableBlend();

        var x = width - (int)MathHelper.getPercentOf(width, 25);
        var y = height - BAR_HEIGHT;

        gui.blit(poseStack, x, y, 0, 0, 9, BAR_HEIGHT);

        RenderSystem.setShaderTexture(0, ICONS_LOCATION);
        var armor = PlayerExtension.getPlayerArmor();
        int heat = armor.getHeatInPercent() / 2;

        var offset = 50 - heat + 1;

        gui.blit(poseStack, x + 1, y + offset, 10,  offset, 7, heat);

    }

}
