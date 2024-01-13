package com.jetug.chassis_core.client;

import com.jetug.chassis_core.client.render.renderers.ChassisHeadRenderer;
import com.jetug.chassis_core.client.resources.ModResourceManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class ClientConfig {
    public static final ModResourceManager modResourceManager = new ModResourceManager();
    public static final ChassisHeadRenderer CHASSIS_HEAD_RENDERER = new ChassisHeadRenderer();

    @NotNull public static final Options OPTIONS = Minecraft.getInstance().options;
}
