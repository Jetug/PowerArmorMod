package com.jetug.power_armor_mod.client.events;

import com.jetug.power_armor_mod.common.minecraft.entity.PowerArmorEntity;
import com.jetug.power_armor_mod.common.util.constants.Global;
import com.jetug.power_armor_mod.common.util.enums.DashDirection;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.InputEvent;
import org.apache.logging.log4j.Level;

import static com.jetug.power_armor_mod.common.util.extensions.PlayerExtension.isWearingPowerArmor;
import static java.util.logging.Level.INFO;

public class InputController {

    public static void onDoubleClick(InputEvent.KeyInputEvent event){
        var minecraft = Minecraft.getInstance();
        var player = minecraft.player;
        var key = event.getKey();

        if (player != null && isWearingPowerArmor(player)) {
            var entity = (PowerArmorEntity) player.getVehicle();
            assert entity != null;
            var options = Minecraft.getInstance().options;

            if (key == options.keyUp.getKey().getValue()) {
                entity.dash(DashDirection.FORWARD);
            }
            if (key == options.keyDown.getKey().getValue()) {
                entity.dash(DashDirection.BACK);
            }
            if (key == options.keyLeft.getKey().getValue()) {
                entity.dash(DashDirection.LEFT);
            }
            if (key == options.keyRight.getKey().getValue()) {
                entity.dash(DashDirection.RIGHT);
            }
            if (key == options.keyJump.getKey().getValue()) {
                entity.dash(DashDirection.UP);
            }
        }
    }

    public static void onRepeat(InputEvent.KeyInputEvent event, int ticks){
        var minecraft = Minecraft.getInstance();
        var player = minecraft.player;
        var key = event.getKey();

        if (player != null && isWearingPowerArmor(player)) {
            var entity = (PowerArmorEntity) player.getVehicle();
            assert entity != null;
            var options = Minecraft.getInstance().options;
            Global.LOGGER.log(Level.INFO, "onRepeat: ticks:" + ticks);
        }
    }
}
