package com.jetug.chassis_core.common.foundation.container.screen;

import com.jetug.chassis_core.common.foundation.container.menu.*;
import com.jetug.chassis_core.common.foundation.entity.WearableChassis;
import com.jetug.chassis_core.common.util.Pos2I;
import com.jetug.chassis_core.Global;
import com.jetug.chassis_core.common.data.enums.*;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

import static com.jetug.chassis_core.common.foundation.registery.ItemRegistry.*;
import static com.jetug.chassis_core.common.data.constants.Gui.*;
import static com.jetug.chassis_core.common.data.constants.Resources.*;
import static com.jetug.chassis_core.common.data.enums.BodyPart.*;
import static com.jetug.chassis_core.common.util.helpers.PlayerUtils.isWearingChassis;
import static net.minecraft.world.item.Items.*;

@SuppressWarnings({"DataFlowIssue", "ConstantConditions"})
public class SteamArmorGui extends ChassisGui{
    public SteamArmorGui(ChassisMenu container, Inventory inventory, Component name) {
        super(container, inventory, name);
    }
}