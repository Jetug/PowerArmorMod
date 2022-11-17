package com.jetug.power_armor_mod.common.minecraft.entity;

import com.jetug.power_armor_mod.common.util.constants.Global;
import com.jetug.power_armor_mod.common.util.enums.BodyPart;
import com.jetug.power_armor_mod.common.util.interfaces.ArmorPartsEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.entity.PartEntity;
import org.apache.logging.log4j.Level;
import org.jetbrains.annotations.NotNull;

import static com.jetug.power_armor_mod.common.capability.data.DataManager.getPowerArmorPartData;

public class PowerArmorPartEntity extends PartEntity<PowerArmorEntity> {
    public final EntityDimensions size;
    public final PowerArmorEntity parentMob;
    public final BodyPart bodyPart;

    private ArmorPartsEvents events = null;

    public PowerArmorPartEntity(PowerArmorEntity parent, BodyPart bodyPart, float xz, float y) {
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

    public void damage(float damage){
        parentMob.damageArmor(bodyPart, damage);
    }

    public double getDefense(){
        var data = getPowerArmorPartData(this);
        if(data.getDefense() < 0)
            return 20;
        return getPowerArmorPartData(this).getDefense();
    }

    private void setDefense(double value){
        getPowerArmorPartData(this).setDefense(value);
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        return parentMob.onInteract(player, hand);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag nbt){}

    @Override
    protected void addAdditionalSaveData(CompoundTag nbt){}

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean hurt(DamageSource damageSource, float damage) {
        Player player = Minecraft.getInstance().player;
        damage(damage);
            Global.LOGGER.log(Level.DEBUG, bodyPart.getName() + " : " + getDurability() + " isClientSide: " + level.isClientSide);
        return !this.isInvulnerableTo(damageSource) && this.parentMob.hurt(this, damageSource, damage);
    }

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
