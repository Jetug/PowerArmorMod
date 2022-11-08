package com.jetug.power_armor_mod.common.util.helpers;

import com.jetug.power_armor_mod.PowerArmorMod;
import com.jetug.power_armor_mod.client.render.ResourceHelper;
import com.jetug.power_armor_mod.common.capability.data.ArmorDataProvider;
import com.jetug.power_armor_mod.common.capability.data.PlayerDataProvider;
import com.jetug.power_armor_mod.common.network.PacketHandler;
import com.jetug.power_armor_mod.common.util.helpers.timer.PlayOnceTimerTask;
import com.jetug.power_armor_mod.common.util.helpers.timer.TickTimer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.Timer;

public class DoubleClickHelper{
    private KeyBinding key1;

    private static final int maxTicks = 10;

    private int ticks = maxTicks;

    public DoubleClickHelper(){
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::onTick);
    }

    private void onTick(final TickEvent.ClientTickEvent event) {
        if (key1 != null)
            ticks -= 1;
    }

    public boolean onClick(KeyBinding key){
        if (key1 == null || key.getKey().getValue() != key1.getKey().getValue()){
            key1 = key;
            return false;
        }
        else if(key.getKey().getValue() == key1.getKey().getValue()){
            key1 = null;
            ticks = maxTicks;
            return true;
        }

        return false;
    }

    public void isDoublePressed(KeyBinding key){

    }
}
