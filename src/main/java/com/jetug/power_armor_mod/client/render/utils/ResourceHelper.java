package com.jetug.power_armor_mod.client.render.utils;

import com.jetug.power_armor_mod.common.foundation.entity.ArmorChassisBase;
import com.jetug.power_armor_mod.common.foundation.entity.ArmorChassisEntity;
import com.jetug.power_armor_mod.common.util.extensions.PlayerExtension;
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

    public static ResourceLocation getResourceLocation(ArmorChassisBase chassis, String path, String extension){
        var name = chassis.getRegistryName();
        var resourceLocation = chassis.getType().getRegistryName();

        return new ResourceLocation(resourceLocation.getNamespace(), path + name + extension);
    }

    public static ResourceLocation getResourceLocation(String path, String extension){
        var chassis = PlayerExtension.getPlayerChassis();
        return getResourceLocation(chassis, path, extension);
    }
}
