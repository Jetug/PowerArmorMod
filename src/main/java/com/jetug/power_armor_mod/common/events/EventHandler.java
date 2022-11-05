package com.jetug.power_armor_mod.common.events;

import com.jetug.power_armor_mod.common.util.helpers.timer.Timer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventHandler {

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        Timer.getInstance().tick(TickEvent.Type.CLIENT);
        System.out.println("Player tick!");
    }

//    @SubscribeEvent
//    public static void onClientTick(TickEvent.ClientTickEvent event) {
//        Timer.getInstance().tick(TickEvent.Type.CLIENT);
//    }
}