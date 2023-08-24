package com.jetug.power_armor_mod.common.foundation.entity;

import com.jetug.power_armor_mod.common.network.PacketHandler;
import com.jetug.power_armor_mod.common.network.packet.InteractPacket;
import com.jetug.power_armor_mod.common.data.enums.*;
import com.jetug.power_armor_mod.common.util.interfaces.ArmorPartsEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;
import org.jetbrains.annotations.NotNull;


public class PowerArmorPartEntityOrig extends PartEntity<ArmorChassisEntity> {
    public final EntityDimensions size;
    public final ArmorChassisEntity parentMob;
    public final BodyPart bodyPart;

    private ArmorPartsEvents events = null;

    public PowerArmorPartEntityOrig(ArmorChassisEntity parent, BodyPart bodyPart, float xz, float y) {
        super(parent);
        this.size = EntityDimensions.scalable(xz, y);
        this.refreshDimensions();
        this.parentMob = parent;
        this.bodyPart = bodyPart;
    }

    @Override
    public boolean isColliding(@NotNull BlockPos p_20040_, @NotNull BlockState p_20041_) {
        return true;
    }

    @Override
    protected void defineSynchedData() {}

    public void subscribeEvents(ArmorPartsEvents events){
        this.events = events;
    }

    public float getDurability(){
        return parentMob.getArmorDurability(bodyPart);
    }

//    public void damage(float damage){
//        parentMob.damageArmorItem(bodyPart, damage);
//    }

//    @Override
//    public InteractionResult interact(Player p_19978_, InteractionHand p_19979_) {
//        return super.interact(p_19978_, p_19979_);
//    }

    @Override
    public @NotNull InteractionResult interactAt(@NotNull Player player, @NotNull Vec3 vector, @NotNull InteractionHand hand) {
        if(level.isClientSide){
            PacketHandler.sendToServer(new InteractPacket(this.getId(), hand ,vector));
        }

        //Global.LOGGER.log(INFO, "interactAt " + bodyPart.getName() + level.isClientSide);
        return parentMob.interactAt(player, vector, hand);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag nbt){}

    @Override
    protected void addAdditionalSaveData(CompoundTag nbt){}

    @Override
    public boolean isPickable() {
        return false;
    }

//    @Override
//    public boolean hurt(DamageSource damageSource, float damage) {
//        Global.LOGGER.log(Level.DEBUG, bodyPart.getName() + " : " + getDurability() + " isClientSide: " + level.isClientSide);
//        //return this.parentMob.hurt(this, damageSource, damage);
//
//    }

    @Override
    public boolean is(@NotNull Entity entity) {
        return this == entity || this.parentMob == entity;
    }

    public Packet<?> getAddEntityPacket() {
        throw new UnsupportedOperationException();
    }

    public @NotNull EntityDimensions getDimensions(@NotNull Pose p_31023_) {
        return size;
    }
}
