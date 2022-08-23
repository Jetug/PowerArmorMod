package com.jetug.power_armor_mod.client.events;

import com.jetug.power_armor_mod.common.minecraft.entity.PowerArmorEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.jetug.power_armor_mod.common.util.extensions.PlayerExtension.*;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class PlayerEvents
{
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onRenderPlayer(RenderPlayerEvent.Pre event)
    {
        PlayerEntity player = event.getPlayer();
//        if(isWearingPowerArmor(player)){
//            player.setInvisible(true);
//        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent()
    public void onEvent(InputEvent.KeyInputEvent event)
    {
        int key = event.getKey();
        PlayerEntity player = Minecraft.getInstance().player;
        player.sendMessage(new StringTextComponent("" + key), player.getUUID());

        if(key == 57){

            Entity entity = player.getVehicle();

            if(player.isPassenger() && entity instanceof PowerArmorEntity){
                ((PowerArmorEntity) entity).jump();
            }

        }

        // Проверка, сработает при нажатии любой клавиши
        System.out.println(key);
    }
}
