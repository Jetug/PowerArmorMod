package com.jetug.power_armor_mod.common.events;

import com.jetug.power_armor_mod.common.util.constants.Global;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class EventHandler {
//    @SubscribeEvent
//    public static void onClientTick(TickEvent.ClientTickEvent event) {
//        Global.CLIENT_TIMER.tick();
//    }

    @SubscribeEvent
    public static void onTick(TickEvent event) {
        switch (event.type){
            case WORLD:
                break;
            case PLAYER:
                break;
            case CLIENT:
                Global.CLIENT_TIMER.tick();
                break;
            case SERVER:
                break;
            case RENDER:
                break;
        }
    }
}