package com.jetug.power_armor_mod.common.capability.constants;

import com.jetug.power_armor_mod.common.capability.TEST.IArmorData;
import com.jetug.power_armor_mod.common.util.constants.Global;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

import static com.jetug.power_armor_mod.common.util.constants.Resources.resourceLocation;

public class Capabilities {
	public static final ResourceLocation ARMOR_DATA_RESOURCE = resourceLocation("armor_data");

	public static final Capability<IArmorData> ARMOR_DATA = CapabilityManager.get(new CapabilityToken<>() {});

	public Capabilities() { }
}
