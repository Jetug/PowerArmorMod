package com.jetug.power_armor_mod.common.entity.entity_type;

import com.jetug.power_armor_mod.common.entity.capability.data.IPowerArmorPartData;
import com.jetug.power_armor_mod.common.util.enums.BodyPart;
import jdk.jfr.internal.LogLevel;
import jdk.jfr.internal.Logger;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.Pose;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.entity.PartEntity;

import static com.jetug.power_armor_mod.common.entity.capability.data.DataManager.*;
import static com.jetug.power_armor_mod.common.entity.capability.data.PowerArmorPartData.DURABILITY;
import static jdk.jfr.internal.LogTag.JFR_SYSTEM;

public class PowerArmorPartEntity extends PartEntity<PowerArmorEntity> {
    private static final DataParameter<Float> DATA_DURABILITY = EntityDataManager.defineId(PowerArmorPartEntity.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> DATA_DEFENCE = EntityDataManager.defineId(PowerArmorPartEntity.class, DataSerializers.FLOAT);

    public final EntitySize size;
    public final PowerArmorEntity parentMob;
    public final BodyPart bodyPart;

    private ArmorPartsEvents events = null;
    private float durability;

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

    public boolean  hasArmor(){
        return getDurability() > 0;
    }

    public float getDurability(){
        float dur = parentMob.getArmorPartDurability(bodyPart);
        return dur;

        //return durability;
        //return entityData.get(DATA_DURABILITY);

//        IPowerArmorPartData data = getPowerArmorPartData(this);
//
//        Logger.log(JFR_SYSTEM, LogLevel.DEBUG, "" + data.getDurability());
//        return data.getDurability();
    }

    public void setDurability(float value){
        parentMob.setArmorPartDurability(bodyPart, value);
//        durability = value;
//        entityData.set(DATA_DURABILITY, value);

//        getPowerArmorPartData(this).setDurability(value);
        if(events != null)
            events.onDurabilityChanged(value);
    }

    public void damage(float damage){
        parentMob.damageArmor(bodyPart, damage);

        //IPowerArmorPartData data = getPowerArmorPartData(this);
//        float durability = getDurability() - damage;
//        if(durability < 0)
//            durability = 0;
//        setDurability(durability);
    }

    public double getDefense(){
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
    protected void defineSynchedData(){
//        CompoundNBT nbt = serializeNBT();
        float dur = 0f;
//        if(nbt.contains(DURABILITY)){
//            dur = nbt.getFloat(DURABILITY);
//        }
        this.entityData.define(DATA_DURABILITY,dur);
        this.entityData.define(DATA_DEFENCE, 0f);
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        durability = nbt.getFloat(DURABILITY);
        //entityData.set(DATA_DURABILITY, nbt.getFloat(DURABILITY));
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = super.serializeNBT();
        nbt.putFloat(DURABILITY, durability);
        //nbt.putFloat(DURABILITY, entityData.get(DATA_DURABILITY));
        return nbt;
    }

    @Override
    public void load(CompoundNBT nbt) {
        durability = nbt.getFloat(DURABILITY);
        super.load(nbt);
    }

    @Override
    public boolean save(CompoundNBT nbt) {
        nbt.putFloat(DURABILITY, durability);
        return super.save(nbt);
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT nbt){
        //entityData.set(DATA_DURABILITY, nbt.getFloat(DURABILITY));
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT nbt){
        //nbt.putFloat(DURABILITY, entityData.get(DATA_DURABILITY));
    }

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
