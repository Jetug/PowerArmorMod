package com.jetug.power_armor_mod.client.events;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;

public class ContainerEvents
{
    @SubscribeEvent
    public static void initContainers(RegistryEvent.Register<ContainerType<?>> evt)
    {
        IForgeRegistry<ContainerType<?>> r = evt.getRegistry();


        DistExecutor.runWhenOn(Dist.CLIENT, () -> () ->
        {
            ScreenManager.registerFactory(extendedInventory, GuiExtendedInventory::new);
        });
    }
}
