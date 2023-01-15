package com.jetug.power_armor_mod.common.util.helpers;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;

public class DoubleClickHelper{
//
//    public class DoubleClickResult{
//        boolean isClicked;
//        int key
//    }

    private static final int maxTicks = 10;

    private Integer lastKey;
    private int ticks = maxTicks;

    public DoubleClickHelper(){
//        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
//        eventBus.addListener(this::onTick);

        MinecraftForge.EVENT_BUS.addListener(this::onTick);
    }

    private void onTick(final TickEvent.ClientTickEvent event) {
        if (lastKey == null) 
            return;
        ticks -= 1;
        if (ticks <= 0)
            lastKey = null;
    }

    public boolean isDoubleClick(int key){
        if(lastKey == null || lastKey != key) {
            lastKey = key;
            ticks = maxTicks;
            return false;
        }
        else if(ticks > 0 && lastKey == key) {
            lastKey = null;
            return true;
        }
        return false;
    }
    
//    private boolean isEqual(KeyBinding key1, KeyBinding key2){
//        return key1.getKey().getValue() == key2.getKey().getValue();
//    }
}
