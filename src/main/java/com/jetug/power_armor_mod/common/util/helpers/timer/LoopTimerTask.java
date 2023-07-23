package com.jetug.power_armor_mod.common.util.helpers.timer;



public class LoopTimerTask implements TimerTask {
    private final Runnable predicate;

    public LoopTimerTask(Runnable predicate) {
        this.predicate = predicate;
    }

    @Override
    public void tick() {
        predicate.run();
    }

    @Override
    public boolean isCompleted() {
        return false;
    }
}
