package com.jetug.chassis_core.common.config.holders;

import com.jetug.chassis_core.ChassisCore;
import net.minecraft.resources.ResourceLocation;


public class ResourceHolder {
    protected final ResourceLocation id;

    public ResourceHolder(ResourceLocation id) {
        this.id = id;
    }

    public ResourceHolder(String name) {
        this.id = new ResourceLocation(ChassisCore.MOD_ID, name);
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public boolean equals(ResourceLocation obj) {
        return this.id.equals(obj);
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
