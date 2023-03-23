package com.jetug.power_armor_mod.client.resources;

import com.google.gson.*;
import com.jetug.power_armor_mod.common.json.*;
import net.minecraft.client.*;
import net.minecraft.resources.*;
import javax.annotation.*;
import java.io.*;
import java.util.*;

import static com.jetug.power_armor_mod.common.util.helpers.ResourceHelper.*;

public class ModResourceManager {
    private static final String CONFIG_DIR = "configs";
    private static final String EQUIPMENT_DIR = CONFIG_DIR + "/equipment";
    private static final String FRAME_DIR = CONFIG_DIR + "/frame";

    private final Map<String, EquipmentSettings> equipmentSettings = new HashMap<>();
    private final Map<String, FrameSettings> frameSettings = new HashMap<>();


    @Nullable
    public EquipmentSettings getEquipmentSettings(String itemId){
        return equipmentSettings.get(itemId);
    }

    @Nullable
    public FrameSettings getFrameSettings(String frameId){
        return frameSettings.get(frameId);
    }

    public void loadConfigs(){
        loadEquipment();
        loadFrame();
    }

    private void loadEquipment() {
        for (ResourceLocation config : getJsonResources(EQUIPMENT_DIR)) {
            var settings = getSettings(config, EquipmentSettings.class);
            if(settings != null)
                equipmentSettings.put(settings.name, settings);
        }
    }

    private void loadFrame() {
        for (ResourceLocation config : getJsonResources(FRAME_DIR)) {
            var settings = getSettings(config, FrameSettings.class);
            if(settings != null)
                frameSettings.put(settings.name, settings);
        }
    }


    private static Collection<ResourceLocation> getJsonResources(String path){
        return Minecraft.getInstance().getResourceManager()
                .listResources(path, fileName -> fileName.endsWith(".json"));
    }

    @Nullable
    private EquipmentSettings getEquipmentSettings(ResourceLocation resourceLocation){
        try {
            var readIn = getBufferedReader(resourceLocation);
            var settings = new Gson().fromJson(readIn, EquipmentSettings.class);
            settings.name = getResourceName(resourceLocation);
            return settings;
        }
        catch (Exception e) {
            return null;
        }
    }

    @Nullable
    private FrameSettings getFrameSettings(ResourceLocation resourceLocation){
        try {
            var readIn = getBufferedReader(resourceLocation);
            var settings = new Gson().fromJson(readIn, FrameSettings.class);
            settings.name = getResourceName(resourceLocation);
            return settings;
        }
        catch (Exception e) {
            return null;
        }
    }

    @Nullable
    private <T extends ModelSettingsBase> T getSettings(ResourceLocation resourceLocation, Class<T> classOfT){
        try {
            var readIn = getBufferedReader(resourceLocation);
            var settings = new Gson().fromJson(readIn, classOfT);
            settings.name = getResourceName(resourceLocation);
            return settings;
        }
        catch (Exception e) {
            return null;
        }
    }
    private BufferedReader getBufferedReader(ResourceLocation resourceLocation) throws IOException {
        return getBufferedReader(Minecraft.getInstance().getResourceManager().getResource(resourceLocation).getInputStream());
    }

    private static BufferedReader getBufferedReader(InputStream stream){
        return new BufferedReader(new InputStreamReader(stream));
    }
}
