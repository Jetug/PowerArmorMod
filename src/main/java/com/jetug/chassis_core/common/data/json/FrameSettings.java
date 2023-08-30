package com.jetug.chassis_core.common.data.json;

import com.jetug.chassis_core.common.data.enums.*;

import javax.annotation.*;

public class FrameSettings extends ModelSettingsBase {
    public FrameAttachment[] attachments;

    @Nullable
    public FrameAttachment getAttachments(BodyPart part) {
        for (var att : attachments) {
            if(att.part == part) return att;
        }
        return null;
    }
}
