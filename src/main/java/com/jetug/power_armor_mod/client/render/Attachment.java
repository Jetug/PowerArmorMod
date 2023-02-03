package com.jetug.power_armor_mod.client.render;

import com.google.gson.annotations.SerializedName;

public class Attachment {
    enum AttachmentMode{
        @SerializedName("add")
        ADD    ("add"),
        @SerializedName("replace")
        REPLACE("replace");

        public final String value;

        AttachmentMode(String value){
            this.value = value;
        }
    }

    public AttachmentMode mode;
    public String frame;
    public String armor;
}
