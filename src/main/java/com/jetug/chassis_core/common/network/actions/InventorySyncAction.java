package com.jetug.chassis_core.common.network.actions;

import com.jetug.chassis_core.common.util.helpers.PlayerUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.SimpleContainer;

import java.util.function.Supplier;

import static com.jetug.chassis_core.common.data.constants.NBT.ITEMS_TAG;
import static com.jetug.chassis_core.common.util.helpers.InventoryHelper.serializeInventory;
import static com.jetug.chassis_core.common.util.helpers.PlayerUtils.getEntityChassis;
import static com.jetug.chassis_core.common.util.helpers.PlayerUtils.isWearingChassis;
import static net.minecraftforge.network.NetworkEvent.Context;

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

//    public int getId(){
//        return 1;
//    }

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
        if(isWearingChassis(player))
            getEntityChassis(player).setInventory(message.inventory);
    }

    @Override
    public void doClientAction(InventorySyncAction message, Supplier<Context> context, int entityId) {
        if(PlayerUtils.isLocalWearingChassis())
            PlayerUtils.getLocalPlayerChassis().setInventory(message.inventory);
    }
}
