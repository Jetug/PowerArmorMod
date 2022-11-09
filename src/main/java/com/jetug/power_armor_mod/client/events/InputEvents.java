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

                if (isEqual(key, options.keyUp)) {
                    ((PowerArmorEntity) entity).dash(DashDirection.FORWARD);
                }

                if (isEqual(key, options.keyDown)) {
                    ((PowerArmorEntity) entity).dash(DashDirection.BACK);
                }

                if (isEqual(key, options.keyLeft)) {
                    ((PowerArmorEntity) entity).dash(DashDirection.LEFT);
                }

                if (isEqual(key, options.keyRight)) {
                    ((PowerArmorEntity) entity).dash(DashDirection.RIGHT);
                }

                if (isEqual(key, options.keyJump)) {
                    ((PowerArmorEntity) entity).dash(DashDirection.UP);
                }
            }

            if (isEqual(key, options.keyJump)) {
                onJump(entity);
            }
            else if(isEqual(key, DASH) ){
                onDash(entity);
            }
        }
    }

    public static void ttt(InputEvent event)
    {

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
