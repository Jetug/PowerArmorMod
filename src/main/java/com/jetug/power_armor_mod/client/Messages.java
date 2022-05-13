package com.jetug.power_armor_mod.client;

import com.jetug.power_armor_mod.PowerArmorMod;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class Messages {
    private static SimpleChannel INSTANCE;

    private static int pocketId = 0;
    private static int id(){return pocketId++;}

    public static void register(){
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(PowerArmorMod.MOD_ID, "messages"))
                .networkProtocolVersion(()->"1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();
        INSTANCE = net;
    }
}
