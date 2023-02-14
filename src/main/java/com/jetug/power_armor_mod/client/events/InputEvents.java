package com.jetug.power_armor_mod.client.events;

import com.jetug.power_armor_mod.common.foundation.entity.PowerArmorEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import static com.jetug.power_armor_mod.client.ClientConfig.*;
import static com.jetug.power_armor_mod.client.KeyBindings.*;
import static com.jetug.power_armor_mod.common.util.extensions.PlayerExtension.*;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class InputEvents {
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent()
    public static void onKeyInput(InputEvent.KeyInputEvent event)
    {
        if (isWearingPowerArmor()) {
            var entity = (PowerArmorEntity)getLocalPlayer().getVehicle();
            assert entity != null;
            onArmorKeyInput(event, entity);
        }
    }

    private static void onArmorKeyInput(InputEvent.KeyInputEvent event, PowerArmorEntity entity) {
        if (OPTIONS.keyJump.isDown()) entity.jump();
        if (OPTIONS.keyShift.isDown()) OPTIONS.keyShift.setDown(false);

        if (event.getAction() == GLFW.GLFW_PRESS) {
            if (LEAVE.isDown()) {
                stopWearingArmor(getLocalPlayer());
            }
        }
    }


}
