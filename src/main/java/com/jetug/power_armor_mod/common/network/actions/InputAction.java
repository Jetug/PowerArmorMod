package com.jetug.power_armor_mod.common.network.actions;

import com.jetug.power_armor_mod.common.input.InputKey;
import com.jetug.power_armor_mod.common.input.KeyAction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static com.jetug.power_armor_mod.common.input.CommonInputHandler.onKeyInput;

@SuppressWarnings("ConstantConditions")
public class InputAction extends Action<InputAction>{
    private InputKey key;
    private KeyAction action;

    public InputAction() {}
    public InputAction(InputKey key, KeyAction action) {
        this.key = key;
        this.action = action;
    }

    public int getId(){
        return 2;
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
                buffer.readEnum(KeyAction.class));
    }

    @Override
    public void doServerAction(InputAction message, Supplier<NetworkEvent.Context> context, int entityId) {
        var player = context.get().getSender();
        onKeyInput(message.key, message.action, player);
    }
}
