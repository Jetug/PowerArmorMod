package com.jetug.chassis_core.common.util.helpers;

import com.jetug.chassis_core.ChassisCore;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.Level;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class BufferedImageHelper {
    @Nullable
    public static BufferedImage resourceToBufferedImage(ResourceLocation resourceLocation) {
        try {
            var resource = Minecraft.getInstance().getResourceManager().getResource(resourceLocation);
            var nativeImage = NativeImage.read(resource.getInputStream());
            var imageArr = nativeImage.asByteArray();
            return getImage(imageArr);
        }
        catch (IOException e) {
            ChassisCore.LOGGER.log(Level.ERROR, e);
            return null;
        }
    }

    public static NativeImage getNativeImage(BufferedImage img) {
        NativeImage nativeImage = new NativeImage(img.getWidth(), img.getHeight(), true);
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                int clr = img.getRGB(x, y);
                int alpha = (clr & 0xff000000) >> 24;
                int red =   (clr & 0x00ff0000) >> 16;
                int green = (clr & 0x0000ff00) >> 8;
                int blue =   clr & 0x000000ff;

                int rgb = alpha;
                rgb = (rgb << 8) + blue;
                rgb = (rgb << 8) + green;
                rgb = (rgb << 8) + red;

                nativeImage.setPixelRGBA(x, y, rgb);
            }
        }
        return nativeImage;
    }

    public static BufferedImage extendImage(BufferedImage image, int width, int height){
        var scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        addImage(scaledImage, image, 0, 0);
        return scaledImage;
    }

    public static void cropZone(BufferedImage image, int x, int y, int width, int height) {
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                if (i < x || i >= x + width || j < y || j >= y + height) {
                    int rgba = image.getRGB(i, j) & 0x00FFFFFF;
                    image.setRGB(i, j, rgba);
                }
            }
        }
    }

    public static void cropImage(BufferedImage img, int xPos, int yPos) {
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                if(x >= xPos || y >= yPos)
                    img.setRGB(x, y, (new Color(0.0f, 0.0f, 0.0f, 0.0f)).getRGB());
            }
        }
    }

    private static void addImage(BufferedImage buff1, BufferedImage buff2, int x, int y) {
        Graphics2D g2d = buff1.createGraphics();
        //g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
        g2d.drawImage(buff2, x, y, null);
        g2d.dispose();
    }

    public static BufferedImage getImage(byte[] imageData) {
        var bais = new ByteArrayInputStream(imageData);
        try {
            return ImageIO.read(bais);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
