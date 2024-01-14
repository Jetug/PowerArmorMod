package com.jetug.chassis_core.client.render.utils;

import com.jetug.chassis_core.common.util.helpers.PlayerUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;

public class GuiUtils {
    public static void drawChassisIcon(GuiComponent gui, PoseStack poseStack, int x, int y){
        RenderSystem.setShaderTexture(0, PlayerUtils.getLocalPlayerChassis().getIcon());
        gui.blit(poseStack,x, y, 0,0, 16, 16, 16, 16);
    }
}
