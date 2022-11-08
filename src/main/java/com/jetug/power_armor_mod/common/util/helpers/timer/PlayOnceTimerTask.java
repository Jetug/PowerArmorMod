package com.jetug.power_armor_mod.common.util.helpers.timer;

import com.jetug.power_armor_mod.common.util.interfaces.SimplePredicate;
import com.sun.org.apache.bcel.internal.generic.RETURN;

public class PlayOnceTimerTask implements TimerTask {
    private final SimplePredicate predicate;
    private final int initTicks;
    private int ticks;
    private boolean isCompleted = false;

    public PlayOnceTimerTask(int ticks, SimplePredicate predicate) {
        this.predicate = predicate;
        this.initTicks = ticks;
        this.ticks = ticks;
    }

    public void reset() {
        ticks = initTicks;
        isCompleted = false;
    }

    @Override
    public boolean isCompleted() {
        return isCompleted;
        //return ticks <= 0;
    }

    @Override
    public void tick() {
        if (ticks == 0) {
            predicate.execute();
            isCompleted = true;
        }
        ticks -= 1;
    }
}
