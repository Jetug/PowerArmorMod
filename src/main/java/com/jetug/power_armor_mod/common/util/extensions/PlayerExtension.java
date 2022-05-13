package com.jetug.power_armor_mod.common.util.extensions;

import com.jetug.power_armor_mod.common.entity.entity_type.PowerArmorEntity;
import net.minecraft.entity.player.PlayerEntity;

public class PlayerExtension {
    public static boolean isWearingPowerArmor(PlayerEntity player){
        return player.getVehicle() instanceof PowerArmorEntity;
    }
}
