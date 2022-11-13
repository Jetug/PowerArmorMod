package com.jetug.power_armor_mod.common.util.constants;

import com.jetug.power_armor_mod.common.util.annotations.Model;
import com.jetug.power_armor_mod.common.util.annotations.Texture;
import net.minecraft.resources.ResourceLocation;

import static com.jetug.power_armor_mod.common.util.constants.Global.MOD_ID;
import static com.jetug.power_armor_mod.common.util.enums.BodyPart.*;
import static com.jetug.power_armor_mod.common.util.enums.EquipmentType.STANDARD;

public class Resources {
    @Model(ArmorPart = HEAD, Type = STANDARD)
    public static final ResourceLocation HELMET_MODEL_LOCATION = new ResourceLocation(MOD_ID, "geo/armor_head.geo.json");
    @Texture(ArmorPart = HEAD, Type = STANDARD)
    public static final ResourceLocation HELMET_TEXTURE_LOCATION = new ResourceLocation(MOD_ID, "textures/entities/armor_head.png");

    @Model(ArmorPart = BODY, Type = STANDARD)
    public static final ResourceLocation BODY_MODEL_LOCATION = new ResourceLocation(MOD_ID, "geo/armor_body.geo.json");
    @Texture(ArmorPart = BODY, Type = STANDARD)
    public static final ResourceLocation BODY_TEXTURE_LOCATION = new ResourceLocation(MOD_ID, "textures/entities/armor_body.png");

    @Model(ArmorPart = LEFT_ARM, Type = STANDARD)
    public static final ResourceLocation LEFT_ARM_MODEL_LOCATION = new ResourceLocation(MOD_ID, "geo/armor_left_arm.geo.json");
    @Texture(ArmorPart = LEFT_ARM, Type = STANDARD)
    public static final ResourceLocation LEFT_ARM_TEXTURE_LOCATION = new ResourceLocation(MOD_ID, "textures/entities/armor_left_arm.png");

    @Model(ArmorPart = RIGHT_ARM, Type = STANDARD)
    public static final ResourceLocation RIGHT_ARM_MODEL_LOCATION = new ResourceLocation(MOD_ID, "geo/armor_right_arm.geo.json");
    @Texture(ArmorPart = RIGHT_ARM, Type = STANDARD)
    public static final ResourceLocation RIGHT_ARM_TEXTURE_LOCATION = new ResourceLocation(MOD_ID, "textures/entities/armor_right_arm.png");

    @Model(ArmorPart = LEFT_LEG, Type = STANDARD)
    public static final ResourceLocation LEFT_LEG_MODEL_LOCATION = new ResourceLocation(MOD_ID, "geo/armor_left_leg.geo.json");
    @Texture(ArmorPart = LEFT_LEG, Type = STANDARD)
    public static final ResourceLocation LEFT_LEG_TEXTURE_LOCATION = new ResourceLocation(MOD_ID, "textures/entities/armor_left_leg.png");

    @Model(ArmorPart = RIGHT_LEG, Type = STANDARD)
    public static final ResourceLocation RIGHT_LEG_MODEL_LOCATION = new ResourceLocation(MOD_ID, "geo/armor_right_leg.geo.json");
    @Texture(ArmorPart = RIGHT_LEG, Type = STANDARD)
    public static final ResourceLocation RIGHT_LEG_TEXTURE_LOCATION = new ResourceLocation(MOD_ID, "textures/entities/armor_right_leg.png");

    public static final ResourceLocation PLAYER_DATA_LOCATION = new ResourceLocation(MOD_ID, "player_data");
    public static final ResourceLocation POWER_ARMOR_PART_DATA_LOCATION = new ResourceLocation(MOD_ID, "power_armor_part_data");

    public static final ResourceLocation POWER_ARMOR_MODEL_LOCATION = new ResourceLocation(MOD_ID, "geo/power_armor_frame.geo.json");
    public static final ResourceLocation POWER_ARMOR_TEXTURE_LOCATION = new ResourceLocation(MOD_ID, "textures/entities/power_armor_frame.png");
    public static final ResourceLocation POWER_ARMOR_ANIMATION_LOCATION = new ResourceLocation(MOD_ID, "animations/power_armor.animation.json");

    public static final ResourceLocation ARMOR_MODEL_LOCATION = new ResourceLocation(MOD_ID, "geo/armor.geo.json");
    public static final ResourceLocation ARMOR_TEXTURE_LOCATION = new ResourceLocation(MOD_ID, "textures/entities/armor.png");
    public static final ResourceLocation ARMOR_ANIMATION_LOCATION = new ResourceLocation(MOD_ID, "animations/armor.animation.json");

    public static final ResourceLocation INVISIBLE_TEXTURE = new ResourceLocation(MOD_ID, "textures/entities/power_armor_invisible.png");
}
