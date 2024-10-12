package com.jetug.chassis_core.common.data.json;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.Lazy;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;
import static com.jetug.chassis_core.common.util.helpers.TextureHelper.createResource;
import static com.jetug.chassis_core.common.util.helpers.TextureHelper.cropTexture;

public class EquipmentConfig extends ModelConfigBase {
    public String parent;
    public String model;
    private final Lazy<ResourceLocation> modelLocation = Lazy.of(this::getModelResource);
    public HashMap<String, String> texture;
    public int[] uv;
    private final Lazy<HashMap<String, ResourceLocation>> textureLocation = Lazy.of(this::initTextureResource);
    public String part;
    public String[] hide = new String[0];
    public EquipmentAttachment[] attachments;
    public String[] mods = new String[0];

    @Nullable
    public Collection<String> getArmorBone(String chassisBone) {
        var result = new ArrayList<String>();
        for (var attachment : attachments) {

            if (Objects.equals(attachment.frame, chassisBone))
                result.add(attachment.armor);

        }
        return result;
    }

    public Collection<String> getAllVariants() {
        return textureLocation.get().keySet();
    }

    public ResourceLocation getModelLocation() {
        return modelLocation.get();
    }

    public ResourceLocation getTextureLocation(String tag) {
        return textureLocation.get().get(tag);
    }

    private HashMap<String, ResourceLocation> initTextureResource() {
        var result = new HashMap<String, ResourceLocation>();
        texture.forEach((key, value) -> result.put(key, handleTexture(key, value)));
        return result;
    }

    private ResourceLocation handleTexture(String variant, String path) {
        var location = new ResourceLocation(path);
        if (uv == null || uv.length < 4) return location;

        var croppedTexture = cropTexture(location, uv[0], uv[1], uv[2], uv[3]);
        if (croppedTexture != null) {
            location = createResource(name + "_" + variant, croppedTexture);
        }
        return location;
    }

    private ResourceLocation getModelResource() {
        return new ResourceLocation(model);
    }
}
