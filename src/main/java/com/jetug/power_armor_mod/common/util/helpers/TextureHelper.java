package com.jetug.power_armor_mod.common.util.helpers;

import com.jetug.power_armor_mod.common.minecraft.entity.PowerArmorEntity;
import com.jetug.power_armor_mod.common.util.constants.Global;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import oshi.util.tuples.Pair;

import javax.annotation.Nullable;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

import static com.jetug.power_armor_mod.common.util.helpers.BufferedImageHelper.*;


public class TextureHelper {

    public static Pair<Integer, Integer> getTextureSize(ResourceLocation resourceLocation) {
        Resource resource = null;
        try {
            resource = Minecraft.getInstance().getResourceManager().getResource(resourceLocation);
            var nativeImage = NativeImage.read(resource.getInputStream());
            return new Pair<>(nativeImage.getWidth(), nativeImage.getHeight());

        } catch (IOException e) {
            return new Pair<Integer, Integer>(0,0);
        }
    }

    @Nullable
    public static AbstractTexture getHeadLayerTexture(AbstractClientPlayer clientPlayer, int width, int height) {
        var playerHead = getPlayerHeadImage(clientPlayer);
        if (playerHead == null) return null;
        playerHead = extendImage(playerHead, width, height);
        return new DynamicTexture(getNativeImage(playerHead));
    }
}
