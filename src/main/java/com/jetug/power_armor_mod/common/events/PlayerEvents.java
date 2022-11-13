package com.jetug.power_armor_mod.common.events;

import com.jetug.power_armor_mod.common.capability.providers.ArmorDataProvider;
import com.jetug.power_armor_mod.common.capability.providers.PlayerDataProvider;
import com.jetug.power_armor_mod.common.minecraft.entity.PowerArmorEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class PlayerEvents {
    @SubscribeEvent
    public static void onPlayerLogsIn(PlayerEvent.PlayerLoggedInEvent event){}

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event){
        event.getPlayer().getCapability(PlayerDataProvider.PLAYER_DATA).ifPresent(oldStore->{
            event.getOriginal().getCapability(PlayerDataProvider.PLAYER_DATA).ifPresent(newStore ->{
                newStore.copyFrom(oldStore);
            });
        });
    }

    @SubscribeEvent
    public static void onTrack(final PlayerEvent.StartTracking event){
        final Entity entity = event.getTarget();
        final Player playerEntity = event.getPlayer();
        if (playerEntity instanceof ServerPlayer) {
            if(entity instanceof PowerArmorEntity){
                entity.getCapability(ArmorDataProvider.POWER_ARMOR_PART_DATA).ifPresent(capability ->
                        capability.syncWithClient((ServerPlayer)playerEntity)
                );
            }
        }
    }
}
