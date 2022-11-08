package com.jetug.power_armor_mod.common.util.helpers;

import com.jetug.power_armor_mod.common.util.helpers.timer.PlayOnceTimerTask;
import com.jetug.power_armor_mod.common.util.helpers.timer.TickTimer;
import net.minecraft.client.settings.KeyBinding;

import java.util.Timer;

public class DoubleClickHelper {
    private TickTimer timer;
    private KeyBinding key1;
    private KeyBinding key2;
    private PlayOnceTimerTask task = new PlayOnceTimerTask(5, () ->{
        key1 = null;
    });

    public DoubleClickHelper(TickTimer timer){
        this.timer = timer;
    }

    public boolean onClick(KeyBinding key){
        if (key1 == null || key.getKey().getValue() != key1.getKey().getValue()){
            key1 = key;
            timer.addTimer(task);
            return false;
        }
        else if(key.getKey().getValue() == key1.getKey().getValue()){
            key1 = null;
            return true;
        }

        return false;
    }

    public void isDoublePressed(KeyBinding key){

    }
}
