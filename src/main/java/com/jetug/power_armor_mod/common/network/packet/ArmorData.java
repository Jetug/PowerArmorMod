package com.jetug.power_armor_mod.common.network.packet;
import com.jetug.power_armor_mod.common.capability.armordata.IArmorData;
import com.jetug.power_armor_mod.common.network.PacketHandler;
import com.jetug.power_armor_mod.common.util.enums.BodyPart;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;

import static com.jetug.power_armor_mod.common.util.enums.BodyPart.*;
import static com.jetug.power_armor_mod.common.util.enums.BodyPart.RIGHT_LEG;

public class ArmorData {
    public static final String DURABILITY = "durability";
    public static final String INVENTORY = "inventory";

    public int entityId = -1;
    public ListTag inventory;
//    public HashMap<BodyPart, Integer> durability = new HashMap<>() {{
//        put(HEAD, 0);
//        put(BODY, 0);
//        put(LEFT_ARM, 0);
//        put(RIGHT_ARM, 0);
//        put(LEFT_LEG, 0);
//        put(RIGHT_LEG, 0);
//    }};

    public ArmorData() {}

    public ArmorData(int entityId) {
        this.entityId = entityId;
    }

    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();

        nbt.putInt("ID", entityId);
        //durability.forEach((part, value) -> nbt.putInt(DURABILITY + part.getId(), value));
        nbt.put(INVENTORY, inventory);

        return nbt;
    }

    public void deserializeNBT(CompoundTag nbt) {
        entityId = nbt.getInt("ID");
//        for(int i = 0; i < durability.size(); i++)
//            durability.put(BodyPart.getById(i) , nbt.getInt(DURABILITY + i));
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
