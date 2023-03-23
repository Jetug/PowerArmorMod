package com.jetug.power_armor_mod.common.json;

import com.jetug.power_armor_mod.common.util.enums.BodyPart;

public class EquipmentSettings extends ModelSettingsBase {
    public BodyPart part;
//    public String name;
//    public String model;
//    public String texture;
    public EquipmentAttachment[] attachments;

//    public ResourceLocation getModel(){
//        return new ResourceLocation(Global.MOD_ID, model);
//    }
//
//    public ResourceLocation getTexture(){
//        return new ResourceLocation(Global.MOD_ID, texture);
//    }
}
