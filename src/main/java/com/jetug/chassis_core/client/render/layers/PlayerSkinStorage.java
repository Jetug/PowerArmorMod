package com.jetug.chassis_core.client.render.layers;

import com.ibm.icu.impl.Pair;
import com.jetug.chassis_core.common.util.helpers.BufferedImageHelper;
import com.mojang.datafixers.kinds.K1;
import com.mojang.datafixers.kinds.K2;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.UUID;

import static com.jetug.chassis_core.common.util.helpers.BufferedImageHelper.extendImage;
import static com.jetug.chassis_core.common.util.helpers.TextureHelper.*;

public class PlayerSkinStorage {
    public static final PlayerSkinStorage INSTANCE = new PlayerSkinStorage();
    public final HashMap<Pair<UUID, Pair<Integer, Integer>>, ResourceLocation> playerSkins = new HashMap<>();

    private PlayerSkinStorage(){}

    public void createSkin(Player player, Pair<Integer, Integer> size, BufferedImage image){
        var tag = player.getUUID();
        var path = "player_" + size.first + "_" + size.second + "_" + tag;
        playerSkins.put(Pair.of(tag, size), createResource(image, path));
    }

    public ResourceLocation getSkin(Player player, Pair<Integer, Integer> size){
        if (!playerSkins.containsKey(Pair.of(player.getUUID(), size))){
            var thread = new Thread(() -> {
                try {
                    var image = getPlayerSkinImage(player);
                    image = extendImage(image, size.first, size.second);
                    createSkin(player, size, image);
                }
                catch (Exception e) { createTemplate(player, size); }
            });
            thread.start();
            createTemplate(player, size);
        }
        return playerSkins.get(Pair.of(player.getUUID(), size));
    }

    private void createTemplate(Player player, Pair<Integer, Integer> size) {
        var image = getDefaultPlayerSkinImage(player);
        image = extendImage(image, size.first, size.second);
        var tag = player.getUUID();
        var path = "player_temp_" + size.first + "_" + size.second + "_" + tag;
        playerSkins.put(Pair.of(tag, size), createResource(image, path));
    }
}
