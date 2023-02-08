package com.jetug.power_armor_mod.common.util.helpers;

import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import org.lwjgl.glfw.GLFW;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class DoubleClickController {
    private static final AtomicInteger maxTicks = new AtomicInteger(10);

    private Integer lastKey;
    private int ticks = maxTicks.get();
    private Consumer<InputEvent.KeyInputEvent> listener = (e) -> {};

    public DoubleClickController(){
        MinecraftForge.EVENT_BUS.addListener(this::onTick);
        MinecraftForge.EVENT_BUS.addListener(this::onClick);
    }

    public void addListener(Consumer<InputEvent.KeyInputEvent> listener){
        this.listener = listener;
    }

    private void onTick(final TickEvent.ClientTickEvent event) {
        if (lastKey == null) 
            return;
        ticks -= 1;
        if (ticks <= 0)
            lastKey = null;
    }

    public void onClick(final InputEvent.KeyInputEvent event){
        if (event.getAction() == GLFW.GLFW_PRESS) {
            var key = event.getKey();
            if (lastKey == null || lastKey != key) {
                lastKey = key;
                ticks = maxTicks.get();
            } else if (ticks > 0) {
                lastKey = null;
                if (listener != null)
                    listener.accept(event);
            }
        }
    }

    public boolean isDoubleClick(int key){
        if(lastKey == null || lastKey != key) {
            lastKey = key;
            ticks = maxTicks.get();
            return false;
        }
        else if(ticks > 0) {
            lastKey = null;
            return true;
        }
        return false;
    }
}
