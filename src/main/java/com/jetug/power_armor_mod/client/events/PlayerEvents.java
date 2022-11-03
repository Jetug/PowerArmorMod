package com.jetug.power_armor_mod.client.events;

import com.jetug.power_armor_mod.common.minecraft.entity.PowerArmorEntity;
import net.minecraft.advancements.criterion.PlayerHurtEntityTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
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

    //@OnlyIn(Dist.CLIENT)
    @SubscribeEvent()
    public static void onAttack(AttackEntityEvent event)
    {
        PlayerEntity player = event.getPlayer();
        Entity target = event.getTarget();

        if(isWearingPowerArmor(player)){
            Vector3d vc = player.getViewVector(1.0F);
            target.push(vc.x * 20, vc.y * 20, vc.z * 20);
        }
    }

}
