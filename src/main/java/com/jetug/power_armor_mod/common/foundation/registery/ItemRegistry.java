package com.jetug.power_armor_mod.common.foundation.registery;

import com.jetug.power_armor_mod.common.foundation.ModCreativeModeTab;
import com.jetug.power_armor_mod.common.foundation.item.ArmorFrameItem;
import com.jetug.power_armor_mod.common.foundation.item.EngineItem;
import com.jetug.power_armor_mod.common.foundation.item.HandItem;
import com.jetug.power_armor_mod.common.foundation.item.PowerArmorItem;
import com.jetug.power_armor_mod.common.data.enums.BodyPart;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static com.jetug.power_armor_mod.common.foundation.registery.ArmorMaterialRegistry.IRON;
import static com.jetug.power_armor_mod.common.data.constants.Global.MOD_ID;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

    public static final RegistryObject<Item> PA_FRAME = ITEMS.register("pa_frame", ArmorFrameItem::new);

    public static final RegistryObject<Item> PA_HELMET = ITEMS.register("pa_helmet", () ->
            new PowerArmorItem(IRON, BodyPart.HEAD));

    public static final RegistryObject<Item> PA_BODY = ITEMS.register("pa_body", () ->
            new PowerArmorItem(IRON, BodyPart.BODY));

    public static final RegistryObject<Item> PA_RIGHT_ARM = ITEMS.register("pa_right_arm", () ->
            new PowerArmorItem(IRON, BodyPart.RIGHT_ARM));

    public static final RegistryObject<Item> PA_LEFT_ARM = ITEMS.register("pa_left_arm", () ->
            new PowerArmorItem(IRON, BodyPart.LEFT_ARM));

    public static final RegistryObject<Item> PA_RIGHT_LEG = ITEMS.register("pa_right_leg", () ->
            new PowerArmorItem(IRON, BodyPart.RIGHT_LEG));

    public static final RegistryObject<Item> PA_LEFT_LEG = ITEMS.register("pa_left_leg", () ->
            new PowerArmorItem(IRON, BodyPart.LEFT_LEG));

    public static final RegistryObject<Item> ENGINE = ITEMS.register("engine", () ->
            new EngineItem(new Item.Properties().tab(ModCreativeModeTab.MY_TAB)));

    public static final RegistryObject<HandItem> HAND = ITEMS.register("hand", () ->
            new HandItem(new Item.Properties().tab(ModCreativeModeTab.MY_TAB)));

    //tutor
    public static final RegistryObject<Item> CITRINE = ITEMS.register("citrine",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MY_TAB)));

    public static final RegistryObject<Item> RAW_CITRINE = ITEMS.register("raw_citrine",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MY_TAB)));

    public static final RegistryObject<Item> GEM_CUTTER_TOOL = ITEMS.register("gem_cutter_tool",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MY_TAB).durability(32)));

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }

    private static <I extends Item> I registerItem(final String name, final Supplier<? extends I> sup){
        return ITEMS.register(name, sup).get();
    }
}
