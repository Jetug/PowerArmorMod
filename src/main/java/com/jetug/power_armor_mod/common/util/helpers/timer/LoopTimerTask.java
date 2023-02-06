package com.jetug.power_armor_mod.common.util.helpers.timer;

import com.jetug.power_armor_mod.common.util.interfaces.SimplePredicate;

public class LoopTimerTask implements TimerTask {
    private final SimplePredicate predicate;
    private boolean isCompleted = false;

    public LoopTimerTask(SimplePredicate predicate) {
        this.predicate = predicate;
    }

    public void reset() {
        isCompleted = false;
    }

    @Override
    public boolean isCompleted() {
        return isCompleted;
    }

    @Override
    public void tick() {
        predicate.execute();
    }
}
