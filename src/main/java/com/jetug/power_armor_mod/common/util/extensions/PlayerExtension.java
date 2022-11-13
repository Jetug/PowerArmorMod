package com.jetug.power_armor_mod.common.util.extensions;

import com.jetug.power_armor_mod.common.minecraft.entity.PowerArmorEntity;
import net.minecraft.world.entity.player.Player;

public class PlayerExtension {
    public static boolean isWearingPowerArmor(Player player){
        return player.getVehicle() instanceof PowerArmorEntity;
    }
}
