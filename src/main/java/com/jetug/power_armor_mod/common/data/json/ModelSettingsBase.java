package com.jetug.power_armor_mod.common.data.json;

import com.jetug.power_armor_mod.common.data.constants.Global;
import net.minecraft.resources.ResourceLocation;

public abstract class ModelSettingsBase {
    public String name;
    public String model;
    public String texture;

    public ResourceLocation getModel(){
        return new ResourceLocation(Global.MOD_ID, model);
    }

    public ResourceLocation getTexture(){
        return new ResourceLocation(Global.MOD_ID, texture);
    }
}
