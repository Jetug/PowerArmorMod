package com.jetug.chassis_core.client.render.utils;

import com.jetug.chassis_core.common.util.helpers.PlayerUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;

public class GuiUtils {
    public static void drawChassisIcon(GuiGraphics gui, int x, int y) {
//        RenderSystem.setShaderTexture(0, PlayerUtils.getLocalPlayerChassis().getIcon());
        gui.blit(PlayerUtils.getLocalPlayerChassis().getIcon(), x, y, 0, 0, 16, 16, 16, 16);
    }
}
