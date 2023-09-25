package com.jetug.chassis_core.client;

import com.jetug.chassis_core.client.resources.ModResourceManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.player.LocalPlayer;
import org.jetbrains.annotations.NotNull;

public class ClientConfig {
    public static final ModResourceManager modResourceManager = new ModResourceManager();

    @NotNull public static final Options OPTIONS = Minecraft.getInstance().options;
}
