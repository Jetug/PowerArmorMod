package com.jetug.power_armor_mod.common.network.actions;

import com.jetug.power_armor_mod.common.network.ActionRegistry;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Supplier;

import static com.jetug.power_armor_mod.common.util.extensions.PlayerExtension.isWearingPowerArmor;
import static net.minecraftforge.network.NetworkEvent.*;

public abstract class Action<T extends Action<T>> {
    public int getId(){
        return ActionRegistry.getActionId(this.getClass());
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
