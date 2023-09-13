package com.jetug.chassis_core.common.data.json;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.Lazy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import static com.jetug.chassis_core.common.util.helpers.TextureHelper.*;

public class EquipmentConfig extends ModelConfigBase {
    private final Lazy<ResourceLocation> textureLocation = Lazy.of(this::getTextureResource);
    private final Lazy<ResourceLocation> modelLocation = Lazy.of(this::getModelResource);

    public String model;
    public String texture;
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

    public ResourceLocation getModelLocation(){
        return modelLocation.get();
    }

    public ResourceLocation getTextureLocation(){
        return textureLocation.get();
    }

    private ResourceLocation getTextureResource() {
        var location = new ResourceLocation(texture);
        if(uv == null || uv.length < 4) return location;

        var croppedTexture = cropTexture(location, uv[0], uv[1], uv[2], uv[3]);
        if(croppedTexture != null){
            location = createResource(name, croppedTexture);
        }
        return location;
    }

    private ResourceLocation getModelResource() {
        return new ResourceLocation(model);
    }
}
