package com.jetug.power_armor_mod.common.util.constants;

import net.minecraft.resources.ResourceLocation;

public class Resources {
    public static final ResourceLocation ARMOR_INVENTORY_TEXTURE = resourceLocation("textures/gui/armor_inventory.png");
    public static final ResourceLocation POWER_ARMOR_MODEL_LOCATION = resourceLocation("geo/power_armor_frame.geo.json");
    public static final ResourceLocation POWER_ARMOR_TEXTURE_LOCATION = resourceLocation("textures/entities/power_armor_frame.png");
    public static final ResourceLocation POWER_ARMOR_ANIMATION_LOCATION = resourceLocation("animations/power_armor.animation.json");
    public static final ResourceLocation ARMOR_MODEL_LOCATION = resourceLocation("geo/armor.geo.json");
    public static final ResourceLocation ARMOR_TEXTURE_LOCATION = resourceLocation("textures/entities/armor.png");
    public static final ResourceLocation ARMOR_ANIMATION_LOCATION = resourceLocation("animations/armor.animation.json");
    public static final ResourceLocation INVISIBLE_TEXTURE = resourceLocation("textures/entities/power_armor_invisible.png");
    public static final ResourceLocation PLAYER_INVENTORY_TABS = resourceLocation("textures/gui/player_inventory_switch_tabs.png");
    public static final ResourceLocation PLAYER_INVENTORY_BOTTOM_TABS = resourceLocation("textures/gui/player_inventory_tabs_bottom.png");

    public static ResourceLocation resourceLocation(String location){
        return new ResourceLocation(Global.MOD_ID, location);
    }
}
