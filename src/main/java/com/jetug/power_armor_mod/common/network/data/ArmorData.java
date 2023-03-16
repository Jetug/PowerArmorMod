package com.jetug.power_armor_mod.common.network.data;

import com.jetug.power_armor_mod.common.network.PacketHandler;
import com.jetug.power_armor_mod.common.network.packet.PowerArmorPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;

public class ArmorData {
    public static final String INVENTORY = "inventory";
    public static final String HEAT = "Heat";
    public static final String ID = "ID";

    public int entityId = -1;
    public ListTag inventory;
    public int heat;

    public ArmorData() {}

    public ArmorData(int entityId) {
        this.entityId = entityId;
    }

    public CompoundTag serializeNBT() {
        var nbt = new CompoundTag();
        nbt.putInt(ID, entityId);
        nbt.put(INVENTORY, inventory);
        nbt.putInt(HEAT, heat);

        return nbt;
    }

    public void deserializeNBT(CompoundTag nbt) {
        entityId = nbt.getInt(ID);
        inventory = (ListTag)nbt.get(INVENTORY);
        heat = nbt.getInt(HEAT);
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
