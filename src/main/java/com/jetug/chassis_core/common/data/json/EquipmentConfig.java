package com.jetug.chassis_core.common.data.json;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.Lazy;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.jetug.chassis_core.common.util.helpers.TextureHelper.*;

public class EquipmentConfig extends ModelConfigBase {
    public static final String DEFAULT = "default";
    public static final String VARIANT = "variant";

    private final Lazy<HashMap<String, ResourceLocation>> textureLocation = Lazy.of(this::initTextureResource);
    private final Lazy<ResourceLocation> modelLocation = Lazy.of(this::getModelResource);

    public String model;
    public HashMap<String, String> texture;
    public int[] uv;
    public String part;
    public String[] hide = new String[0];
    public EquipmentAttachment[] attachments;
    public EquipmentAttachment[] pov;

    @Nullable
    public Collection<String> getArmorBone(String chassisBone){
        var result = new ArrayList<String>();
        for (var attachment: attachments) {
            if(Objects.equals(attachment.frame, chassisBone))
                result.add(attachment.armor);
        }
        return result;
    }

    public Collection<String> getAllVariants(){
        return textureLocation.get().keySet();
    }

    public ResourceLocation getModelLocation(){
        return modelLocation.get();
    }

    public ResourceLocation getTextureLocation(String tag){
        return textureLocation.get().get(tag);
    }

    private HashMap<String, ResourceLocation> initTextureResource() {
        var result = new HashMap<String, ResourceLocation>();
        texture.forEach((key, value) -> result.put(key, handleTexture(key, value)));
        return result;
    }

    private ResourceLocation handleTexture(String variant, String path) {
        var location = new ResourceLocation(path);
        if(uv == null || uv.length < 4) return location;

        var croppedTexture = cropTexture(location, uv[0], uv[1], uv[2], uv[3]);
        if(croppedTexture != null){
            location = createResource(name + "_" + variant, croppedTexture);
        }
        return location;
    }

    private ResourceLocation getModelResource() {
        return new ResourceLocation(model);
    }
}
