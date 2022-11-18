package com.jetug.power_armor_mod.common.capability;

import com.jetug.power_armor_mod.common.capability.TEST.IArmorData;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class Capabilities {

	public static final Capability<IArmorData> ARMOR_DATA = CapabilityManager.get(new CapabilityToken<>() {});

	public Capabilities() { }
}
