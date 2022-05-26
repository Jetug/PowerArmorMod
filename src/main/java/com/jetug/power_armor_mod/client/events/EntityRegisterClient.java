package com.jetug.power_armor_mod.client.events;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.jetug.power_armor_mod.common.util.extensions.PlayerExtension.isWearingPowerArmor;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class EntityRegisterClient
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
    @SubscribeEvent
    public static void onRenderLiving(RenderLivingEvent event)
    {
//        Entity entity = event.getEntity();
//
//        if(entity instanceof PowerArmorEntity){
//            event.getRenderer()
//            ((PowerArmorEntity)entity).
//        }
    }
}
