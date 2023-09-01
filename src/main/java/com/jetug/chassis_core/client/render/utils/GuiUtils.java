package com.jetug.chassis_core.client.render.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;

import static com.jetug.chassis_core.common.data.constants.Gui.TOP_TAB_ICON_POS_2;
import static com.jetug.chassis_core.common.util.helpers.PlayerUtils.getPlayerChassis;

public class GuiUtils {
    public static void drawChassisIcon(GuiComponent gui, PoseStack poseStack, int x, int y){
        RenderSystem.setShaderTexture(0, getPlayerChassis().getIcon());
        gui.blit(poseStack,x, y, 0,0, 16, 16, 16, 16);
    }
}
