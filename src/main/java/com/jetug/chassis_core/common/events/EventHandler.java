package com.jetug.chassis_core.common.events;

import com.jetug.chassis_core.Global;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class EventHandler {
    @SubscribeEvent
    public static void onTick(TickEvent event) {
        switch (event.type){
            case WORLD:
                break;
            case PLAYER:
                break;
            case CLIENT:
                Global.CLIENT_TIMER.tick();
                break;
            case SERVER:
                break;
            case RENDER:
                break;
        }
    }

    @SubscribeEvent()
    public static void onAttack(AttackEntityEvent event) {
        var player = event.getPlayer();
        var target = event.getTarget();

//        if(isWearingChassis(player))
//            getPlayerChassis(player).powerPunch();
    }

//    @SubscribeEvent
//    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
//        var world = event.getWorld();
//        var pos = event.getPos();
//        var player = event.getPlayer();
//        var state = world.getBlockState(pos);
//        var blockEntity = world.getBlockEntity(pos);
//
//        if (blockEntity instanceof ArmorStationBlockEntity stationBlockEntity) {
//            stationBlockEntity.openGui(player);
//        }
//    }

    @SuppressWarnings("ConstantConditions")
    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
//        if(event.getEntity() instanceof Player player && isWearingChassis(player)){
//            var damage = ((WearableChassis)player.getVehicle()).getPlayerDamageValue(event.getSource(), event.getAmount());
//            player.hurt(event.getSource(), damage);
//            event.setCanceled(true);
//        }
    }
}