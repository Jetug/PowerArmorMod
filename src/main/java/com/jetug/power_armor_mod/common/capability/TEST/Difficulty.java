package com.jetug.power_armor_mod.common.capability.TEST;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class Difficulty {

	public static final Capability<IDifficulty> INSTANCE = CapabilityManager.get(new CapabilityToken<>() {});

	public Difficulty() { }
}
