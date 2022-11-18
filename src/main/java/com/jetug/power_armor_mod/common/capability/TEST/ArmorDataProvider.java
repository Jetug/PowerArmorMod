package com.jetug.power_armor_mod.common.capability.TEST;

import com.jetug.power_armor_mod.common.capability.constants.Capabilities;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ArmorDataProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {
	private final IArmorData backend = new ArmorDataImpl(null);
	private final LazyOptional<IArmorData> optionalData = LazyOptional.of(() -> backend);

	@NotNull
	@Override
	public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		return Capabilities.ARMOR_DATA.orEmpty(cap, this.optionalData);
	}

	public void invalidate() {
		this.optionalData.invalidate();
	}

	@Override
	public CompoundTag serializeNBT() {
		return backend.serializeNBT();
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		backend.deserializeNBT(nbt);
	}
}
