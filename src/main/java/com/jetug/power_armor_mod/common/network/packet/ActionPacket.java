package com.jetug.power_armor_mod.common.network.packet;

import com.jetug.power_armor_mod.common.foundation.entity.PowerArmorEntity;
import com.jetug.power_armor_mod.common.util.enums.ActionType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static com.jetug.power_armor_mod.common.util.extensions.PlayerExtension.*;

@SuppressWarnings("ConstantConditions")
public class ActionPacket{
    ActionType action = null;

    public ActionPacket(ActionType action) {
        this.action = action;
    }

    public ActionPacket() {}

    public static void write(ActionPacket message, FriendlyByteBuf buffer) {
        buffer.writeByte(message.action.getId());
    }

    public static ActionPacket read(FriendlyByteBuf buffer) {
        var action = ActionType.getById(buffer.readByte());
        return new ActionPacket(action);
    }

    public static void handle(ActionPacket message, Supplier<NetworkEvent.Context> context) {
        var player = context.get().getSender();
        if (player == null || !isWearingPowerArmor(player)) return;
        var armor = (PowerArmorEntity)player.getVehicle();

        switch (message.action){
            case DISMOUNT -> player.stopRiding();
            case OPEN_GUI -> armor.openGUI(player);
            case ADD_ATTACK_CHARGE -> armor.addAttackCharge();
        }
    }
}