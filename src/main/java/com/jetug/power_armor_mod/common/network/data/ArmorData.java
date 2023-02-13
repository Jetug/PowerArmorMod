package com.jetug.power_armor_mod.common.network.data;

import com.jetug.power_armor_mod.common.network.PacketHandler;
import com.jetug.power_armor_mod.common.network.packet.PowerArmorPacket;
import com.jetug.power_armor_mod.common.util.enums.BodyPart;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;

import static com.jetug.power_armor_mod.common.util.enums.BodyPart.*;
import static com.jetug.power_armor_mod.common.util.enums.BodyPart.RIGHT_LEG;

public class ArmorData {
    public static final String INVENTORY = "inventory";

    public int entityId = -1;
    public ListTag inventory;

    public ArmorData() {}

    public ArmorData(int entityId) {
        this.entityId = entityId;
    }

    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("ID", entityId);
        nbt.put(INVENTORY, inventory);

        return nbt;
    }

    public void deserializeNBT(CompoundTag nbt) {
        entityId = nbt.getInt("ID");
        inventory = (ListTag)nbt.get(INVENTORY);
    }

    public void sentToClientPlayer(ServerPlayer player) {
        PacketHandler.sendTo(new PowerArmorPacket(this), player);
    }

    public void sentToServer() {
        PacketHandler.sendToServer(new PowerArmorPacket(this));
    }

    public void sentToClient() {
        PacketHandler.sendToAllPlayers(new PowerArmorPacket(this));
    }

    public boolean syncWithAll() {
        return false;
    }
}
