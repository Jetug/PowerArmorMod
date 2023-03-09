package com.jetug.power_armor_mod.common.util.helpers.timer;

import com.jetug.power_armor_mod.common.util.interfaces.SimpleAction;

public class LoopTimerTask implements TimerTask {
    private final SimpleAction predicate;

    public LoopTimerTask(SimpleAction predicate) {
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
