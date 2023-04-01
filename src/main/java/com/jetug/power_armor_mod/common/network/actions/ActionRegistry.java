package com.jetug.power_armor_mod.common.network.actions;

import java.util.*;

@SuppressWarnings("ALL")
public class ActionRegistry {
    private static int currentId = 0;
    private static Map<Integer, Action> actions = new HashMap<>();
    private static Map<Class, Integer> actionsId = new HashMap<>();

    static {
        addAction(new DashAction());
        addAction(new InventorySyncAction());
    }

    public static Action getAction(int id){
        return actions.get(id);
    }

    public static int getActionId(Class type){
        return actionsId.get(type);
    }

    private static <T extends Action> void addAction(T action){
        actions.put(action.getId(), action);
        actionsId.put(action.getClass(), currentId++);
    }

}
