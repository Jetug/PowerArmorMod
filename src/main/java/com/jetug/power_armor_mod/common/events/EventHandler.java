package com.jetug.power_armor_mod.common.events;

import com.jetug.power_armor_mod.common.entity.data.IPlayerData;
import com.jetug.power_armor_mod.common.entity.data.PlayerDataProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class EventHandler {
    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event){
//        if(event.getObject() instanceof PlayerEntity){
//            if(!((PlayerEntity) event.getObject()).getCapability(PlayerDataProvider.PLAYER_DATA).isPresent()){
//                event.addCapability(new ResourceLocation(MOD_ID, "properties"), new PlayerDataProvider());
//            }
//        }
    }

    @SubscribeEvent
    public void onPlayerLogsIn(PlayerEvent.PlayerLoggedInEvent event) {
        PlayerEntity player = event.getPlayer();
        IPlayerData data = (IPlayerData) player.getCapability(PlayerDataProvider.PLAYER_DATA, null);

        String message;
        if (data.getIsInPowerArmor())
            message = "you are in PA";
        else
            message = "you are not in PA";

        player.sendMessage(new StringTextComponent(message), player.getUUID());
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event){

        event.getPlayer().getCapability(PlayerDataProvider.PLAYER_DATA).ifPresent(oldStore->{
            event.getOriginal().getCapability(PlayerDataProvider.PLAYER_DATA).ifPresent(newStore ->{
                newStore.copyFrom(oldStore);
            });
        });
    }

}
