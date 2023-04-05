package com.jetug.power_armor_mod.client.input;

import com.jetug.power_armor_mod.common.foundation.entity.PowerArmorEntity;
import com.jetug.power_armor_mod.common.input.InputKey;
import com.jetug.power_armor_mod.common.input.KeyAction;
import com.jetug.power_armor_mod.common.util.extensions.PlayerExtension;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.InputEvent;
import org.lwjgl.glfw.GLFW;

import static com.jetug.power_armor_mod.client.ClientConfig.getLocalPlayer;
import static com.jetug.power_armor_mod.common.input.CommonInputHandler.onKeyInput;
import static com.jetug.power_armor_mod.common.util.extensions.PlayerExtension.*;

@SuppressWarnings("ConstantConditions")
public class InputHandler {
    public static void onArmorKeyInput(InputEvent.KeyInputEvent event, PowerArmorEntity entity) {

        KeyAction action;
        if(event.getAction() == GLFW.GLFW_PRESS)
            action = KeyAction.PRESS;
        else if(event.getAction() == GLFW.GLFW_RELEASE)
            action = KeyAction.RELEASE;
        else
            action = KeyAction.REPEAT;

        onKeyInput(InputKey.getByKey(event.getKey()), action, getLocalPlayer());

//        if (OPTIONS.keyJump.isDown())
//            entity.jump();
//        switch (event.getAction()) {
//            case GLFW.GLFW_PRESS:
//                if (LEAVE.isDown())
//                    stopWearingArmor(getLocalPlayer());
//                break;
//            case GLFW.GLFW_RELEASE:
//                onMouseRelease(event.getKey());
//                break;
//        }
    }

    public static void onDoubleClick(InputEvent.KeyInputEvent event){
        if (getLocalPlayer() == null || !isWearingPowerArmor()) return;
//
//        var key = event.getKey();
//        var armorEntity = (PowerArmorEntity) getLocalPlayer().getVehicle();
//        DashDirection direction;

        onKeyInput(InputKey.getByKey(event.getKey()), KeyAction.DOUBLE_CLICK, getLocalPlayer());

//
//        if (keysEqual(key, OPTIONS.keyUp)) {
//            direction = DashDirection.FORWARD;
//        }
//        else if (keysEqual(key, OPTIONS.keyDown)) {
//            direction = DashDirection.BACK;
//        }
//        else if (keysEqual(key, OPTIONS.keyLeft)) {
//            direction = DashDirection.LEFT;
//        }
//        else if (keysEqual(key, OPTIONS.keyRight)) {
//            direction = DashDirection.RIGHT;
//        }
//        else if (keysEqual(key, OPTIONS.keyJump)) {
//            direction = DashDirection.UP;
//        }
//        else return;
//
//        armorEntity.dash(direction);
    }

    @SuppressWarnings("ConstantConditions")
    public static void onLongClick(int key, int ticks){
        if(Minecraft.getInstance().isPaused()) return;

        onKeyInput(InputKey.getByKey(key), KeyAction.LONG_PRESS,  getLocalPlayer());

//        if (isWearingPowerArmor()) {
//            var options = Minecraft.getInstance().options;
//            //LOGGER.log(Level.INFO, "onLongClick: ticks:" + ticks);
//
//            if(keysEqual(key, options.keyUse)){
//                var pa = PlayerExtension.getPlayerPowerArmor();
//                pa.addAttackCharge();
//            }
//        }
    }

    public static void onLongRelease(int key, int ticks){}

    @SuppressWarnings("ConstantConditions")
    public static void onMouseRelease(int key){
        onKeyInput(InputKey.getByKey(key), KeyAction.RELEASE,  getLocalPlayer());

//        if (isWearingPowerArmor()) {
//            var options = Minecraft.getInstance().options;
//
//            if(keysEqual(key, options.keyUse, options.keyAttack) ){
//                var pa = PlayerExtension.getPlayerPowerArmor();
//                pa.resetAttackCharge();
//            }
//        }
    }

    private static boolean keysEqual(int key1, KeyMapping key2){
        return key1 == key2.getKey().getValue();
    }

    public static boolean keysEqual(int key1, KeyMapping... arguments){
        for (KeyMapping argument : arguments)
            if(key1 == argument.getKey().getValue()) return true;
        return false;
    }
}
