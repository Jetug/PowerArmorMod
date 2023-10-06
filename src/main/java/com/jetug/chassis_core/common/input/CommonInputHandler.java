package com.jetug.chassis_core.common.input;

import com.jetug.chassis_core.common.events.CommonInputEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;

import static com.jetug.chassis_core.common.input.InputKey.JUMP;
import static com.jetug.chassis_core.common.input.KeyAction.PRESS;
import static com.jetug.chassis_core.common.input.KeyAction.REPEAT;
import static com.jetug.chassis_core.common.util.helpers.PlayerUtils.*;

@SuppressWarnings("ConstantConditions")
public class CommonInputHandler {
    public static void onKeyInput(InputKey key, KeyAction action, Player player) {
        MinecraftForge.EVENT_BUS.post(new CommonInputEvent(key, action, player));

        if (!isWearingChassis(player) || key == null) return;

        if((action == PRESS || action == REPEAT) && key == JUMP)
            getPlayerChassis(player).jump();

        switch (action) {
            case PRESS -> onPress(key, player);
            case RELEASE -> onRelease(key, player);
            case DOUBLE_CLICK -> onDoubleClick(key, player);
            case LONG_PRESS -> onLongPress(key, player);
        }
    }

    public static void onPress(InputKey key, Player player){
        if (key == InputKey.LEAVE)
            stopWearingArmor(player);
    }

    public static void onRelease(InputKey key, Player player){
        if (!isWearingChassis(player)) return;
//        if(key == ATTACK) getPlayerChassis(player).powerPunch();
//        if(key == USE) getPlayerChassis(player).resetAttackCharge();
    }

    public static void onDoubleClick(InputKey key, Player player){
        if (!isWearingChassis(player)) return;

//        DashDirection direction = switch (key){
//            case UP    -> DashDirection.FORWARD;
//            case DOWN  -> DashDirection.BACK;
//            case LEFT  -> DashDirection.LEFT;
//            case RIGHT -> DashDirection.RIGHT;
//            case JUMP  -> DashDirection.UP;
//            default -> null;
//        };
//
//        if(direction == null) return;
//        getPlayerChassis(player).dash(direction);
    }

    public static void onLongPress(InputKey key, Player player){
//        if (!isWearingChassis(player)) return;
//        var bool = key == USE;
//        if(bool){
//            getPlayerChassis(player).addAttackCharge();
//        }
    }
}
