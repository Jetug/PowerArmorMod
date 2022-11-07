package com.jetug.power_armor_mod.common.util.helpers.timer;

import com.jetug.power_armor_mod.common.util.interfaces.SimplePredicate;

public class PlayOnceTimerTask implements TimerTask {
    private final SimplePredicate predicate;
    private int ticks;
    private boolean isCompleted = false;

    public PlayOnceTimerTask(SimplePredicate predicate, int ticks) {
        this.predicate = predicate;
        this.ticks = ticks;
    }

    @Override
    public boolean isCompleted() {
        return isCompleted;
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
