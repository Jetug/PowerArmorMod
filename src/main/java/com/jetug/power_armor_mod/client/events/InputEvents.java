package com.jetug.power_armor_mod.client.events;

import com.jetug.power_armor_mod.common.input.*;
import com.jetug.power_armor_mod.common.network.actions.InputAction;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;
import com.jetug.power_armor_mod.common.input.InputKey;
import com.jetug.power_armor_mod.common.input.KeyAction;

import static com.jetug.power_armor_mod.client.ClientConfig.getLocalPlayer;
import static com.jetug.power_armor_mod.common.network.PacketSender.doServerAction;
import static com.jetug.power_armor_mod.common.util.extensions.PlayerExtension.*;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class InputEvents {
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent()
    public static void onKeyInput(InputEvent.KeyInputEvent event)
    {
        KeyAction action;
        if(event.getAction() == GLFW.GLFW_PRESS)
            action = KeyAction.PRESS;
        else if(event.getAction() == GLFW.GLFW_RELEASE)
            action = KeyAction.RELEASE;
        else
            action = KeyAction.REPEAT;

        CommonInputHandler.onKeyInput(InputKey.getByKey(event.getKey()), action, getLocalPlayer());
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent()
    public static void onMouseKeyInput(InputEvent.MouseInputEvent event) {
        if (event.getAction() == GLFW.GLFW_RELEASE)
            CommonInputHandler.onKeyInput(InputKey.getByKey(event.getButton()), KeyAction.RELEASE,  getLocalPlayer());
    }

    public static void onDoubleClick(InputEvent.KeyInputEvent event){
        handleInput(event.getKey(), KeyAction.DOUBLE_CLICK);
    }

    public static void onLongClick(int key, int ticks){
        if(Minecraft.getInstance().isPaused()) return;
        CommonInputHandler.onKeyInput(InputKey.getByKey(key), KeyAction.LONG_PRESS,  getLocalPlayer());

        //handleInput(key, KeyAction.LONG_PRESS);
    }

    public static void onLongRelease(int key, int ticks){}

    private static void handleInput(int key, KeyAction action){
        var inputKey = InputKey.getByKey(key);
        if (getLocalPlayer() == null || inputKey == null) return;

        doServerAction(new InputAction(inputKey, action), -1);
        CommonInputHandler.onKeyInput(inputKey, action, getLocalPlayer());
    }
}
