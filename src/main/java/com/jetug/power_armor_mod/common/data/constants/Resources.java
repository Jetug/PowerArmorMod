package com.jetug.power_armor_mod.common.data.constants;

import net.minecraft.resources.ResourceLocation;

public class Resources {
    //Textures
    public static final ResourceLocation ARMOR_INVENTORY_TEXTURE      = resourceLocation("textures/gui/armor_inventory.png");
    public static final ResourceLocation FRAME_TEXTURE_LOCATION       = resourceLocation("textures/entities/power_armor_frame.png");
    public static final ResourceLocation ARMOR_TEXTURE_LOCATION       = resourceLocation("textures/entities/armor.png");
    public static final ResourceLocation INVISIBLE_TEXTURE            = resourceLocation("textures/entities/power_armor_invisible.png");
    public static final ResourceLocation EMPTY_ARMOR_SLOT_HEAD        = resourceLocation("textures/gui/empty_armor_slot_head.png");
    public static final ResourceLocation EMPTY_ARMOR_SLOT_BODY        = resourceLocation("textures/gui/empty_armor_slot_body.png");
    public static final ResourceLocation EMPTY_ARMOR_SLOT_LEFT_ARM    = resourceLocation("textures/gui/empty_armor_slot_left_arm.png");
    public static final ResourceLocation EMPTY_ARMOR_SLOT_RIGHT_ARM   = resourceLocation("textures/gui/empty_armor_slot_right_arm.png");
    public static final ResourceLocation EMPTY_ARMOR_SLOT_LEFT_LEG    = resourceLocation("textures/gui/empty_armor_slot_left_leg.png");
    public static final ResourceLocation EMPTY_ARMOR_SLOT_RIGHT_LEG   = resourceLocation("textures/gui/empty_armor_slot_right_leg.png");
    public static final ResourceLocation PLAYER_INVENTORY_TABS        = resourceLocation("textures/gui/player_inventory_switch_tabs.png");
    public static final ResourceLocation ICONS_LOCATION               = resourceLocation("textures/gui/icons.png");
    public static final ResourceLocation ARMOR_STATION_GUI            = resourceLocation("textures/gui/armor_station_gui.png");
    public static final ResourceLocation PLAYER_INVENTORY_BOTTOM_TABS = resourceLocation("textures/gui/player_inventory_tabs_bottom.png");

    //Animations
    public static final ResourceLocation POWER_ARMOR_ANIMATION_LOCATION = resourceLocation("animations/power_armor.animation.json");
    public static final ResourceLocation ARMOR_ANIMATION_LOCATION       = resourceLocation("animations/armor.animation.json");
    public static final ResourceLocation HAND_ANIMATION_LOCATION        = resourceLocation("animations/hand.animation.json");

    //Models
    public static final ResourceLocation FRAME_MODEL_LOCATION  = resourceLocation("geo/frame/power_armor_frame.geo.json");
    public static final ResourceLocation ARMOR_MODEL_LOCATION  = resourceLocation("geo/armor/armor.geo.json");
    public static final ResourceLocation FRAME_MODEL_BODY      = resourceLocation("geo/frame_parts/frame_body.geo.json");
    public static final ResourceLocation FRAME_MODEL_LEFT_ARM  = resourceLocation("geo/frame_parts/frame_left_arm.geo.json");
    public static final ResourceLocation FRAME_MODEL_RIGHT_ARM = resourceLocation("geo/frame_parts/frame_right_arm.geo.json");
    public static final ResourceLocation FRAME_MODEL_LEFT_LEG  = resourceLocation("geo/frame_parts/frame_left_leg.geo.json");
    public static final ResourceLocation FRAME_MODEL_RIGHT_LEG = resourceLocation("geo/frame_parts/frame_right_leg.geo.json");
    public static final ResourceLocation HAND_MODEL_LOCATION   = resourceLocation("geo/misc/hand.geo.json");

    public static ResourceLocation resourceLocation(String location){
        return new ResourceLocation(Global.MOD_ID, location);
    }
}
