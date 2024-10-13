package com.jetug.chassis_core.common.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jetug.chassis_core.ChassisCore;
import com.jetug.chassis_core.common.config.holders.BodyPart;
import com.jetug.chassis_core.common.data.annotation.Validator;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InvalidObjectException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static net.minecraftforge.registries.ForgeRegistries.ITEMS;

public class ConfigUtils {
    private static final int FILE_TYPE_LENGTH_VALUE = ".json".length();

    public static final Gson GSON_INSTANCE = Util.make(() -> {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ResourceLocation.class, JsonDeserializers.RESOURCE_LOCATION);
        builder.registerTypeAdapter(BodyPart.class, JsonDeserializers.BODY_PART);
        builder.excludeFieldsWithModifiers(Modifier.TRANSIENT);
        return builder.create();
    });

    @NotNull
    private static Map<ResourceLocation, Resource> getJsonResources(ResourceManager manager, String path, ResourceLocation id) {
        return manager.listResources(path, (fileName) -> fileName.getPath().endsWith(id.getPath() + ".json"));
    }

    @NotNull
    public static<T, Y> Map<T, Y> getConfigMap(ResourceManager manager, Function<Item, Boolean> tClass, Class<Y> yClass, String resourcePath) {
        var map = new HashMap<T, Y>();

        ITEMS.getValues().stream().filter(tClass::apply).forEach(item ->
        {
            var id = ITEMS.getKey(item);

            if (id != null) {
                var resources = new ArrayList<>(getJsonResources(manager, resourcePath, id).keySet());

                resources.sort((r1, r2) -> {
                    if (r1.getNamespace().equals(r2.getNamespace())) return 0;
                    return r2.getNamespace().equals(ChassisCore.MOD_ID) ? 1 : -1;
                });

                resources.forEach(resourceLocation ->
                {
                    var path = resourceLocation.getPath().substring(0, resourceLocation.getPath().length() - FILE_TYPE_LENGTH_VALUE);
                    var splitPath = path.split("/");

                    // Makes sure the file name matches exactly with the id of the config
                    if (!id.getPath().equals(splitPath[splitPath.length - 1]))
                        return;

                    // Also check if the mod id matches with the config's registered namespace
                    if (!id.getNamespace().equals(resourceLocation.getNamespace()))
                        return;

                    manager.getResource(resourceLocation).ifPresent(resource ->
                    {
                        try (var reader = new BufferedReader(new InputStreamReader(resource.open(), StandardCharsets.UTF_8))) {
                            var equipment = GsonHelper.fromJson(GSON_INSTANCE, reader, yClass);

                            if (Validator.isValidObject(equipment)) {
                                map.put((T) item, equipment);
                            }
                            else {
                                ChassisCore.LOGGER.error("Couldn't load data file {} as it is missing or malformed. Using default config data", resourceLocation);
                                map.putIfAbsent((T) item, yClass.getDeclaredConstructor().newInstance());
                            }
                        }
                        catch (InvalidObjectException e) {
                            ChassisCore.LOGGER.error("Missing required properties for {}", resourceLocation);
                            e.printStackTrace();
                        }
                        catch (IOException | InvocationTargetException | InstantiationException | NoSuchMethodException e) {
                            ChassisCore.LOGGER.error("Couldn't parse data file {}", resourceLocation);
                        }
                        catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    });
                });
            }
        });
        return map;
    }

}
