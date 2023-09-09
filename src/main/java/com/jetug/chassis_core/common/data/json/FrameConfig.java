package com.jetug.chassis_core.common.data.json;

import javax.annotation.*;
import java.util.Objects;

public class FrameConfig extends ModelConfigBase {
    public FrameAttachment[] attachments;

    @Nullable
    public FrameAttachment getAttachments(String part) {
        for (var att : attachments) {
            if(Objects.equals(att.part, part)) return att;
        }
        return null;
    }
}
