package com.jetug.power_armor_mod.common.events;

import com.jetug.power_armor_mod.common.foundation.entity.PowerArmorEntity;
import com.jetug.power_armor_mod.common.util.constants.Global;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.jetug.power_armor_mod.common.util.extensions.PlayerExtension.*;

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

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        if(event.getEntity() instanceof Player player && isWearingPowerArmor(player)){
            var entity = (PowerArmorEntity)player.getVehicle();
            entity.hurt(event.getSource(), event.getAmount());
            event.setCanceled(true);
        }
    }
}