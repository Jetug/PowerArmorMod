package com.jetug.power_armor_mod.common.foundation.entity;

import com.jetug.power_armor_mod.common.network.*;
import com.jetug.power_armor_mod.common.network.packet.*;
import com.jetug.power_armor_mod.common.util.constants.*;
import com.jetug.power_armor_mod.common.util.enums.*;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import static com.jetug.power_armor_mod.common.foundation.registery.EntityTypeRegistry.POWER_ARMOR_PART;
import static org.apache.logging.log4j.Level.INFO;

public class PowerArmorPartEntity extends Entity{
    public EntityDimensions size;
    public PowerArmorEntity parentMob;
    public BodyPart bodyPart;

    public PowerArmorPartEntity(EntityType<?> type, Level level) {
        super(type, level);
    }

    public PowerArmorPartEntity(PowerArmorEntity parent, BodyPart bodyPart, float xz, float y) {
        this(POWER_ARMOR_PART.get(), parent.level);
        this.parentMob = parent;
        this.bodyPart = bodyPart;
        this.size = EntityDimensions.scalable(xz, y);
        this.refreshDimensions();
    }

    @Override
    public boolean isColliding(@NotNull BlockPos p_20040_, @NotNull BlockState p_20041_) {
        return true;
    }

    @Override
    protected void defineSynchedData() {}

    public float getDurability(){
        return parentMob.getArmorDurability(bodyPart);
    }

    @Override
    public @NotNull InteractionResult interactAt(@NotNull Player player, @NotNull Vec3 vector, @NotNull InteractionHand hand) {
        if(level.isClientSide){
            PacketHandler.sendToServer(new InteractPacket(this.getId(), hand ,vector));
        }

        Global.LOGGER.log(INFO, "interactAt " + bodyPart.getName() + level.isClientSide);
        return parentMob.interactAt(player, vector, hand);
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag nbt){}

    @Override
    protected void addAdditionalSaveData(CompoundTag nbt){}

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean hurt(DamageSource damageSource, float damage) {
        Global.LOGGER.log(INFO, bodyPart.getName() + " : " + getDurability() + " isClientSide: " + level.isClientSide);
        return this.parentMob.hurt(this, damageSource, damage);
    }

    @Override
    public boolean is(@NotNull Entity entity) {
        return this == entity || this.parentMob == entity;
    }

    public @NotNull EntityDimensions getDimensions(@NotNull Pose p_31023_) {
        return size;
    }

    public boolean shouldContinuePersisting() {
        return isAddedToWorld() || this.isRemoved();
    }
}
