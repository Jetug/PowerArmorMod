package com.jetug.power_armor_mod.common.util.helpers;

import com.google.gson.JsonParser;
import com.jetug.power_armor_mod.common.data.constants.Global;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.entity.player.Player;
import oshi.util.tuples.Pair;


import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;

import static com.jetug.power_armor_mod.common.util.helpers.BufferedImageHelper.*;
import static com.jetug.power_armor_mod.common.util.helpers.texture.PlayerSkins.getSkin;
import static com.mojang.authlib.minecraft.MinecraftProfileTexture.*;
import static org.apache.logging.log4j.Level.*;


public class TextureHelper {
    public static final String MINECRAFT_PROFILE_URL = "https://sessionserver.mojang.com/session/minecraft/profile/";

    @Nullable
    public static BufferedImage getPlayerHeadImage(Player clientPlayer) {
        try {
            var playerSkin = getPlayerSkinImage(clientPlayer);
            //var playerSkin = resourceToBufferedImage(getPlayerSkinLocation(clientPlayer));
            //print(playerSkin);

            if (playerSkin == null) return null;
            cropImage(playerSkin, 64, 16);
            return playerSkin;
        }
        catch (Exception e){
            var skin = getSkin(clientPlayer);
            var playerSkin = resourceToBufferedImage(skin);
            if (playerSkin == null) return null;

            cropImage(playerSkin, 64, 16);
            return playerSkin;
        }
    }

    private static void print(BufferedImage image){

        File outputFile = new File("C:/Users/Jetug/Desktop/test/output.png"); // Путь к файлу и его расширение
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
    private static BufferedImage getPlayerSkinImage(Player clientPlayer) {
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
                Global.LOGGER.log(ERROR, e.getMessage(), e);
            return null;
        }
    }

    public static ResourceLocation createResource(BufferedImage image, String name) {
        var minecraft = Minecraft.getInstance();
        var nativeImage = getNativeImage(image);
        var texture = new DynamicTexture(nativeImage);
        var resourceLocation = new ResourceLocation(Global.MOD_ID, name);
        minecraft.getTextureManager().register(resourceLocation, texture);
        return resourceLocation;
    }

    public static Pair<Integer, Integer> getTextureSize(ResourceLocation resourceLocation) {
        Resource resource = null;
        try {
            resource = Minecraft.getInstance().getResourceManager().getResource(resourceLocation);
            var nativeImage = NativeImage.read(resource.getInputStream());
            return new Pair<>(nativeImage.getWidth(), nativeImage.getHeight());

        } catch (IOException e) {
            return new Pair<>(0, 0);
        }
    }

    @Nullable
    public static AbstractTexture getHeadLayerTexture(Player clientPlayer, int width, int height) {
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
