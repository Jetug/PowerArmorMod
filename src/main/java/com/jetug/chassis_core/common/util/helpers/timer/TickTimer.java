package com.jetug.chassis_core.common.util.helpers.timer;

import java.util.ArrayList;

public class TickTimer {
    private final ArrayList<TimerTask> tasks = new ArrayList<>();

    public void tick() {
        ArrayList<TimerTask> removed = new ArrayList<>();

        for (TimerTask task : tasks) {
            task.tick();
            if (task.isCompleted())
                removed.add(task);
        }

        tasks.removeAll(removed);
    }

    public void addTimer(TimerTask task) {
        tasks.add(task);
    }

    public void addCooldownTimer(int duration, Runnable predicate) {
        tasks.add(new PlayOnceTimerTask(duration, predicate));
    }

}

