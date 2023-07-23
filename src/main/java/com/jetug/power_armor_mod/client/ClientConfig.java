package com.jetug.power_armor_mod.client;

import com.jetug.power_armor_mod.client.resources.ModResourceManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.player.LocalPlayer;
import org.jetbrains.annotations.NotNull;

public class ClientConfig {
    public static final ModResourceManager modResourceManager = new ModResourceManager();

    @NotNull public static final Options OPTIONS = Minecraft.getInstance().options;

    public static LocalPlayer getLocalPlayer(){
        return Minecraft.getInstance().player;
    }
}
