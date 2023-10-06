package com.jetug.chassis_core.common.network.actions;

import com.jetug.chassis_core.common.network.ActionRegistry;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Supplier;

import static net.minecraftforge.network.NetworkEvent.Context;

public abstract class Action<T extends Action<T>> {
    private int id = -1;

    public int getId(){
        return ActionRegistry.getActionId(this.getClass());
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName(){
        return this.getClass().getCanonicalName();
    }

    public static Action<?> getClassByName(String name){
        try {
            var act = Class.forName(name);
            var o = (Object)act;
            var action = (Action<?>)o;

            return action;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void doServerAction(T message, Supplier<Context> context, int entityId) {}

    public void doClientAction(T message, Supplier<Context> context, int entityId) {}

    public abstract void write(FriendlyByteBuf buffer);

    public abstract T read(FriendlyByteBuf buffer);


}
