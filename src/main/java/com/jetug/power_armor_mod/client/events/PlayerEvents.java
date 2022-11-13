package com.jetug.power_armor_mod.client.events;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderArmEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.jetug.power_armor_mod.common.util.extensions.PlayerExtension.isWearingPowerArmor;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class PlayerEvents
{
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onRenderPlayer(RenderPlayerEvent.Pre event)
    {
        Player player = event.getPlayer();
        if(isWearingPowerArmor(player)){
            player.setInvisible(true);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent()
    public static void onAttack(AttackEntityEvent event)
    {
        var player = event.getPlayer();
        var target = event.getTarget();

        if(isWearingPowerArmor(player)){
            Vec3 vc = player.getViewVector(1.0F);
            target.push(vc.x * 20, vc.y * 0, vc.z * 20);
        }
    }


    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent()
    public static void onHandRender(RenderArmEvent event)
    {

    }

//    @OnlyIn(Dist.CLIENT)
//    @SubscribeEvent()
//    public static void onItemRender( event)
//    {
//
//    }

//    @OnlyIn(Dist.CLIENT)
//    @SubscribeEvent()
//    public static void onCollide( event)
//    {
//        Player player = event.getPlayer();
//        Entity target = event.getTarget();
//
//        if(isWearingPowerArmor(player)){
//            Vec3 vc = player.getViewVector(1.0F);
//            target.push(vc.x * 20, vc.y * 20, vc.z * 20);
//        }
//    }
}
