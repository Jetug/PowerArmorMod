package com.jetug.chassis_core.common.network;

import com.jetug.chassis_core.common.network.actions.Action;
import com.jetug.chassis_core.common.network.actions.CastingStatusAction;
import com.jetug.chassis_core.common.network.actions.InputAction;
import com.jetug.chassis_core.common.network.actions.InventorySyncAction;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("ALL")
public class ActionRegistry {
    private static int currentId = 0;
    private static Map<Integer, Action> actions = new HashMap<>();
    private static Map<Class, Integer> actionsId = new HashMap<>();

    static {
        addAction(new InventorySyncAction());
        addAction(new InputAction());
        addAction(new CastingStatusAction());
    }

    public static Action getAction(int id){
        return actions.get(id);
    }

    public static int getActionId(Class type){
        return actionsId.get(type);
    }

    public static <T extends Action> void addAction(T action){
        var id = currentId++;
        actions.put(id, action);
        actionsId.put(action.getClass(), id);
    }

}
