package com.jetug.power_armor_mod.client.events;

import com.jetug.power_armor_mod.common.minecraft.entity.PowerArmorEntity;
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
        int key = event.getKey();
        if(player == null) return;
        Entity entity = player.getVehicle();

        if(isPressed(key, options.keyJump)){
            if(isWearingPowerArmor(player) && entity != null){
                ((PowerArmorEntity) entity).jump();
                LOGGER.log(Level.DEBUG, "jump");
                out.println("Jump!");
            }
        }
        else if(isPressed(key, DASH) ){
            if(isWearingPowerArmor(player) && entity != null){
                ((PowerArmorEntity) entity).dash();
                out.println("Dash!");

                List<Entity> entities = Minecraft.getInstance().level.getEntitiesOfClass(Entity.class,
                        new AxisAlignedBB(player.position().x, player.position().y, player.position().z,
                                player.position().x + 20, 255, player.position().x + 20));

                for (Entity item: entities) {
                    if (item != entity && item != player){
                        item.hurt(DamageSource.ANVIL, 20);
                        item.kill();
                        //entity.hurt(DamageSource.ANVIL, 20);
                    }
                }
            }
        }
        else {

        }
    }

    private static boolean isPressed(int key, KeyBinding keyBinding){
        return key == keyBinding.getKey().getValue();
    }
}
