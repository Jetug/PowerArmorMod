package com.jetug.power_armor_mod.common.events;

import com.jetug.power_armor_mod.common.data.constants.Global;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.EventPriority;

import static java.lang.System.out;

@Mod.EventBusSubscriber(modid = Global.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CustomHandler {

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onEntityContainerChange(ContainerChangedEvent event) {
        var entity = event.getEntity();
        out.println(entity);
    }

//    @SubscribeEvent
//    public static void onPlayerInteractEntity(PlayerInteractEvent.EntityInteract event) {
//        Player player = event.getPlayer();
//        Entity entity = event.getTarget();
//
//        var t = entity;
//        // Ваш код для работы с сущностью, на которую игрок смотрит
//    }

}