package com.jetug.power_armor_mod.client.input;

import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;

import java.util.function.BiConsumer;

import static org.lwjgl.glfw.GLFW.*;

public class LongClickController {
    private Integer lastKey;
    private int ticks = 0;
    private BiConsumer<Integer, Integer> repeatListener = (k, t) -> {};
    private BiConsumer<Integer, Integer> releaseListener = (k, t) -> {};

    public LongClickController(){
        MinecraftForge.EVENT_BUS.addListener(this::onTick);
        MinecraftForge.EVENT_BUS.addListener(this::onMouseInput);
        MinecraftForge.EVENT_BUS.addListener(this::onKeyInput);
    }

    public void addRepeatListener(BiConsumer<Integer, Integer> listener){
        this.repeatListener = listener;
    }

    public void addReleaseListener(BiConsumer<Integer, Integer> listener){
        this.releaseListener = listener;
    }

    private void onTick(final TickEvent.ClientTickEvent event) {
        if(lastKey == null) return;
        ticks++;
        //if(repeatListener != null)
            repeatListener.accept(lastKey, ticks);
    }

    private void onMouseInput(final InputEvent.MouseInputEvent event){
        switch (event.getAction()){
            case GLFW_PRESS -> onPress(event.getButton());
            case GLFW_RELEASE -> onRelease(event.getButton());
        }
    }

    private void onKeyInput(final InputEvent.KeyInputEvent event){
        switch (event.getAction()){
            case GLFW_PRESS -> onPress(event.getKey());
            case GLFW_RELEASE -> onRelease(event.getKey());
        }
    }

    private void onPress(int inputKey) {
        ticks = 0;
        lastKey = inputKey;
    }

    private void onRelease(int inputKey) {
        if (lastKey == null || inputKey != lastKey) return;

        lastKey = null;

        //if(releaseListener != null)
            releaseListener.accept(inputKey, ticks);
    }
}
