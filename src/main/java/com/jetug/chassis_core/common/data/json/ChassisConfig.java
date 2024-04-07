package com.jetug.chassis_core.common.data.json;

import net.minecraftforge.common.util.Lazy;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Objects;

public class ChassisConfig extends ModelConfigBase {
    public FrameAttachment[] attachments;
    private final Lazy<HashMap<String, String>> boneMap = Lazy.of(this::boneMap);

    @Nullable
    public FrameAttachment getAttachments(String part) {
        for (var att : attachments) {
            if (Objects.equals(att.part, part)) return att;
        }
        return null;
    }

    @Nullable
    public String getPartForBone(String bone) {
        return boneMap.get().get(bone);
    }

    @Nullable
    public HashMap<String, String> boneMap() {
        var result = new HashMap<String, String>();
        for (var att : attachments) {
            for (var bone : att.bones)
                result.put(bone, att.part);
        }
        return result;
    }
}
