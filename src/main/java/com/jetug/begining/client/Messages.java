package com.jetug.begining.client;

import com.jetug.begining.ExampleMod;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import sun.nio.ch.Net;

public class Messages {
    private static SimpleChannel INSTANCE;

    private static int pocketId = 0;
    private static int id(){return pocketId++;}

    public static void register(){
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(ExampleMod.MOD_ID, "messages"))
                .networkProtocolVersion(()->"1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();
        INSTANCE = net;
    }
}
