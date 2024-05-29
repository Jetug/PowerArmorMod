package com.jetug.chassis_core.common.util.helpers;

import com.google.gson.JsonParser;
import com.ibm.icu.impl.Pair;
import com.jetug.chassis_core.ChassisCore;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;

import static com.jetug.chassis_core.common.util.helpers.BufferedImageHelper.*;
import static com.jetug.chassis_core.common.util.helpers.texture.PlayerSkins.getSkin;
import static org.apache.logging.log4j.Level.ERROR;


public class TextureHelper {
    public static ResourceLocation createResource(String name, AbstractTexture abstractTexture) {
        return createResource(ChassisCore.MOD_ID, name, abstractTexture);
    }

    public static ResourceLocation createResource(String namespace, String name, AbstractTexture abstractTexture) {
        var textureManager = Minecraft.getInstance().getTextureManager();
        var headTextureLocation = new ResourceLocation(namespace, name);
        textureManager.register(headTextureLocation, abstractTexture);
        return headTextureLocation;
    }

    @Nullable
    public static AbstractTexture cropTexture(ResourceLocation resourceLocation, int x, int y, int width, int height) {
        var image = resourceToBufferedImage(resourceLocation);
        if (image == null) return null;
        cropZone(image, x, y, width, height);
        return new DynamicTexture(getNativeImage(image));
    }

    public static ResourceLocation createResource(BufferedImage image, String name) {
        return createResource(image, ChassisCore.MOD_ID, name);
    }

    public static ResourceLocation createResource(BufferedImage image, String namespace, String name) {
        var minecraft = Minecraft.getInstance();
        var nativeImage = getNativeImage(image);
        var texture = new DynamicTexture(nativeImage);
        var resourceLocation = new ResourceLocation(namespace, name);
        minecraft.getTextureManager().register(resourceLocation, texture);
        return resourceLocation;
    }
}
