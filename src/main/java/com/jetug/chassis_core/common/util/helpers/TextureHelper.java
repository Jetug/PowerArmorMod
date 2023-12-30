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
    public static final String MINECRAFT_PROFILE_URL = "https://sessionserver.mojang.com/session/minecraft/profile/";

    public static ResourceLocation createResource(String name, AbstractTexture abstractTexture){
        var textureManager = Minecraft.getInstance().getTextureManager();
        var headTextureLocation = new ResourceLocation(ChassisCore.MOD_ID, name);
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

    @Nullable
    public static BufferedImage getPlayerImage(Player clientPlayer) {
        try {
            return getPlayerSkinImage(clientPlayer);
        }
        catch (Exception e){
            var skin = DefaultPlayerSkin.getDefaultSkin(clientPlayer.getGameProfile().getId());
            return resourceToBufferedImage(skin);
        }
    }


    @Nullable
    public static BufferedImage getPlayerHeadImage(Player clientPlayer) {
        try {
            var playerSkin = getPlayerSkinImage(clientPlayer);
            if (playerSkin == null) return null;
            cropImage(playerSkin, 64, 16);
            return playerSkin;
        }
        catch (Exception e){
            var skin = DefaultPlayerSkin.getDefaultSkin(clientPlayer.getGameProfile().getId());
            var playerSkin = resourceToBufferedImage(skin);
            if (playerSkin == null) return null;

            cropImage(playerSkin, 64, 16);
            return playerSkin;
        }
    }

    @Nullable
    public static AbstractTexture getDefaultResizedHeadTexture(Player clientPlayer, int width, int height) {
        var playerHead = getDefaultPlayerHeadImage(clientPlayer);
        if (playerHead == null) return null;
        playerHead = extendImage(playerHead, width, height);
        return new DynamicTexture(getNativeImage(playerHead));
    }

    @Nullable
    public static BufferedImage getDefaultPlayerHeadImage(Player clientPlayer) {
        var skin = getSkin(clientPlayer);
        var playerSkin = resourceToBufferedImage(skin);
        if (playerSkin == null) return null;

        cropImage(playerSkin, 64, 16);
        return playerSkin;
    }

    private static void print(BufferedImage image){
        var outputFile = new File("C:/Users/Jetug/Desktop/test/output.png"); // Путь к файлу и его расширение
        try {
            ImageIO.write(image, "png", outputFile);
        } catch (IOException e) {

        }
    }

    public static ResourceLocation getPlayerSkinLocation(AbstractClientPlayer player) {
//        var minecraft = Minecraft.getInstance();
//        //var gameProfile = clientPlayer.getGameProfile();
//
//        clientPlayer.getSkinTextureLocation();
//
//        var gameProfile = new GameProfile(UUID.fromString("494036be-71f6-4b58-bb8d-a483a18a322f"), null);
//
//        var map = minecraft.getSkinManager().getInsecureSkinInformation(gameProfile);
//
//        var skin = map.containsKey(Type.SKIN) ?
//                minecraft.getSkinManager().registerTexture(map.get(Type.SKIN), Type.SKIN) :
//                DefaultPlayerSkin.getDefaultSkin(clientPlayer.getUUID());
;;

        var gameProfile = player.getGameProfile();
        var propertyMap = gameProfile.getProperties();
        if(!propertyMap.containsKey("textures"))
            return player.getSkinTextureLocation();
        var property = propertyMap.get("textures").iterator().next();

        var textureValue = property.getValue();
        ResourceLocation skinLocation = new ResourceLocation("textures/" + textureValue);

        return skinLocation;
        //return player.getSkinTextureLocation();
    }


//    @Nullable
//    public static BufferedImage getPlayerHeadwearImage(AbstractClientPlayer clientPlayer) {
//        var playerSkin = getPlayerSkinImage(clientPlayer);
//        if(playerSkin == null) return null;
//        cropImage(playerSkin, 32, 16);
//        return playerSkin;
//    }

    @Nullable
    public static BufferedImage getPlayerSkinImage(Player clientPlayer) {
        var skin = skinRequest(clientPlayer.getUUID());
        return Objects.requireNonNullElse(skin, resourceToBufferedImage(getSkin(clientPlayer) /*clientPlayer.getSkinTextureLocation()*/));
    }

    @Nullable
    public static BufferedImage skinRequest(UUID uuid) {
        var url = MINECRAFT_PROFILE_URL + uuid.toString().replace("-", "");
        try {
            var response = getHTML(url);
            var json = JsonParser.parseString(response).getAsJsonObject();
            var base64 = json.get("properties").getAsJsonArray().get(0).getAsJsonObject().get("value").getAsString();
            var skinData = decodeBase64(base64);
            json = JsonParser.parseString(skinData).getAsJsonObject();
            var jsonUrl = json.get("textures").getAsJsonObject().get("SKIN").getAsJsonObject().get("url").getAsString();

            return ImageIO.read(new URL(jsonUrl));
        }
        catch (Exception e){
            if(!(e instanceof IOException || e instanceof IllegalStateException))
                ChassisCore.LOGGER.log(ERROR, e.getMessage(), e);
            return null;
        }
    }

    public static ResourceLocation createResource(BufferedImage image, String name) {
        var minecraft = Minecraft.getInstance();
        var nativeImage = getNativeImage(image);
        var texture = new DynamicTexture(nativeImage);
        var resourceLocation = new ResourceLocation(ChassisCore.MOD_ID, name);
        minecraft.getTextureManager().register(resourceLocation, texture);
        return resourceLocation;
    }

    public static Pair<Integer, Integer> getTextureSize(ResourceLocation resourceLocation) {
        try {
            var resource = Minecraft.getInstance().getResourceManager().getResource(resourceLocation);
            var nativeImage = NativeImage.read(resource.getInputStream());
            return Pair.of(nativeImage.getWidth(), nativeImage.getHeight());

        } catch (IOException e) {
            return Pair.of(0, 0);
        }
    }

    @Nullable
    public static AbstractTexture getResizedHeadTexture(Player clientPlayer, int width, int height) {
        var playerHead = getPlayerHeadImage(clientPlayer);
        if (playerHead == null) return null;
        playerHead = extendImage(playerHead, width, height);
        return new DynamicTexture(getNativeImage(playerHead));
    }

    private static String decodeBase64(String base64) {
        byte[] decoded = Base64.getDecoder().decode(base64);
        return new  String(decoded, StandardCharsets.UTF_8);
    }

    private static String getHTML(String urlToRead) {
        var result = new StringBuilder();

        try {
            var url = new URL(urlToRead);
            var conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            var res = conn.getResponseMessage();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                for (String line; (line = reader.readLine()) != null; ) {
                    result.append(line);
                }
                return result.toString();
            }
        }
        catch (Exception e){
            return "";
        }
    }
}
