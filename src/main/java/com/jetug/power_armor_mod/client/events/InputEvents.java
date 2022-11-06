package com.jetug.power_armor_mod.client.events;

import com.jetug.power_armor_mod.common.minecraft.entity.PowerArmorEntity;
import com.jetug.power_armor_mod.common.util.enums.DashDirection;
import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.Level;
import software.bernie.shadowed.eliotlash.mclib.math.functions.limit.Min;

import java.util.List;

import static com.jetug.power_armor_mod.PowerArmorMod.LOGGER;
import static com.jetug.power_armor_mod.client.KeyBindings.*;
import static com.jetug.power_armor_mod.common.util.extensions.PlayerExtension.isWearingPowerArmor;
import static java.lang.System.out;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class InputEvents {

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent()
    public static void onKeyInput(InputEvent.KeyInputEvent event)
    {
        Minecraft minecraft = Minecraft.getInstance();
        PlayerEntity player = minecraft.player;
        GameSettings options = minecraft.options;
        Entity entity = player.getVehicle();
        int key = event.getKey();


        if(isPressed(key, options.keyJump)){
            onJump(player, entity);
        }
        else if(isPressed(key, DASH) ){
            onDash(player, entity);
        }
        else {

        }
    }

    private static void onJump(PlayerEntity player, Entity entity){
        if(isWearingPowerArmor(player) && entity != null){
            ((PowerArmorEntity)entity).jump();
            LOGGER.log(Level.DEBUG, "jump");
            out.println("Jump!");
        }
    }

    private static void onDash(PlayerEntity player, Entity entity){
        if (!isWearingPowerArmor(player) || entity == null)
            return;

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

    private static boolean isPressed(int key, KeyBinding keyBinding){
        return key == keyBinding.getKey().getValue();
    }
}
