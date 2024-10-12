package com.jetug.chassis_core.common.foundation.item;

import com.jetug.chassis_core.common.config.NetworkManager;
import com.jetug.chassis_core.common.data.json.ItemConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface IConfigProvider<T extends INBTSerializable<CompoundTag>> {
    T getConfig();

    void setConfig(NetworkManager.Supplier<T> supplier);
}
