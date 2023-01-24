package com.jetug.power_armor_mod.common.util.helpers;

import com.google.gson.JsonParser;
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
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

import static com.jetug.power_armor_mod.common.util.helpers.BufferedImageHelper.*;


public class TextureHelper {

//    public static ResourceLocation getMojangSkin(AbstractClientPlayer clientPlayer) {
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


    public static BufferedImage skinRequest(UUID uuid) {
        var strUuid = uuid.toString();
        strUuid = strUuid.replace("-", "");

        var url = "https://sessionserver.mojang.com/session/minecraft/profile/" + strUuid;
        url =     "https://sessionserver.mojang.com/session/minecraft/profile/494036be71f64b58bb8da483a18a322f";

        try {
            var response = getHTML(url);
//            var gson = new Gson();
//            var user = gson.fromJson(response, User.class);

            var json = JsonParser.parseString(response).getAsJsonObject();
            var base64 = json.get("properties").getAsJsonArray().get(0).getAsJsonObject().get("value").getAsString();//user.properties[0].value;
            var skinData = decodeBase64(base64);
            json = JsonParser.parseString(skinData).getAsJsonObject();
            var jsonUrl = json.get("textures").getAsJsonObject().get("SKIN").getAsJsonObject().get("url").getAsString();
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
