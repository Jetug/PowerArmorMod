package com.jetug.power_armor_mod.common.util.helpers.timer;

import com.jetug.power_armor_mod.common.util.interfaces.SimpleAction;

import java.util.ArrayList;

public class TickTimer {
    private final ArrayList<TimerTask> tasks = new ArrayList<>();

    public void tick(){
        ArrayList<TimerTask> removed = new ArrayList<>();

        for (TimerTask task: tasks) {
            task.tick();
            if (task.isCompleted())
                removed.add(task);
        }

        tasks.removeAll(removed);
    }

    public void addTimer(TimerTask task){
        tasks.add(task);
    }

    public void addCooldownTimer(int duration, SimpleAction predicate){
        tasks.add(new PlayOnceTimerTask(duration, predicate));
    }

}

