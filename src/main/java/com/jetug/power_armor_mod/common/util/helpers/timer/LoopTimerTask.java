package com.jetug.power_armor_mod.common.util.helpers.timer;

import com.jetug.power_armor_mod.common.util.interfaces.SimplePredicate;

public class LoopTimerTask implements TimerTask {
    private final SimplePredicate predicate;

    public LoopTimerTask(SimplePredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public void tick() {
        predicate.execute();
    }

    @Override
    public boolean isCompleted() {
        return false;
    }
}
