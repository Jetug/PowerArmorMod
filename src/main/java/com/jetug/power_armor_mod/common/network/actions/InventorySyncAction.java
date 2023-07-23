package com.jetug.power_armor_mod.common.network.actions;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.SimpleContainer;

import java.util.function.Supplier;

import static com.jetug.power_armor_mod.common.data.constants.NBT.ITEMS_TAG;
import static com.jetug.power_armor_mod.common.util.extensions.PlayerExtension.*;
import static com.jetug.power_armor_mod.common.util.helpers.InventoryHelper.*;
import static net.minecraftforge.network.NetworkEvent.*;

@SuppressWarnings("ConstantConditions")
public class InventorySyncAction extends Action<InventorySyncAction>{
    private ListTag inventory;

    public InventorySyncAction() {}
    public InventorySyncAction(SimpleContainer inventory) {
        this.inventory = serializeInventory(inventory);
    }

    public InventorySyncAction(ListTag inventory) {
        this.inventory = inventory;
    }

    public int getId(){
        return 1;
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        var nbt = new CompoundTag();
        nbt.put(ITEMS_TAG, inventory);
        buffer.writeNbt(nbt);
    }

    @Override
    public InventorySyncAction read(FriendlyByteBuf buffer) {
        var nbt = buffer.readNbt();
        var listTag = (ListTag)nbt.get(ITEMS_TAG);
        return new InventorySyncAction(listTag);
    }

    @Override
    public void doServerAction(InventorySyncAction message, Supplier<Context> context, int entityId) {
        var player = context.get().getSender();
        if(isWearingPowerArmor(player))
            getPlayerPowerArmor(player).setInventory(message.inventory);
    }

    @Override
    public void doClientAction(InventorySyncAction message, Supplier<Context> context, int entityId) {
        if(isWearingPowerArmor())
            getPlayerPowerArmor().setInventory(message.inventory);
    }
}
