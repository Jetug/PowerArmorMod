package com.jetug.chassis_core.common.util.helpers;

import com.jetug.chassis_core.ChassisCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.awt.image.BufferedImage;

import static com.jetug.chassis_core.common.util.helpers.BufferedImageHelper.*;


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
