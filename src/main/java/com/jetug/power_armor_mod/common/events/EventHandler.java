package com.jetug.power_armor_mod.common.events;

import com.jetug.power_armor_mod.common.capability.data.ArmorDataProvider;
import com.jetug.power_armor_mod.common.capability.data.IArmorPartData;
import com.jetug.power_armor_mod.common.capability.data.IPlayerData;
import com.jetug.power_armor_mod.common.capability.data.PlayerDataProvider;
import com.jetug.power_armor_mod.common.entity.entitytype.PowerArmorEntity;
import com.jetug.power_armor_mod.common.entity.entitytype.PowerArmorPartEntity;
import com.jetug.power_armor_mod.common.network.PacketHandler;
import com.jetug.power_armor_mod.common.network.packet.ArmorPartPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class EventHandler {
    @SubscribeEvent
    public static void onPlayerLogsIn(PlayerEvent.PlayerLoggedInEvent event) {
//        PlayerEntity player = event.getPlayer();
//        IPlayerData data = (IPlayerData) player.getCapability(PlayerDataProvider.PLAYER_DATA, null);
//
//        String message;
//        if (data.getIsInPowerArmor())
//            message = "you are in PA";
//        else
//            message = "you are not in PA";
//
//        player.sendMessage(new StringTextComponent(message), player.getUUID());
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event){
        event.getPlayer().getCapability(PlayerDataProvider.PLAYER_DATA).ifPresent(oldStore->{
            event.getOriginal().getCapability(PlayerDataProvider.PLAYER_DATA).ifPresent(newStore ->{
                newStore.copyFrom(oldStore);
            });
        });
    }

    @SubscribeEvent
    public static void onTrack(final PlayerEvent.StartTracking event) {
        final Entity entity = event.getTarget();
        final PlayerEntity playerEntity = event.getPlayer();
        if (playerEntity instanceof ServerPlayerEntity) {
            if(entity instanceof PowerArmorEntity){
//                for (PowerArmorPartEntity part : ((PowerArmorEntity) entity).subEntities) {
//                    part.getCapability(ArmorDataProvider.POWER_ARMOR_PART_DATA).ifPresent(capability ->
//                            PacketHandler.sendTo( new ArmorPartPacket(capability),(ServerPlayerEntity) playerEntity)
//                    );
//                }

                entity.getCapability(ArmorDataProvider.POWER_ARMOR_PART_DATA).ifPresent(capability ->
                        PacketHandler.sendTo( new ArmorPartPacket(capability),(ServerPlayerEntity) playerEntity)
                );
            }


        }
    }
}
