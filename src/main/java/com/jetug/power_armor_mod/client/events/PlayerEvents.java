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
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.Field;
import java.util.Map;

import static com.jetug.power_armor_mod.common.util.extensions.PlayerExtension.*;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class PlayerEvents
{
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onRenderPlayer(RenderPlayerEvent.Pre event)
    {
        PlayerEntity player = event.getPlayer();
        if(isWearingPowerArmor(player)){
            player.setInvisible(true);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent()
    public static void onEvent(InputEvent.KeyInputEvent event)
    {
        int key = event.getKey();
        PlayerEntity player = Minecraft.getInstance().player;
        //player.sendMessage(new StringTextComponent("" + key), player.getUUID());

        if(key == Minecraft.getInstance().options.keyJump.getKey().getValue()){

            Entity entity = player.getVehicle();

            if(player.isPassenger() && entity instanceof PowerArmorEntity){
                ((PowerArmorEntity) entity).jump();
            }

        }

        // Проверка, сработает при нажатии любой клавиши
        System.out.println(key);
    }

//    private static Field KEYBIND_ARRAY = null;
//
//    @SubscribeEvent (priority = EventPriority.LOWEST)
//    public static void onClientTick(TickEvent.ClientTickEvent event) throws Exception {
//        PlayerEntity player = Minecraft.getInstance().player;
//
//        if(KEYBIND_ARRAY == null){
//            KEYBIND_ARRAY = KeyBinding.class.getDeclaredField("KEYBIND_ARRAY");
//            KEYBIND_ARRAY.setAccessible(true);
//        }
//        if(event.phase.equals(TickEvent.Phase.END)){
//            Map<String, KeyBinding> binds = (Map<String, KeyBinding>) KEYBIND_ARRAY.get(null);
//            for (String bind : binds.keySet()) {
//                if(binds.get(bind).isDown()){
//                    String str = bind + " - " + binds.get(bind).getName();
//                    System.out.println(str);
//
//                    player.sendMessage(new StringTextComponent(str), player.getUUID());
//                    break;
//                }
//            }
//        }
//    }
}
