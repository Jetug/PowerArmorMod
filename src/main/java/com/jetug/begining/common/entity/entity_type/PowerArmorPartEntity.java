package com.jetug.begining.common.entity.entity_type;

import com.jetug.begining.common.entity.data.IPowerArmorPartData;
import com.jetug.begining.common.util.enums.BodyPart;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.entity.PartEntity;

import static com.jetug.begining.common.entity.data.DataManager.*;

public class PowerArmorPartEntity extends PartEntity<PowerArmorEntity> {

    public final PowerArmorEntity parentMob;
    public final BodyPart bodyPart;
    private final EntitySize size;

    public PowerArmorPartEntity(PowerArmorEntity parent, BodyPart bodyPart, float p_i50232_3_, float p_i50232_4_) {
        super(parent);
        this.size = EntitySize.scalable(p_i50232_3_, p_i50232_4_);
        this.refreshDimensions();
        this.parentMob = parent;
        this.bodyPart = bodyPart;
    }

    public double getDurability(){
        return getPowerArmorPartData(this).getDurability();
    }

    public void damage(double damage){
        IPowerArmorPartData data = getPowerArmorPartData(this);
        double durability = data.getDurability() - damage;
        if(durability < 0)
            durability = 0;
        data.setDurability(durability);
    }

    @Override
    protected void defineSynchedData(){}

    @Override
    protected void readAdditionalSaveData(CompoundNBT p_70037_1_){}

    @Override
    protected void addAdditionalSaveData(CompoundNBT p_213281_1_){}

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean hurt(DamageSource damageSource, float damage) {
        PlayerEntity player = Minecraft.getInstance().player;
        player.sendMessage(new StringTextComponent(bodyPart.getName() + " : " + damage), this.getUUID());
        return this.isInvulnerableTo(damageSource) ? false : this.parentMob.hurt(this, damageSource, damage);
    }

    @Override
    public boolean is(Entity entity) {
        return this == entity || this.parentMob == entity;
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        throw new UnsupportedOperationException();
    }

    @Override
    public EntitySize getDimensions(Pose p_213305_1_) {
        return this.size;
    }
}
