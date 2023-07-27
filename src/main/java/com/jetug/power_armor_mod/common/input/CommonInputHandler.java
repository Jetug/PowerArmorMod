package com.jetug.power_armor_mod.common.input;

import com.jetug.power_armor_mod.common.data.constants.Global;
import com.jetug.power_armor_mod.common.data.enums.DashDirection;
import net.minecraft.world.entity.player.Player;
import org.apache.logging.log4j.Level;

import java.util.logging.Logger;

import static com.jetug.power_armor_mod.common.input.KeyAction.*;
import static com.jetug.power_armor_mod.common.input.InputKey.*;
import static com.jetug.power_armor_mod.common.util.extensions.PlayerExtension.*;
import static net.minecraft.client.renderer.debug.DebugRenderer.getTargetedEntity;

@SuppressWarnings("ConstantConditions")
public class CommonInputHandler {
    public static void onKeyInput(InputKey key, KeyAction action, Player player) {
        if (!isWearingPowerArmor(player) || key == null) return;

        if((action == PRESS || action == REPEAT) && key == JUMP)
            getPlayerPowerArmor(player).jump();

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
        if (!isWearingPowerArmor(player)) return;
        if(key == ATTACK) getPlayerPowerArmor(player).punch();
        if(key == USE || key == ATTACK) getPlayerPowerArmor(player).resetAttackCharge();
    }

    public static void onDoubleClick(InputKey key, Player player){
        if (!isWearingPowerArmor(player)) return;

        DashDirection direction = switch (key){
            case UP    -> DashDirection.FORWARD;
            case DOWN  -> DashDirection.BACK;
            case LEFT  -> DashDirection.LEFT;
            case RIGHT -> DashDirection.RIGHT;
            case JUMP  -> DashDirection.UP;
            default -> null;
        };

        if(direction == null) return;
        getPlayerPowerArmor(player).dash(direction);
    }

    public static void onLongPress(InputKey key, Player player){
        if (!isWearingPowerArmor(player)) return;
        var bool = key == USE;
        if(bool){
            getPlayerPowerArmor(player).addAttackCharge();
        }
    }
}
