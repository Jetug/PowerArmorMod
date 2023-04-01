package com.jetug.power_armor_mod.common.network.actions;

import com.jetug.power_armor_mod.common.foundation.entity.PowerArmorBase;
import com.jetug.power_armor_mod.common.foundation.entity.PowerArmorEntity;
import com.jetug.power_armor_mod.common.util.enums.DashDirection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

import static com.jetug.power_armor_mod.common.util.extensions.PlayerExtension.*;

@SuppressWarnings("ConstantConditions")
public class DashAction extends Action<DashAction>{
    private DashDirection direction;

    public DashAction() {}
    public DashAction(DashDirection direction) {
        this.direction = direction;
    }

    public int getId(){
        return 0;
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeEnum(direction);
    }

    @Override
    public DashAction read(FriendlyByteBuf buffer) {
        return new DashAction(buffer.readEnum(DashDirection.class));
    }

    @Override
    public void doServerAction(DashAction message, Supplier<NetworkEvent.Context> context, int entityId) {
        var player = context.get().getSender();
        ((PowerArmorEntity)player.level.getEntity(entityId)).dash(message.direction);
    }
}
