package com.jetug.chassis_core.common.config;

import com.jetug.chassis_core.common.data.annotation.Ignored;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Author: MrCrayfish
 */
public class CustomEquipment implements INBTSerializable<CompoundTag> {
    @Ignored
    public ItemStack model;
    public Equipment config;

    public ItemStack getModel() {
        return this.model;
    }

    public Equipment getConfig() {
        return this.config;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compound = new CompoundTag();
        compound.put("Model", this.model.save(new CompoundTag()));
        compound.put("Equipment", this.config.serializeNBT());
        return compound;
    }

    @Override
    public void deserializeNBT(CompoundTag compound) {
        this.model = ItemStack.of(compound.getCompound("Model"));
        var name = ForgeRegistries.ITEMS.getKey(model.getItem());
        this.config = Equipment.create(name, compound.getCompound("Equipment"));
    }
}
