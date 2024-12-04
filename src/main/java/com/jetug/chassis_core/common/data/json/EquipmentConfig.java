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
    private HashMap<String, ResourceLocation> croppedTextures;
    public String parent;
    public ResourceLocation model;
    public HashMap<String, ResourceLocation> texture;
    public int[] uv;
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

    public void onFinishLoading(){
        croppedTextures = texture;
        initTextureResource();
//        var t = new Thread(this::initTextureResource);
//        t.start();
    }

    public Collection<String> getAllVariants() {
        return croppedTextures.keySet();
    }

    public ResourceLocation getModel() {
        return model;
    }

    public ResourceLocation getTexture(String tag) {
        return croppedTextures.get(tag);
    }

    private void initTextureResource() {
        var result = new HashMap<String, ResourceLocation>();
        texture.forEach((key, value) -> result.put(key, handleTexture(key, value)));
        croppedTextures = result;
    }

    private ResourceLocation handleTexture(String variant, ResourceLocation location) {
        if (uv == null || uv.length < 4) return location;

        var croppedTexture = cropTexture(location, uv[0], uv[1], uv[2], uv[3]);
        if (croppedTexture != null) {
            location = createResource(name + "_" + variant, croppedTexture);
        }
        return location;
    }

    private ResourceLocation getModelResource() {
        return model;
    }
}
