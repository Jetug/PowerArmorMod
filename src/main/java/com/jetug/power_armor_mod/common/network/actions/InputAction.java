package com.jetug.power_armor_mod.common.network.actions;

import com.jetug.power_armor_mod.common.input.InputKey;
import com.jetug.power_armor_mod.common.input.KeyInputAction;
import com.jetug.power_armor_mod.common.util.enums.DashDirection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static com.jetug.power_armor_mod.common.input.CommonInputHandler.onArmorKeyInput;

@SuppressWarnings("ConstantConditions")
public class InputAction extends Action<InputAction>{
    private InputKey key;
    private KeyInputAction action;

    public InputAction() {}
    public InputAction(InputKey key, KeyInputAction action) {
        this.key = key;
        this.action = action;
    }

    public int getId(){
        return 0;
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeEnum(key);
        buffer.writeEnum(action);

    }

    @Override
    public InputAction read(FriendlyByteBuf buffer) {
        return new InputAction(
                buffer.readEnum(InputKey.class),
                buffer.readEnum(KeyInputAction.class));
    }

    @Override
    public void doServerAction(InputAction message, Supplier<NetworkEvent.Context> context, int entityId) {
        var player = context.get().getSender();
        onArmorKeyInput(message.key, message.action, player);
    }
}
