package com.jetug.power_armor_mod.common.data.constants;

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

    public static final ResourceLocation HAND_ANIMATION_LOCATION = resourceLocation("animations/hand.animation.json");

    public static final ResourceLocation PLAYER_INVENTORY_TABS = resourceLocation("textures/gui/player_inventory_switch_tabs.png");
    public static final ResourceLocation PLAYER_INVENTORY_BOTTOM_TABS = resourceLocation("textures/gui/player_inventory_tabs_bottom.png");
    public static final ResourceLocation ICONS_LOCATION = new ResourceLocation(Global.MOD_ID ,"textures/gui/icons.png");

    public static final ResourceLocation EMPTY_ARMOR_SLOT_HEAD      = new ResourceLocation(Global.MOD_ID ,"textures/gui/empty_armor_slot_head.png");
    public static final ResourceLocation EMPTY_ARMOR_SLOT_BODY      = new ResourceLocation(Global.MOD_ID ,"textures/gui/empty_armor_slot_body.png");
    public static final ResourceLocation EMPTY_ARMOR_SLOT_LEFT_ARM  = new ResourceLocation(Global.MOD_ID ,"textures/gui/empty_armor_slot_left_arm.png");
    public static final ResourceLocation EMPTY_ARMOR_SLOT_RIGHT_ARM = new ResourceLocation(Global.MOD_ID ,"textures/gui/empty_armor_slot_right_arm.png");
    public static final ResourceLocation EMPTY_ARMOR_SLOT_LEFT_LEG  = new ResourceLocation(Global.MOD_ID ,"textures/gui/empty_armor_slot_left_leg.png");
    public static final ResourceLocation EMPTY_ARMOR_SLOT_RIGHT_LEG = new ResourceLocation(Global.MOD_ID ,"textures/gui/empty_armor_slot_right_leg.png");

    public static ResourceLocation resourceLocation(String location){
        return new ResourceLocation(Global.MOD_ID, location);
    }
}
