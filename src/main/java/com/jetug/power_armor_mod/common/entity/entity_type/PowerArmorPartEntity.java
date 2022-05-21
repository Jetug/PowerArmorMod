package com.jetug.power_armor_mod.common.entity.entity_type;

import com.jetug.power_armor_mod.common.entity.capability.data.IPowerArmorPartData;
import com.jetug.power_armor_mod.common.util.enums.BodyPart;
import jdk.jfr.internal.LogLevel;
import jdk.jfr.internal.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.entity.PartEntity;

import static com.jetug.power_armor_mod.common.entity.capability.data.DataManager.*;
import static jdk.jfr.internal.LogTag.JFR_SYSTEM;

public class PowerArmorPartEntity extends PartEntity<PowerArmorEntity> {
    public final EntitySize size;
    public final PowerArmorEntity parentMob;
    public final BodyPart bodyPart;

    private ArmorPartsEvents events = null;
    public boolean isArmorAttached = false;

    public PowerArmorPartEntity(PowerArmorEntity parent, BodyPart bodyPart, float xz, float y) {
        super(parent);
        this.size = EntitySize.scalable(xz, y);
        this.refreshDimensions();
        this.parentMob = parent;
        this.bodyPart = bodyPart;
    }

    public void subscribeEvents(ArmorPartsEvents events){
        this.events = events;
    }

    public boolean hasArmor(){
        return getDurability() > 0;
    }

    public double getDurability(){
        IPowerArmorPartData data = getPowerArmorPartData(this);

        Logger.log(JFR_SYSTEM, LogLevel.DEBUG, "" + data.getDurability());
        return data.getDurability();
    }

    public void setDurability(double value){
        getPowerArmorPartData(this).setDurability(value);
        if(events != null)
            events.onDurabilityChanged(value);
    }

    public void damage(double damage){
        IPowerArmorPartData data = getPowerArmorPartData(this);

        double durability = data.getDurability() - damage;
        if(durability < 0)
            durability = 0;
        setDurability(durability);
    }

    private double getDefense(){
        IPowerArmorPartData data = getPowerArmorPartData(this);
        if(data.getDefense() < 0)
            return 20;
        return getPowerArmorPartData(this).getDefense();
    }

    private void setDefense(double value){
        getPowerArmorPartData(this).setDefense(value);
    }

    @Override
    public ActionResultType interact(PlayerEntity player, Hand hand) {
        return parentMob.onInteract(player, hand);
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
        damage(damage);
        player.sendMessage(new StringTextComponent(bodyPart.getName() + " : " + getDurability()), this.getUUID());
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
