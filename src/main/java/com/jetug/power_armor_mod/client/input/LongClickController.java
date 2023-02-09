package com.jetug.power_armor_mod.client.input;

import cpw.mods.util.LambdaExceptionUtils;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import org.lwjgl.glfw.GLFW;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class LongClickController {
    private Integer lastKey;
    private int ticks = 0;
    private BiConsumer<InputEvent.KeyInputEvent, Integer> listener = (k, t) -> {};

    public LongClickController(){
        MinecraftForge.EVENT_BUS.addListener(this::onTick);
        MinecraftForge.EVENT_BUS.addListener(this::onClick);
    }

    public void addListener(BiConsumer<InputEvent.KeyInputEvent, Integer> listener){
        this.listener = listener;
    }

    private void onTick(final TickEvent.ClientTickEvent event) {
        if(lastKey == null) return;
        ticks++;
    }

    private void onClick(final InputEvent.KeyInputEvent event){
        if (event.getAction() == GLFW.GLFW_REPEAT) {
            if(event.getKey() != lastKey){
                ticks = 0;
                lastKey = event.getKey();
            }
            else listener.accept(event, ticks);
        }
        else if(event.getAction() == GLFW.GLFW_RELEASE){
            if(event.getKey() == lastKey){
                lastKey = null;
            }
        }
    }
}
