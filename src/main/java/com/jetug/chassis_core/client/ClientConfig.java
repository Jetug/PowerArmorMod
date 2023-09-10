package com.jetug.chassis_core.client;

import com.jetug.chassis_core.client.resources.ModResourceManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.player.LocalPlayer;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.file.GeoModelLoader;

public class ClientConfig {
    public static final ModResourceManager modResourceManager = new ModResourceManager();
    public static final GeoModelLoader modelLoader = new GeoModelLoader();

    @NotNull public static final Options OPTIONS = Minecraft.getInstance().options;

    public static LocalPlayer getLocalPlayer(){
        return Minecraft.getInstance().player;
    }
}
