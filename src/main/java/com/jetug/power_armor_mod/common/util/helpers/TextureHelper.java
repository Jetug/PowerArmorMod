package com.jetug.power_armor_mod.common.util.helpers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jetug.power_armor_mod.common.util.constants.Global;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import oshi.util.tuples.Pair;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

import static com.jetug.power_armor_mod.common.util.helpers.BufferedImageHelper.*;
import static com.mojang.authlib.minecraft.MinecraftProfileTexture.*;


public class TextureHelper {

//    public static ResourceLocation getResourceLocation(AbstractClientPlayer clientPlayer) {
//        var minecraft = Minecraft.getInstance();
//        var playerInfo = minecraft.getConnection().getPlayerInfo(clientPlayer.getUUID());
//
//        GameProfile gameprofile = playerInfo.getProfile();
//
//        var map = minecraft.getSkinManager().getInsecureSkinInformation(gameprofile);
//
//        if (map.containsKey(Type.SKIN)) {
//            final var skin = map.get(Type.SKIN);
//            skin.getUrl();
//
////            String s = Hashing.sha1().hashUnencodedChars(skin.getHash()).toString();
////            ResourceLocation resourcelocation = new ResourceLocation("skins/" + s);
////
////            return resourcelocation;
//            return minecraft.getSkinManager().registerTexture(skin, Type.SKIN);
//        }
//        return null;
//
//    }

    public static ResourceLocation getResourceLocation(AbstractClientPlayer clientPlayer) {
        var minecraft = Minecraft.getInstance();

        var uuid = clientPlayer.getUUID();
        //uuid = UUID.fromString("494036be-71f6-4b58-bb8d-a483a18a322f");
//        var playerInfo = minecraft.getConnection().getPlayerInfo(uuid);
//        var gameProfile = playerInfo.getProfile();
//        var map = minecraft.getSkinManager().getInsecureSkinInformation(gameProfile);

        var skin = skinRequest(clientPlayer.getUUID());



//        Map<Type, MinecraftProfileTexture> taxtures = null;
//        try {
//            var field = Minecraft.class.getDeclaredField("minecraftSessionService");
//            field.setAccessible(true);
//            var sess = (MinecraftSessionService)field.get(minecraft);
//            taxtures = sess.getTextures(gameProfile, false);
//
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }

//        if (map.containsKey(Type.SKIN)) {
//            final var skin = map.get(Type.SKIN);
//            try {
//
//                var url = new URL(skin.getUrl());
//                var image = ImageIO.read(url);
//                return createResource(image, clientPlayer.getUUID().toString());
//
//            } catch (Exception e) {
//                //return clientPlayer.getSkinTextureLocation();
//                throw new RuntimeException(e);
//            }
//        }
        return null;
    }


    private static BufferedImage skinRequest(UUID uuid) {
        var strUuid = uuid.toString();
        strUuid = strUuid.replace("-", "");

        var url = "https://sessionserver.mojang.com/session/minecraft/profile/" + strUuid;
        //url =     "https://sessionserver.mojang.com/session/minecraft/profile/494036be71f64b58bb8da483a18a322f";

        try {
            var response = getHTML(url);
            var gson = new Gson();
            var user = gson.fromJson(response, User.class);
            var encoded = user.properties[0].value;
            var skinData = decodeBase64(encoded);
            var itemsObject = JsonParser.parseString(skinData).getAsJsonObject();
            var jsonUrl = itemsObject.get("textures").getAsJsonObject().get("SKIN").getAsJsonObject().get("url").getAsString();
            var image = ImageIO.read(new URL(jsonUrl));

            return image;
//            File file = new File("C:\\Users\\Jetug\\Desktop\\result\\test_image.png");
//            ImageIO.write(image, "png", file);
        } catch (IOException e) {
            return null;
        }
    }

    private static String decodeBase64(String base64) {
        byte[] decoded = Base64.getDecoder().decode(base64);
        return new  String(decoded, StandardCharsets.UTF_8);
    }

    public static String getHTML(String urlToRead) {
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
    public static AbstractTexture getHeadLayerTexture(AbstractClientPlayer clientPlayer, int width, int height) {
        var playerHead = getPlayerHeadImage(clientPlayer);
        if (playerHead == null) return null;
        playerHead = extendImage(playerHead, width, height);
        return new DynamicTexture(getNativeImage(playerHead));
    }
}
