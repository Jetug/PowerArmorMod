package com.jetug.chassis_core.common.foundation.registery;

import com.jetug.chassis_core.ChassisCore;
import com.jetug.chassis_core.common.foundation.item.*;
import com.jetug.chassis_core.common.data.enums.*;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static com.jetug.chassis_core.common.foundation.registery.ArmorMaterialRegistry.*;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, ChassisCore.MOD_ID);

    public static final RegistryObject<Item> PA_FRAME = ITEMS.register("pa_frame", ArmorChassisItem::new);

    public static final RegistryObject<ChassisArmor> PA_HELMET = ITEMS.register("pa_helmet", () ->
            new ChassisArmor(IRON, BodyPart.HELMET, 0.9f));

    public static final RegistryObject<ChassisArmor> PA_BODY = ITEMS.register("pa_body", () ->
            new ChassisArmor(IRON, BodyPart.BODY_ARMOR, 0.9f));

    public static final RegistryObject<ChassisArmor> PA_RIGHT_ARM = ITEMS.register("pa_right_arm", () ->
            new ChassisArmor(IRON, BodyPart.RIGHT_ARM_ARMOR, 0.9f));

    public static final RegistryObject<ChassisArmor> PA_LEFT_ARM = ITEMS.register("pa_left_arm", () ->
            new ChassisArmor(IRON, BodyPart.LEFT_ARM_ARMOR, 0.9f));

    public static final RegistryObject<ChassisArmor> PA_RIGHT_LEG = ITEMS.register("pa_right_leg", () ->
            new ChassisArmor(IRON, BodyPart.RIGHT_LEG_ARMOR, 0.9f));

    public static final RegistryObject<ChassisArmor> PA_LEFT_LEG = ITEMS.register("pa_left_leg", () ->
            new ChassisArmor(IRON, BodyPart.LEFT_LEG_ARMOR, 0.9f));

    public static final RegistryObject<ChassisArmor> ARMOR_LIGHT_HELMET = ITEMS.register("armor_light_helmet", () ->
            new ChassisArmor(IRON, BodyPart.HELMET, 1f));

    public static final RegistryObject<ChassisArmor> ARMOR_LIGHT_BODY = ITEMS.register("armor_light_body", () ->
            new ChassisArmor(IRON, BodyPart.BODY_ARMOR, 1f));

    public static final RegistryObject<ChassisArmor> ARMOR_LIGHT_RIGHT_ARM = ITEMS.register("armor_light_right_arm", () ->
            new ChassisArmor(IRON, BodyPart.RIGHT_ARM_ARMOR, 1f));

    public static final RegistryObject<ChassisArmor> ARMOR_LIGHT_LEFT_ARM = ITEMS.register("armor_light_left_arm", () ->
            new ChassisArmor(IRON, BodyPart.LEFT_ARM_ARMOR, 1f));

    public static final RegistryObject<ChassisArmor> ARMOR_LIGHT_RIGHT_LEG = ITEMS.register("armor_light_right_leg", () ->
            new ChassisArmor(IRON, BodyPart.RIGHT_LEG_ARMOR, 1f));

    public static final RegistryObject<ChassisArmor> ARMOR_LIGHT_LEFT_LEG = ITEMS.register("armor_light_left_leg", () ->
            new ChassisArmor(IRON, BodyPart.LEFT_LEG_ARMOR, 1f));

    public static final RegistryObject<Item> ENGINE = ITEMS.register("engine", () ->
            new EngineItem(1000, 6, 1f));

    public static final RegistryObject<Item> ENGINE_MEDIUM = ITEMS.register("engine_medium", () ->
            new EngineItem(1500, 3, 1.5f));

    public static final RegistryObject<Item> ENGINE_CREATIVE = ITEMS.register("engine_creative", () ->
            new EngineItem(10000000, 0, 1.5f));

    public static final RegistryObject<Item> JETPACK = ITEMS.register("jetpack", () ->
            new JetpackItem( 450, 1.5f, 4));

    public static final RegistryObject<Item> POWER_KNUCKLE = ITEMS.register("power_knuckle", () ->
            new PowerKnuckle(100, 5));

    public static final RegistryObject<Item> FIREPROOF_COATING = ITEMS.register("fireproof_coating", () ->
            new CoatingItem());

    public static final RegistryObject<Item> DRILL = ITEMS.register("drill", () ->
            new DrillItem());

    public static final RegistryObject<Item> FRAME_BODY = ITEMS.register("frame_body", () ->
            new FramePartItem(BodyPart.BODY_FRAME));

    public static final RegistryObject<Item> FRAME_RIGHT_ARM = ITEMS.register("frame_right_arm", () ->
            new FramePartItem(BodyPart.RIGHT_ARM_FRAME));

    public static final RegistryObject<Item> FRAME_LEFT_ARM = ITEMS.register("frame_left_arm", () ->
            new FramePartItem(BodyPart.LEFT_ARM_FRAME));

    public static final RegistryObject<Item> FRAME_RIGHT_LEG = ITEMS.register("frame_right_leg", () ->
            new FramePartItem(BodyPart.RIGHT_LEG_FRAME));

    public static final RegistryObject<Item> FRAME_LEFT_LEG = ITEMS.register("frame_left_leg", () ->
            new FramePartItem(BodyPart.LEFT_LEG_FRAME));

    public static final RegistryObject<Item> CAST_HELMET = ITEMS.register("cast_helmet", () ->
            new CastItem(PA_HELMET.get()));

    public static final RegistryObject<Item> CAST_BODY = ITEMS.register("cast_body", () ->
            new CastItem(PA_BODY.get()));

    public static final RegistryObject<Item> CAST_RIGHT_ARM = ITEMS.register("cast_right_arm", () ->
            new CastItem(PA_RIGHT_ARM.get()));

    public static final RegistryObject<Item> CAST_LEFT_ARM = ITEMS.register("cast_left_arm", () ->
            new CastItem(PA_LEFT_ARM.get()));

    public static final RegistryObject<Item> CAST_RIGHT_LEG = ITEMS.register("cast_right_leg", () ->
            new CastItem(PA_RIGHT_LEG.get()));

    public static final RegistryObject<Item> CAST_LEFT_LEG = ITEMS.register("cast_left_leg", () ->
            new CastItem(PA_LEFT_LEG.get()));


    public static final RegistryObject<Item> CAST_LIGHT_HELMET = ITEMS.register("cast_light_helmet", () ->
            new CastItem(ARMOR_LIGHT_HELMET.get()));

    public static final RegistryObject<Item> CAST_LIGHT_BODY = ITEMS.register("cast_light_body", () ->
            new CastItem(ARMOR_LIGHT_BODY.get()));

    public static final RegistryObject<Item> CAST_LIGHT_RIGHT_ARM = ITEMS.register("cast_light_right_arm", () ->
            new CastItem(ARMOR_LIGHT_RIGHT_ARM.get()));

    public static final RegistryObject<Item> CAST_LIGHT_LEFT_ARM = ITEMS.register("cast_light_left_arm", () ->
            new CastItem(ARMOR_LIGHT_LEFT_ARM.get()));

    public static final RegistryObject<Item> CAST_LIGHT_RIGHT_LEG = ITEMS.register("cast_light_right_leg", () ->
            new CastItem(ARMOR_LIGHT_RIGHT_LEG.get()));

        public static final RegistryObject<Item> CAST_LIGHT_LEFT_LEG = ITEMS.register("cast_light_left_leg", () ->
            new CastItem(ARMOR_LIGHT_LEFT_LEG.get()));


    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }

    private static <I extends Item> I registerItem(final String name, final Supplier<? extends I> sup){
        return ITEMS.register(name, sup).get();
    }
}
