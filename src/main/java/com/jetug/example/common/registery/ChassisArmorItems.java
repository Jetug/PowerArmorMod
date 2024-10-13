package com.jetug.example.common.registery;

import com.jetug.chassis_core.ChassisCore;
import com.jetug.chassis_core.common.data.constants.ChassisPart;
import com.jetug.chassis_core.common.foundation.entity.WearableChassis;
import com.jetug.chassis_core.common.foundation.item.ChassisArmor;
import com.jetug.chassis_core.common.foundation.item.ChassisItem;
import com.jetug.example.common.entities.ExampleChassis;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.jetug.example.common.registery.ChassisArmorMaterials.EXAMPLE;


public class ChassisArmorItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ChassisCore.MOD_ID);

    public static final RegistryObject<Item> FRAME_ITEM = ITEMS.register("frame_item", () ->
            new ChassisItem<>(new Item.Properties().fireResistant(), EntityTypes.EXAMPLE_CHASSIS, ExampleChassis::new)
    );

    public static final RegistryObject<Item> EXAMPLE_HELMET = ITEMS.register("t45_helmet", () ->
            new ChassisArmor(new Item.Properties(), EXAMPLE));

    public static final RegistryObject<Item> EXAMPLE_BODY = ITEMS.register("t45_body", () ->
            new ChassisArmor(new Item.Properties(), EXAMPLE));

    public static final RegistryObject<Item> EXAMPLE_RIGHT_ARM = ITEMS.register("t45_right_arm", () ->
            new ChassisArmor(new Item.Properties(), EXAMPLE));

    public static final RegistryObject<Item> EXAMPLE_LEFT_ARM = ITEMS.register("t45_left_arm", () ->
            new ChassisArmor(new Item.Properties(), EXAMPLE));

    public static final RegistryObject<Item> EXAMPLE_RIGHT_LEG = ITEMS.register("t45_right_leg", () ->
            new ChassisArmor(new Item.Properties(), EXAMPLE));

    public static final RegistryObject<Item> EXAMPLE_LEFT_LEG = ITEMS.register("t45_left_leg", () ->
            new ChassisArmor(new Item.Properties(), EXAMPLE));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
