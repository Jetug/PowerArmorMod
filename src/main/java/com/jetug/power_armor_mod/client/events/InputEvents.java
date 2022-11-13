package com.jetug.power_armor_mod.client.events;

import com.jetug.power_armor_mod.common.minecraft.entity.PowerArmorEntity;
import com.jetug.power_armor_mod.common.util.enums.DashDirection;
import com.jetug.power_armor_mod.common.util.helpers.DoubleClickHelper;
import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.Level;
import org.lwjgl.glfw.GLFW;

import static com.jetug.power_armor_mod.common.util.constants.Global.LOGGER;
import static com.jetug.power_armor_mod.client.KeyBindings.*;
import static com.jetug.power_armor_mod.common.util.extensions.Key.isEqual;
import static com.jetug.power_armor_mod.common.util.extensions.PlayerExtension.isWearingPowerArmor;
import static java.lang.System.out;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class InputEvents {
    static DoubleClickHelper doubleClickHelper = new DoubleClickHelper();
    
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent()
    public static void onKeyInput(InputEvent.KeyInputEvent event)
    {
        if (event.getAction() == GLFW.GLFW_PRESS) {
            Minecraft minecraft = Minecraft.getInstance();
            PlayerEntity player = minecraft.player;
            GameSettings options = minecraft.options;

            if (player == null) return;
            if (!isWearingPowerArmor(player)) return;

            Entity entity = player.getVehicle();
            int key = event.getKey();

            if(doubleClickHelper.isDoubleClick(key)) {
                assert entity != null;

                if (options.keyUp.isDown()) {
                    ((PowerArmorEntity) entity).dash(DashDirection.FORWARD);
                }

                if (options.keyDown.isDown()) {
                    ((PowerArmorEntity) entity).dash(DashDirection.BACK);
                }

                if (options.keyLeft.isDown()) {
                    ((PowerArmorEntity) entity).dash(DashDirection.LEFT);
                }

                if (options.keyRight.isDown()) {
                    ((PowerArmorEntity) entity).dash(DashDirection.RIGHT);
                }

                if (options.keyJump.isDown()) {
                    ((PowerArmorEntity) entity).dash(DashDirection.UP);
                }
            }

            if (isEqual(key, options.keyJump)) {
                onJump(entity);
            }
            else if (LEAVE.isDown()){
                player.stopRiding();
            }

            if (options.keyShift.isDown() && player.getVehicle() instanceof PowerArmorEntity) {
                minecraft.options.keyShift.setDown(false);
                //onDash(entity);
            }
        }
    }

    private static void onJump(Entity entity){
        ((PowerArmorEntity)entity).jump();
        LOGGER.log(Level.DEBUG, "jump");
        out.println("Jump!");
    }

    private static void onDash(Entity entity){
        GameSettings options = Minecraft.getInstance().options;

        if (options.keyDown.isDown()){
            ((PowerArmorEntity)entity).dash(DashDirection.BACK);
        }
        else if (options.keyRight.isDown()){
            ((PowerArmorEntity)entity).dash(DashDirection.RIGHT);
        }
        else if (options.keyLeft.isDown()){
            ((PowerArmorEntity)entity).dash(DashDirection.LEFT);
        }
        else {
            ((PowerArmorEntity) entity).dash(DashDirection.FORWARD);
        }
        out.println("Dash!");
    }

    
}
