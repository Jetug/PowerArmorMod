package com.jetug.chassis_core.client.resources;

import com.google.gson.*;
import com.jetug.chassis_core.common.data.json.EquipmentConfig;
import com.jetug.chassis_core.common.data.json.FrameConfig;
import com.jetug.chassis_core.common.data.json.ModelConfigBase;
import net.minecraft.client.*;
import net.minecraft.resources.*;
import javax.annotation.*;
import java.io.*;
import java.util.*;

import static com.jetug.chassis_core.client.render.utils.ResourceHelper.getResourceName;

public class ModResourceManager {
    private static final String CONFIG_DIR = "config/model/";
    private static final String EQUIPMENT_DIR = CONFIG_DIR + "equipment";
    private static final String FRAME_DIR = CONFIG_DIR + "chassis";

    private final Map<String, EquipmentConfig> equipmentSettings = new HashMap<>();
    private final Map<String, FrameConfig> frameSettings = new HashMap<>();

    @Nullable
    public EquipmentConfig getEquipmentSettings(String itemId){
        return equipmentSettings.get(itemId);
    }

    @Nullable
    public FrameConfig getFrameSettings(String frameId){
        return frameSettings.get(frameId);
    }

    public void loadConfigs(){
        loadEquipment();
        loadFrame();
        //loadAnimations();
    }

    private void loadEquipment() {
        for (ResourceLocation config : getJsonResources(EQUIPMENT_DIR)) {
            var settings = getSettings(config, EquipmentConfig.class);
            if(settings != null)
                equipmentSettings.put(settings.name, settings);
        }
    }

    private void loadFrame() {
        for (ResourceLocation config : getJsonResources(FRAME_DIR)) {
            var settings = getSettings(config, FrameConfig.class);
            if(settings != null)
                frameSettings.put(settings.name, settings);
        }
    }

    private static Collection<ResourceLocation> getJsonResources(String path){
        return Minecraft.getInstance().getResourceManager()
                .listResources(path, fileName -> fileName.endsWith(".json"));
    }

    @Nullable
    private EquipmentConfig getEquipmentSettings(ResourceLocation resourceLocation){
        try {
            var readIn = getBufferedReader(resourceLocation);
            var settings = new Gson().fromJson(readIn, EquipmentConfig.class);
            settings.name = getResourceName(resourceLocation);
            return settings;
        }
        catch (Exception e) {
            return null;
        }
    }

    @Nullable
    private FrameConfig getFrameSettings(ResourceLocation resourceLocation){
        try {
            var readIn = getBufferedReader(resourceLocation);
            var settings = new Gson().fromJson(readIn, FrameConfig.class);
            settings.name = getResourceName(resourceLocation);
            return settings;
        }
        catch (Exception e) {
            return null;
        }
    }

    @Nullable
    private <T extends ModelConfigBase> T getSettings(ResourceLocation resourceLocation, Class<T> classOfT){
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
