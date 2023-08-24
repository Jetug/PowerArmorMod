package com.jetug.power_armor_mod.common.util.helpers;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.apache.commons.io.FilenameUtils;

import java.io.IOException;

import static org.apache.commons.io.FilenameUtils.*;

public class ResourceHelper {
    public static String getResourceName(ResourceLocation resourceLocation){
        var path = resourceLocation.getPath();
        return removeExtension(getName(path));
    }

    public static boolean hasResource(ResourceLocation path) {
        return Minecraft
                .getInstance()
                .getResourceManager()
                .hasResource(path);
    }
}
