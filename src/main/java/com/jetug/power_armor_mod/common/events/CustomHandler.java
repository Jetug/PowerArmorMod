package com.jetug.power_armor_mod.common.events;

import com.jetug.power_armor_mod.common.data.constants.Global;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.EventPriority;

import static java.lang.System.out;

@Mod.EventBusSubscriber(modid = Global.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CustomHandler {

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onEntityContainerChange(ContainerChangedEvent event) {
        var entity = event.getEntity();
        out.println(entity);
    }
}