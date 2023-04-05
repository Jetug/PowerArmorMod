package com.jetug.power_armor_mod.common.network.actions;

import com.jetug.power_armor_mod.common.foundation.entity.PowerArmorEntity;
import com.jetug.power_armor_mod.common.util.enums.DashDirection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

@SuppressWarnings("ConstantConditions")
public class InputAction extends Action<InputAction>{
    private int key;

    public InputAction() {}
    public InputAction(int key) {
        this.key = key;
    }

    public int getId(){
        return 0;
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(key);

    }

    @Override
    public InputAction read(FriendlyByteBuf buffer) {
        return new InputAction(buffer.readInt());
    }

    @Override
    public void doServerAction(InputAction message, Supplier<NetworkEvent.Context> context, int entityId) {
        var player = context.get().getSender();
        ((PowerArmorEntity)player.level.getEntity(entityId)).dash(message.direction);
    }
}
