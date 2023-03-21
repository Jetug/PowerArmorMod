package com.jetug.power_armor_mod.client.resources;

import com.google.gson.Gson;
import com.jetug.power_armor_mod.common.json.ArmorPartSettings;
import com.jetug.power_armor_mod.common.util.enums.BodyPart;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.apache.commons.io.FilenameUtils;

import javax.annotation.Nullable;
import java.io.*;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.jetug.power_armor_mod.common.util.helpers.ResourceHelper.getResourceName;

public class ModResourceManager {

    private static final String CONFIG_FOLDER = "configs";

    private final ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();

    private Collection<ResourceLocation> partConfigs;
    private final Map<String, ArmorPartSettings> partSettings = new HashMap<>();

    public void loadConfigs(){
        partConfigs = resourceManager.listResources(CONFIG_FOLDER, fileName -> fileName.endsWith(".json"));

        for (ResourceLocation config : partConfigs) {
            var settings = getPartSettings(config);
            if(settings != null){
                partSettings.put(settings.name, settings);
            }
        }
    }

    @Nullable
    public ArmorPartSettings getPartSettings(String itemId){
        return partSettings.get(itemId);
    }

    @Nullable
    private ArmorPartSettings getPartSettings(ResourceLocation resourceLocation){
        try {
            var readIn = getBufferedReader(resourceLocation);
            var settings = new Gson().fromJson(readIn, ArmorPartSettings.class);
            settings.name = getResourceName(resourceLocation);
            return settings;
        }
        catch (Exception e) {
            return null;
        }
    }

    private BufferedReader getBufferedReader(ResourceLocation resourceLocation) throws IOException {
        return getBufferedReader(resourceManager.getResource(resourceLocation).getInputStream());
    }

    private static BufferedReader getBufferedReader(InputStream stream){
        return new BufferedReader(new InputStreamReader(stream));
    }
}
