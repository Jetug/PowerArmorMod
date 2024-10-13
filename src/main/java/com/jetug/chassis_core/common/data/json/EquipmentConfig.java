package com.jetug.chassis_core.common.data.json;

import com.jetug.chassis_core.common.config.holders.BodyPart;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

import static com.jetug.chassis_core.common.util.helpers.TextureHelper.createResource;
import static com.jetug.chassis_core.common.util.helpers.TextureHelper.cropTexture;

public class EquipmentConfig extends ModelConfigBase {
    private HashMap<String, ResourceLocation> croppedTextures;
    private String parent;
    private BodyPart part;
    private ResourceLocation model;
    private HashMap<String, ResourceLocation> texture;
    private int[] uv;
    private String[] hide = new String[0];
    private EquipmentAttachment[] attachments = new EquipmentAttachment[0];
    private String[] mods = new String[0];

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
        var t = new Thread(this::initTextureResource);
        t.start();
    }

    public Collection<String> getAllVariants() {
        return croppedTextures.keySet();
    }

    public HashMap<String, ResourceLocation> getCroppedTextures() {
        return croppedTextures;
    }

    public String getParent() {
        return parent;
    }

    public BodyPart getPart() {
        return part;
    }

    public int[] getUv() {
        return uv;
    }

    public String[] getHide() {
        return hide;
    }

    public EquipmentAttachment[] getAttachments() {
        return attachments;
    }

    public String[] getMods() {
        return mods;
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
