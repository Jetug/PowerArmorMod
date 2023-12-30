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

import static com.jetug.chassis_core.common.util.helpers.TextureHelper.createResource;

public class PlayerSkinStorage {
    public static final PlayerSkinStorage INSTANCE = new PlayerSkinStorage();
    public final HashMap<Pair<UUID, Pair<Integer, Integer>>, ResourceLocation> playerSkins = new HashMap<>();

    private PlayerSkinStorage(){}

    public void createSkin(Player player, Pair<Integer, Integer> size, BufferedImage image){
        var key= Pair.of(player.getUUID(), size);
        playerSkins.put(key, createResource(image, size.first + "x" + size.second + "_" + player.getUUID()));
    }

    public ResourceLocation getSkin(Player player, Pair<Integer, Integer> size){
        var skin = playerSkins.get(Pair.of(player.getUUID(), size));
        return skin;
//        if(skin == null)
    }
}
