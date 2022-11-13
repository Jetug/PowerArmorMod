package com.jetug.power_armor_mod.common.minecraft.registery;

import com.jetug.power_armor_mod.common.minecraft.item.ModCreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.jetug.power_armor_mod.common.util.constants.Global.MOD_ID;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

    public static final RegistryObject<Item> TEST = ITEMS.register("test",
            () -> new Item( new Item.Properties().tab(ModCreativeModeTab.MY_TAB) ));

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
