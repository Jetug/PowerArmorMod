package com.jetug.example.client.events;

import com.jetug.chassis_core.ChassisCore;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.jetug.example.client.ExampleChassisRenderer;

import static com.jetug.example.common.registery.EntityTypes.EXAMPLE_CHASSIS;

@Mod.EventBusSubscriber(modid = ChassisCore.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class SetupEvents {
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent()
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(EXAMPLE_CHASSIS.get(), ExampleChassisRenderer::new);
    }

}
