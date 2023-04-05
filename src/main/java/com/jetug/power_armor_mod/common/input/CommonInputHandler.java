package com.jetug.power_armor_mod.common.input;

import com.jetug.power_armor_mod.common.foundation.entity.PowerArmorEntity;
import com.jetug.power_armor_mod.common.util.extensions.PlayerExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.InputEvent;
import org.lwjgl.glfw.GLFW;

import static com.jetug.power_armor_mod.client.ClientConfig.*;
import static com.jetug.power_armor_mod.client.KeyBindings.*;
import static com.jetug.power_armor_mod.client.input.InputHandler.*;
import static com.jetug.power_armor_mod.common.util.extensions.PlayerExtension.*;

public class CommonInputHandler {
    public static void onArmorKeyInput(Keys key, int action, Player player) {
        if (isWearingPowerArmor(player)) {
            var entity = getPlayerPowerArmor(player);
            if (OPTIONS.keyJump.isDown()) entity.jump();

            switch (action) {
                case GLFW.GLFW_PRESS:
                    if (key == Keys.LEAVE) {
                        stopWearingArmor(player);
                    }
                    break;
                case GLFW.GLFW_RELEASE:
                    onRelease(key, player);
                    break;
            }
        }
    }

    public static void onRelease(Keys key, Player player){
        if (isWearingPowerArmor()) {
            var options = Minecraft.getInstance().options;

//            if(keysEqual(key, options.keyUse, options.keyAttack) ){
//                var pa = PlayerExtension.getPlayerPowerArmor();
//                pa.resetAttackCharge();
//            }
        }
    }

}
