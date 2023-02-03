package com.jetug.power_armor_mod.client.resources;

import com.google.gson.Gson;
import com.jetug.power_armor_mod.client.render.ArmorPartSettings;
import com.jetug.power_armor_mod.common.util.constants.Global;
import com.jetug.power_armor_mod.common.util.enums.BodyPart;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.jetug.power_armor_mod.client.render.ResourceHelper.getBufferedReader;
import static com.jetug.power_armor_mod.common.util.enums.BodyPart.BODY;

public class ModResourceManager {

    private static final String CONFIG_FOLDER = "configs";

    private final ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();

    private Collection<ResourceLocation> partConfigs;
    private final Map<BodyPart, ArmorPartSettings> partSettings = new HashMap<>();

    public void loadConfigs(){
        partConfigs = resourceManager.listResources(CONFIG_FOLDER, fileName -> fileName.endsWith(".json"));

        for (ResourceLocation config : partConfigs) {
            var settings = getPartSettings(config);
            if(settings != null){
                partSettings.put(settings.part, settings);
            }
        }
    }

    public ArmorPartSettings getPartSettings(BodyPart bodyPart){
        if(partSettings.containsKey(bodyPart))
            return partSettings.get(bodyPart);
        return null;
    }

    @Nullable
    private ArmorPartSettings getPartSettings(ResourceLocation resourceLocation){
        try {
            var readIn = getBufferedReader(resourceManager.getResource(resourceLocation).getInputStream());
            var gson = new Gson();
            return gson.fromJson(readIn, ArmorPartSettings.class);
        } catch (Exception e) {
            return null;
        }
    }
}
