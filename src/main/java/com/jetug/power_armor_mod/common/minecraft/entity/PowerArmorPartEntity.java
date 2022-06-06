package com.jetug.power_armor_mod.common.minecraft.entity;

import com.jetug.power_armor_mod.common.capability.data.IArmorPartData;
import com.jetug.power_armor_mod.common.util.interfaces.ArmorPartsEvents;
import com.jetug.power_armor_mod.common.util.enums.BodyPart;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.Pose;
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

import static com.jetug.power_armor_mod.common.capability.data.DataManager.getPowerArmorPartData;

public class PowerArmorPartEntity extends PartEntity<PowerArmorEntity> {
    private static final DataParameter<Float> DATA_DURABILITY = EntityDataManager.defineId(PowerArmorPartEntity.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> DATA_DEFENCE = EntityDataManager.defineId(PowerArmorPartEntity.class, DataSerializers.FLOAT);

    public final EntitySize size;
    public final PowerArmorEntity parentMob;
    public final BodyPart bodyPart;

    private ArmorPartsEvents events = null;

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

//    public boolean hasArmor(){
//        return getDurability() > 0;
//    }

    public float getDurability(){
        return parentMob.getArmorDurability(bodyPart);
    }

//    public void setDurability(float value){
//        parentMob.setArmorDurability(bodyPart, value);
//
//        if(events != null)
//            events.onDurabilityChanged(value);
//    }

    public void damage(float damage){
        parentMob.damageArmor(bodyPart, damage);
    }

    public double getDefense(){
        IArmorPartData data = getPowerArmorPartData(this);
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
        this.entityData.define(DATA_DURABILITY, 0f);
        this.entityData.define(DATA_DEFENCE, 0f);
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT nbt){}

    @Override
    protected void addAdditionalSaveData(CompoundNBT nbt){}

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean hurt(DamageSource damageSource, float damage) {
        PlayerEntity player = Minecraft.getInstance().player;
        damage(damage);
        player.sendMessage(new StringTextComponent(bodyPart.getName() + " : " + getDurability() + " isClientSide: " + level.isClientSide), this.getUUID());
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
