package com.jetug.power_armor_mod.common.network.actions;

import com.jetug.power_armor_mod.client.ClientData;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.ContainerData;

import java.util.ArrayList;
import java.util.function.Supplier;

import static com.jetug.power_armor_mod.common.util.extensions.PlayerExtension.getPlayerPowerArmor;
import static com.jetug.power_armor_mod.common.util.extensions.PlayerExtension.isWearingPowerArmor;
import static net.minecraft.nbt.NbtUtils.readBlockPos;
import static net.minecraft.nbt.NbtUtils.writeBlockPos;
import static net.minecraftforge.network.NetworkEvent.Context;

@SuppressWarnings("ConstantConditions")
public class CastingStatusAction extends Action<CastingStatusAction>{
    private ContainerData data;
    private BlockPos blockPos;

    public CastingStatusAction() {}

    public CastingStatusAction(ContainerData data, BlockPos blockPos) {
        this.data = data;
        this.blockPos = blockPos;
    }

    public int getId(){
        return 4;
    }

    private CompoundTag writeContainerData(ContainerData data){
        var tag = new CompoundTag();
        var arr = new ArrayList<Integer>();

        for (var i = 0; i < data.getCount(); i++) arr.add(data.get(i));

        tag.putIntArray("arr", arr);
        return tag;
    }

    private ContainerData readContainerData(CompoundTag tag){
        return new ContainerData(){
            private final int[] data = tag.getIntArray("arr");
            @Override
            public int get(int pIndex) {
                return data[pIndex];
            }

            @Override
            public void set(int pIndex, int pValue) {}

            @Override
            public int getCount() {
                return data.length;
            }
        };
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeNbt(writeContainerData(data));
        buffer.writeNbt(writeBlockPos(blockPos));
    }

    @Override
    public CastingStatusAction read(FriendlyByteBuf buffer) {
        return new CastingStatusAction(
                readContainerData(buffer.readNbt()),
                readBlockPos(buffer.readNbt()));
    }

    @Override
    public void doClientAction(CastingStatusAction message, Supplier<Context> context, int entityId) {
        ClientData.containerData.put(message.blockPos, message.data);
    }
}
