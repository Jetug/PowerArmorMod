package com.jetug.begining.common.entity.data;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;
import software.bernie.geckolib3.world.storage.GeckoLibIdTracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static com.jetug.begining.common.entity.data.ModPlayerData.IS_IN_POWER_ARMOR;

public class PlayerDataManager extends WorldSavedData{
    private static final String NAME = "mod_player_data";
    private ModPlayerData data = null;

    public PlayerDataManager() {
        super(NAME);
    }

    public PlayerDataManager(CompoundNBT tag) {
        super(NAME);
        data = new ModPlayerData();
        data.setIsInPowerArmor(tag.getBoolean(IS_IN_POWER_ARMOR));
    }

    public static PlayerDataManager get(World world){
        if(world.isClientSide){
            throw new RuntimeException("Don't access this client-side");
        }
        DimensionSavedDataManager storage = ((ServerWorld) world).getDataStorage();

        return world.getServer()
                .overworld()
                .getDataStorage()
                .computeIfAbsent(PlayerDataManager::new, NAME);

        //return storage.computeIfAbsent(PlayerDataManager::new, "player_data_manager");
    }

    @Override
    public void load(CompoundNBT tag) {

    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        tag.putBoolean(IS_IN_POWER_ARMOR ,data.getIsInPowerArmor());
        return tag;
    }
}
