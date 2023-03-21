package com.jetug.power_armor_mod.common.util.helpers;

import net.minecraft.resources.ResourceLocation;
import org.apache.commons.io.FilenameUtils;

public class ResourceHelper {
    public static String getResourceName(ResourceLocation path){
        return getFileName(path.getPath());
    }

    public static String getFileName(String path){
        return FilenameUtils.removeExtension(FilenameUtils.getName(path));
    }
}
