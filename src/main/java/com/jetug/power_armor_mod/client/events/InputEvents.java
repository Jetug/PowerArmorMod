package com.jetug.power_armor_mod.client.events;

import com.jetug.power_armor_mod.client.input.LongClickController;
import com.jetug.power_armor_mod.common.minecraft.entity.PowerArmorEntity;
import com.jetug.power_armor_mod.common.util.enums.ActionType;
import com.jetug.power_armor_mod.client.input.DoubleClickController;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import static com.jetug.power_armor_mod.client.KeyBindings.LEAVE;
import static com.jetug.power_armor_mod.common.network.PacketSender.doServerAction;
import static com.jetug.power_armor_mod.common.util.extensions.PlayerExtension.isWearingPowerArmor;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class InputEvents {
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent()
    public static void onKeyInput(InputEvent.KeyInputEvent event)
    {
        var minecraft = Minecraft.getInstance();
        var player = minecraft.player;
        var options = minecraft.options;

        if (player != null && isWearingPowerArmor(player)) {
            var entity = (PowerArmorEntity)player.getVehicle();
            assert entity != null;

            if (options.keyJump.isDown()) entity.jump();
            if (options.keyShift.isDown()) options.keyShift.setDown(false);

            if (event.getAction() == GLFW.GLFW_PRESS) {

//                if (DOUBLE_CLICK_CONTROLLER.isDoubleClick(event.getKey()))
//                    onDoubleClick(entity, event.getKey());

                if (LEAVE.isDown()) {
                    player.stopRiding();
                    doServerAction(ActionType.DISMOUNT);
                    player.setInvisible(false);
                }
            }
        }
    }
}
