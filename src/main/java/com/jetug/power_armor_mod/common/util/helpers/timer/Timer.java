package com.jetug.power_armor_mod.common.util.helpers.timer;

import net.minecraftforge.event.TickEvent;

import java.util.ArrayList;

public class Timer {
    private static final Timer INSTANCE = new Timer();
    private final ArrayList<TimerTask> clientTasks = new ArrayList<>();

    private Timer() {}

    public static Timer getInstance() {
        return INSTANCE;
    }

    public void tick(TickEvent.Type type){
        ArrayList<TimerTask> tasks = new ArrayList<>();

        switch (type){
            case WORLD:
                break;
            case PLAYER:
                break;
            case CLIENT:
                tasks = clientTasks;
                break;
            case SERVER:
                break;
            case RENDER:
                break;
        }



        for (TimerTask task: tasks) {
            if (task.isCompleted())
                tasks.remove(task);
            else
                task.tick();
        }
    }

    public void addTimer(TimerTask task, TickEvent.Type type){
        addTask(task, type);
    }

    public void addRepeatingTimer(){

    }

    private void addTask(TimerTask task, TickEvent.Type type){
        switch (type){
            case WORLD:
                break;
            case PLAYER:
                break;
            case CLIENT:
                clientTasks.add(task);
                break;
            case SERVER:
                break;
            case RENDER:
                break;
        }
    }
}

