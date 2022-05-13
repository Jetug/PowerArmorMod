package com.jetug.power_armor_mod.common.util.constants;

import com.jetug.power_armor_mod.PowerArmorMod;
import net.minecraft.util.ResourceLocation;

import static com.jetug.power_armor_mod.PowerArmorMod.MOD_ID;

public class Resources {
    public static final ResourceLocation PLAYER_DATA_LOCATION = new ResourceLocation(MOD_ID, "player_data");
    public static final ResourceLocation POWER_ARMOR_PART_DATA_LOCATION = new ResourceLocation(MOD_ID, "power_armor_part_data");

    public static final ResourceLocation POWER_ARMOR_MODEL_LOCATION = new ResourceLocation(PowerArmorMod.MOD_ID, "geo/power_armor_frame.geo.json");
    public static final ResourceLocation POWER_ARMOR_TEXTURE_LOCATION = new ResourceLocation(PowerArmorMod.MOD_ID, "textures/entities/power_armor_frame.png");
    public static final ResourceLocation POWER_ARMOR_ANIMATION_LOCATION = new ResourceLocation(PowerArmorMod.MOD_ID, "animations/power_armor.animation.json");

    public static final ResourceLocation ARMOR_MODEL_LOCATION = new ResourceLocation(PowerArmorMod.MOD_ID, "geo/armor.geo.json");
    public static final ResourceLocation ARMOR_TEXTURE_LOCATION = new ResourceLocation(PowerArmorMod.MOD_ID, "textures/entities/armor.png");
    public static final ResourceLocation ARMOR_ANIMATION_LOCATION = new ResourceLocation(PowerArmorMod.MOD_ID, "animations/armor.animation.json");

    public static final ResourceLocation HELMET_MODEL_LOCATION = new ResourceLocation(PowerArmorMod.MOD_ID, "geo/armor_head.geo.json");
    public static final ResourceLocation HELMET_TEXTURE_LOCATION = new ResourceLocation(PowerArmorMod.MOD_ID, "textures/entities/armor_head.png");

    public static final ResourceLocation INVISIBLE_TEXTURE = new ResourceLocation(PowerArmorMod.MOD_ID, "textures/entities/power_armor_invisible.png");
}
