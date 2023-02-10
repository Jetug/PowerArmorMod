package com.jetug.power_armor_mod.client.input;

import com.jetug.power_armor_mod.common.minecraft.entity.PowerArmorEntity;
import com.jetug.power_armor_mod.common.util.constants.Global;
import com.jetug.power_armor_mod.common.util.enums.DashDirection;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.InputEvent;
import org.apache.logging.log4j.Level;

import static com.jetug.power_armor_mod.client.ClientConfig.OPTIONS;
import static com.jetug.power_armor_mod.client.ClientConfig.getLocalPlayer;
import static com.jetug.power_armor_mod.common.util.extensions.PlayerExtension.*;

public class InputController {

    public static void onDoubleClick(InputEvent.KeyInputEvent event){
        if (getLocalPlayer() == null || !isWearingPowerArmor()) return;

        var key = event.getKey();
        var armorEntity = (PowerArmorEntity) getLocalPlayer().getVehicle();
        DashDirection direction = null;

        if (keysEqual(key, OPTIONS.keyUp)) {
            direction = DashDirection.FORWARD;
        }
        else if (keysEqual(key, OPTIONS.keyDown)) {
            direction = DashDirection.BACK;
        }
        else if (keysEqual(key, OPTIONS.keyLeft)) {
            direction = DashDirection.LEFT;
        }
        else if (keysEqual(key, OPTIONS.keyRight)) {
            direction = DashDirection.RIGHT;
        }
        else if (keysEqual(key, OPTIONS.keyJump)) {
            direction = DashDirection.UP;
        }
        else return;

        armorEntity.dash(direction);
    }

    public static boolean keysEqual(int key1, KeyMapping key2){
        return key1 == key2.getKey().getValue();
    }

    public static void onRepeat(int key, int ticks){
        var minecraft = Minecraft.getInstance();
        var player = minecraft.player;

        if (player != null && isWearingPowerArmor(player)) {
            var entity = (PowerArmorEntity) player.getVehicle();
            assert entity != null;
            var options = Minecraft.getInstance().options;
            Global.LOGGER.log(Level.INFO, "onRepeat: ticks:" + ticks);
        }
    }

    public static void onLongRelease(int key, int ticks){
        var minecraft = Minecraft.getInstance();
        var player = minecraft.player;

        if (player != null && isWearingPowerArmor(player)) {
            var entity = (PowerArmorEntity) player.getVehicle();
            assert entity != null;
            var options = Minecraft.getInstance().options;
            Global.LOGGER.log(Level.INFO, "onRepeat: ticks:" + ticks);
        }
    }
}
