package com.jetug.power_armor_mod.common.capability.armordata;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.jetug.power_armor_mod.common.capability.constants.Capabilities.ARMOR_DATA;


public class ArmorDataProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {
	private IArmorData data;
	private final LazyOptional<IArmorData> optionalData = LazyOptional.of(() -> data);
	//private LazyOptional<IArmorData> instance = LazyOptional.of(data);

	public ArmorDataProvider(Entity entity){
		this.data = new ArmorData(entity);
	}

	@NotNull
	@Override
	public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		return ARMOR_DATA.orEmpty(cap, this.optionalData);
	}

	@Override
	public CompoundTag serializeNBT() {
		return data.serializeNBT();
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		data.deserializeNBT(nbt);
	}
}
