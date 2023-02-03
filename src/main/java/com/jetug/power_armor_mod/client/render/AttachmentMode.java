package com.jetug.power_armor_mod.client.render;

import com.google.gson.annotations.SerializedName;

public enum AttachmentMode{
    @SerializedName("add")
    ADD    ("add"),
    @SerializedName("replace")
    REPLACE("replace");

    public final String value;

    AttachmentMode(String value){
        this.value = value;
    }
}