
package com.jetug.chassis_core.common.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.jetug.chassis_core.common.config.holders.BodyPart;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.crafting.CraftingHelper;

import java.lang.reflect.Modifier;

/**
 * Author: MrCrayfish
 */
public class JsonDeserializers {
    public static final JsonDeserializer<ItemStack> ITEM_STACK = (json, typeOfT, context) -> CraftingHelper.getItemStack(json.getAsJsonObject(), true);
    public static final JsonDeserializer<ResourceLocation> RESOURCE_LOCATION = (json, typeOfT, context) -> new ResourceLocation(json.getAsString());
    public static final JsonDeserializer<BodyPart> BODY_PART = (json, typeOfT, context) -> BodyPart.getPart(json.getAsString());

    public static final Gson GSON_INSTANCE = Util.make(() ->
        new GsonBuilder()
                .registerTypeAdapter(ResourceLocation.class, JsonDeserializers.RESOURCE_LOCATION)
                .registerTypeAdapter(BodyPart.class, JsonDeserializers.BODY_PART)
                .excludeFieldsWithModifiers(Modifier.TRANSIENT)
                .create()
    );
}
