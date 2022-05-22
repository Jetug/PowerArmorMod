package com.jetug.power_armor_mod.common.entity.entity_type;

import com.jetug.power_armor_mod.common.entity.capability.data.IPlayerData;
import com.jetug.power_armor_mod.common.entity.capability.data.IPowerArmorPartData;
import com.jetug.power_armor_mod.common.entity.capability.data.PowerArmorDataProvider;
import com.jetug.power_armor_mod.common.util.MinecraftUtils;
import com.jetug.power_armor_mod.common.util.enums.BodyPart;
import it.unimi.dsi.fastutil.Hash;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.entity.PartEntity;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;

import java.util.HashMap;

import static com.jetug.power_armor_mod.common.entity.capability.data.DataManager.getPlayerData;
import static com.jetug.power_armor_mod.common.entity.capability.data.DataManager.getPowerArmorPartData;
import static com.jetug.power_armor_mod.common.entity.capability.data.PowerArmorPartData.DURABILITY;
import static com.jetug.power_armor_mod.common.util.enums.BodyPart.*;

public class PowerArmorEntity extends CreatureEntity implements IAnimatable, IJumpingMount
{
    private static final DataParameter<CompoundNBT> DATA_DURABILITY = EntityDataManager.defineId(PowerArmorEntity.class, DataSerializers.COMPOUND_TAG);

    public static final String DURABILITY_ARRAY = "durability_array";
    //private static HashMap
    private static int count = 0;
    private int id;

    private AnimationFactory factory = new AnimationFactory(this);
    private float durability;
    private float[] armorPartsDurability;// = getDefaultArmorPartArray();

    protected boolean isJumping;
    protected float playerJumpPendingScale;

    private final PowerArmorPartEntity[] subEntities;
    public final PowerArmorPartEntity head_;
    public final PowerArmorPartEntity body;
    public final PowerArmorPartEntity leftArm;
    public final PowerArmorPartEntity rightArm;
    public final PowerArmorPartEntity leftLeg;
    public final PowerArmorPartEntity rightLeg;

//    @Override
//    public void load(CompoundNBT p_70020_1_) {
//        super.load(p_70020_1_);
//    }

    public PowerArmorEntity(EntityType<? extends CreatureEntity> type, World worldIn) {
        super(type, worldIn);
        id = count++;
        head_ =   new PowerArmorPartEntity(this, HEAD, 0.7f, 0.7f);
        body   =   new PowerArmorPartEntity(this, BodyPart.BODY, 1.0f, 1.0f);
        leftArm  = new PowerArmorPartEntity(this, BodyPart.LEFT_ARM, 0.7f, 1.0f);
        rightArm = new PowerArmorPartEntity(this, BodyPart.RIGHT_ARM, 0.7f, 1.0f);
        leftLeg  = new PowerArmorPartEntity(this, BodyPart.LEFT_LEG, 0.6f, 1.0f);
        rightLeg = new PowerArmorPartEntity(this, BodyPart.RIGHT_LEG, 0.6f, 1.0f);
        subEntities = new PowerArmorPartEntity[]{head_, body, leftArm, rightArm, leftLeg, rightLeg};
        this.noCulling = true;
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return CreatureEntity.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 10.0D)
            .add(Attributes.ATTACK_DAMAGE, 0.0D)
            .add(Attributes.MOVEMENT_SPEED, 0.20D)
            .add(Attributes.ATTACK_KNOCKBACK, 0.0D)
            .add(Attributes.JUMP_STRENGTH, 2.0D)
            .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    public PowerArmorPartEntity getArmorPart(BodyPart part){
        switch (part){
            case HEAD:
                return head_;
            case BODY:
                return body;
            case LEFT_ARM:
                return leftArm;
            case RIGHT_ARM:
                return rightArm;
            case LEFT_LEG:
                return leftLeg;
            case RIGHT_LEG:
                return rightLeg;
        }
        return null;
    }

    public boolean isJumping() {
        return this.isJumping;
    }

    public void setIsJumping(boolean p_110255_1_) {
        this.isJumping = p_110255_1_;
    }

    public boolean hurt(PowerArmorPartEntity part, DamageSource damageSource, float damage) {
        if (damage < 0.01F) {
            return false;
        } else {
            return true;
        }
    }

    private void tickPart(PowerArmorPartEntity part, double x, double y, double z) {
        part.setPos(this.getX() + x, this.getY() + y, this.getZ() + z);
    }

    @Override
    public void aiStep() {
        super.aiStep();

        Vector3d[] aVector3d = new Vector3d[this.subEntities.length];
        for(int j = 0; j < this.subEntities.length; ++j) {
            aVector3d[j] = new Vector3d(this.subEntities[j].getX(), this.subEntities[j].getY(), this.subEntities[j].getZ());
        }

        float rotation = this.yRot * ((float)Math.PI / 180F);
        float xPos = MathHelper.cos(rotation);
        float zPos = MathHelper.sin(rotation);
        float armPos = 0.8f;
        float legPos = 0.2f;

        this.tickPart(this.head_, 0, 2.1,0);
        this.tickPart(this.body, 0, 1.2, 0);
        this.tickPart(this.rightArm, xPos * -armPos, 1.1, zPos * -armPos);
        this.tickPart(this.leftArm, xPos * armPos, 1.1, zPos * armPos);
        this.tickPart(this.rightLeg, xPos * -legPos, 0, zPos * -legPos);
        this.tickPart(this.leftLeg, xPos * legPos, 0, zPos * legPos);

        for(int l = 0; l < this.subEntities.length; ++l) {
            this.subEntities[l].xo = aVector3d[l].x;
            this.subEntities[l].yo = aVector3d[l].y;
            this.subEntities[l].zo = aVector3d[l].z;
            this.subEntities[l].xOld = aVector3d[l].x;
            this.subEntities[l].yOld = aVector3d[l].y;
            this.subEntities[l].zOld = aVector3d[l].z;
        }
    }

    @Override
    public PartEntity<?>[] getParts() {
        return this.subEntities;
    }

    @Override
    public boolean isMultipartEntity() {
        return true;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean isInvisible() {
        ClientPlayerEntity clientPlayer = Minecraft.getInstance().player;
        PointOfView pov = Minecraft.getInstance().options.getCameraType();

        if(hasPassenger(clientPlayer) && pov == PointOfView.FIRST_PERSON)
            return true;
        return super.isInvisible();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(DATA_DURABILITY, 0f);
    }

    private float[] arrayIntToFloat(int[] array){
        float[] result = new float[array.length];
        for (int i = 0; i < array.length ;i++){
            result[i] = Float.intBitsToFloat(array[i]);
        }
        return result;
    }

//    private void arrayIntToFloat(int[] array1, float array2[]){
//        for (int i = 0; i < array1.length ;i++){
//            float val = Float.intBitsToFloat(array1[i]);
//            if(val == 0)
//                val = 1.0f;
//            array2[i] = val;
//        }
//    }


    private int[] arrayFloatToInt(float[] array){
        int[] result = new int[array.length];
        for (int i = 0; i < array.length ;i++){
            int val = Float.floatToIntBits(array[i]);
            if(val == 0)
                val = Float.floatToIntBits(1f);
            result[i] = val;
        }
        return result;
    }

    @Override
    public void load(CompoundNBT nbt) {
        super.load(nbt);
        durability = nbt.getFloat(DURABILITY);
        if(nbt.contains(DURABILITY_ARRAY))
            armorPartsDurability = arrayIntToFloat(nbt.getIntArray(DURABILITY_ARRAY));
    }

    @Override
    public boolean save(CompoundNBT nbt) {
        nbt.putFloat(DURABILITY, durability);
        nbt.putIntArray(DURABILITY_ARRAY, arrayFloatToInt(armorPartsDurability));
        return super.save(nbt);
    }

//    @Override
//    public void deserializeNBT(CompoundNBT nbt) {
//        super.deserializeNBT(nbt);
//        durability = nbt.getFloat(DURABILITY);
//        if(nbt.contains(DURABILITY_ARRAY))
//            armorPartsDurability = arrayIntToFloat(nbt.getIntArray(DURABILITY_ARRAY));
//    }
//
//    @Override
//    public CompoundNBT serializeNBT() {
//        CompoundNBT nbt = super.serializeNBT();
//        nbt.putFloat(DURABILITY, durability);
//        nbt.putIntArray(DURABILITY_ARRAY, arrayFloatToInt(armorPartsDurability));
//        return nbt;
//    }

    private float[] getDefaultArmorPartArray(){
        return new float[6];//{1,1,1,1,1,1};
    }

    public float getArmorPartDurability(BodyPart bodyPart){
        int i = bodyPart.getId();

        CompoundNBT nbt = entityData.get(DATA_DURABILITY);
        durability = nbt.getFloat(DURABILITY);
        if(nbt.contains(DURABILITY_ARRAY))
            armorPartsDurability = arrayIntToFloat(nbt.getIntArray(DURABILITY_ARRAY));
        return armorPartsDurability[i];
//        if(armorPartsDurability != null && armorPartsDurability.length > 0)
//            return armorPartsDurability[i];
//        return 1;
    }

    public void setArmorPartDurability(BodyPart bodyPart, float value){
        int i = bodyPart.getId();

        CompoundNBT nbt = new CompoundNBT();
        armorPartsDurability[i] = value;
        nbt.putIntArray(DURABILITY_ARRAY, arrayFloatToInt(armorPartsDurability));

//        if(armorPartsDurability == null || armorPartsDurability.length == 0) {
//            armorPartsDurability = getDefaultArmorPartArray();
//        }
//        armorPartsDurability[i] = value;
    }

    public void damageArmor(BodyPart bodyPart, float damage){
        float durability = getArmorPartDurability(bodyPart) - damage;
        if(durability < 0)
            durability = 0;

        setArmorPartDurability(bodyPart, durability);
    }

    public ActionResultType onInteract(PlayerEntity player, Hand hand) {
        for(int j = 0; j < this.subEntities.length; ++j) {
            this.subEntities[j].setDurability(1);
        }
        this.doPlayerRide(player);

//        IPowerArmorPartData data = this.getCapability(PowerArmorDataProvider.POWER_ARMOR_PART_DATA, null).orElse(null);
//        MinecraftUtils.sendMessage("" + data.getDurability(), this);
//        data.setDurability(data.getDurability() + 1);

//        MinecraftUtils.sendMessage("?" + (this == body.parentMob), this);


        MinecraftUtils.sendMessage("" + durability, this);
        durability += 1;
//        entityData.set(DATA_DURABILITY, entityData.get(DATA_DURABILITY) + 1);



        return ActionResultType.sidedSuccess(this.level.isClientSide);
    }

    //AbstractHorseEntity

    @Nullable
    public Entity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
    }

    public boolean canBeControlledByRider() {
        return this.getControllingPassenger() instanceof LivingEntity;
    }

    public double getCustomJump() {
        return this.getAttributeValue(Attributes.JUMP_STRENGTH);
    }

    @Override
    public void positionRider(Entity entity) {
        super.positionRider(entity);

        float posX = MathHelper.sin(this.yBodyRot * ((float)Math.PI / 180F));
        float posZ = MathHelper.cos(this.yBodyRot * ((float)Math.PI / 180F));
        double posXZ = 0.1D;
        double posY = 0.9D;
        entity.setPos(this.getX() + (posXZ * posX),
                this.getY() + this.getPassengersRidingOffset() + entity.getMyRidingOffset() - posY,
                this.getZ() - (posXZ * posZ));
        if (entity instanceof LivingEntity) {
            ((LivingEntity) entity).yBodyRot = this.yBodyRot;
        }
    }

    @Override
    public void travel(Vector3d sVector3d) {
        if (this.isAlive()) {
            if (this.isVehicle() && this.canBeControlledByRider()) {
                LivingEntity livingentity = (LivingEntity)this.getControllingPassenger();
                this.yRot = livingentity.yRot;
                this.yRotO = this.yRot;
                this.xRot = livingentity.xRot * 0.5F;
                this.setRot(this.yRot, this.xRot);
                this.yBodyRot = this.yRot;
                this.yHeadRot = this.yBodyRot;
                float f = livingentity.xxa /* * 0.5F*/;
                float f1 = livingentity.zza;

                if (this.playerJumpPendingScale > 0.0F && !this.isJumping() && this.onGround) {
                    double jump = this.getCustomJump() * (double)this.playerJumpPendingScale * (double)this.getBlockJumpFactor();
                    double d1;
                    if (this.hasEffect(Effects.JUMP)) {
                        d1 = jump + (double)((float)(this.getEffect(Effects.JUMP).getAmplifier() + 1) * 0.1F);
                    } else {
                        d1 = jump;
                    }

                    Vector3d vector3d = this.getDeltaMovement();
                    this.setDeltaMovement(vector3d.x, d1, vector3d.z);
                    this.setIsJumping(true);
                    this.hasImpulse = true;
                    net.minecraftforge.common.ForgeHooks.onLivingJump(this);
                    if (f1 > 0.0F) {
                        float f2 = MathHelper.sin(this.yRot * ((float)Math.PI / 180F));
                        float f3 = MathHelper.cos(this.yRot * ((float)Math.PI / 180F));
                        this.setDeltaMovement(this.getDeltaMovement().add(
                                -0.4F * f2 * this.playerJumpPendingScale
                                ,0.0D
                                ,0.4F * f3 * this.playerJumpPendingScale));
                    }

                    this.playerJumpPendingScale = 0.0F;
                }

                this.flyingSpeed = this.getSpeed() * 0.1F;
                if (this.isControlledByLocalInstance()) {
                    this.setSpeed((float)this.getAttributeValue(Attributes.MOVEMENT_SPEED));
                    super.travel(new Vector3d(f, sVector3d.y, f1));
                } else if (livingentity instanceof PlayerEntity) {
                    this.setDeltaMovement(Vector3d.ZERO);
                }

                if (this.onGround) {
                    this.playerJumpPendingScale = 0.0F;
                    this.setIsJumping(false);
                }

                this.calculateEntityAnimation(this, false);
            } else {
                this.flyingSpeed = 0.02F;
                super.travel(sVector3d);
            }
        }
    }

    @Override
    public boolean causeFallDamage(float height, float p_225503_2_) {
        if (height > 1.0F) {
            this.playSound(SoundEvents.ANVIL_LAND, 0.4F, 1.0F);
        }

        int immune = 3;
        int damage = this.calculateFallDamage(height, p_225503_2_);
        if (damage <= immune) {
            return false;
        } else {
            //this.hurt(DamageSource.FALL, (float) damage - immune);
            if (this.isVehicle()) {
                for(Entity entity : this.getIndirectPassengers()) {
                    entity.hurt(DamageSource.FALL, (float) damage - immune);
                }
            }

            this.playBlockFallSound();
            return true;
        }
    }


    @Override
    public void onPlayerJump(int p_110206_1_) {
        if (p_110206_1_ < 0) {
            p_110206_1_ = 0;
        }

        if (p_110206_1_ >= 90) {
            this.playerJumpPendingScale = 1.0F;
        } else {
            this.playerJumpPendingScale = 0.4F + 0.4F * (float)p_110206_1_ / 90.0F;
        }
    }

    @Override
    public boolean canJump() {
        return true;
    }

    @Override
    public void handleStartJump(int p_184775_1_) {}

    @Override
    public void handleStopJump() {}

    @Override
    public void registerControllers(AnimationData data) {
        AnimationController controller = new AnimationController<PowerArmorEntity>(this, "controller", 0, this::predicate);
        data.addAnimationController(controller);
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if(event.isMoving()){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("walk", true));
            event.getController().animationSpeed = 4.0D;
            return PlayState.CONTINUE;
        }
        event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", true));
        return PlayState.CONTINUE;
    }

    private void doPlayerRide(PlayerEntity player) {
        if (!this.level.isClientSide) {
            player.yRot = this.yRot;
            player.xRot = this.xRot;
            player.startRiding(this);

            IPlayerData data = getPlayerData(player);
            data.setIsInPowerArmor(true);
            player.sendMessage(new StringTextComponent("" + data.getIsInPowerArmor()), this.getUUID());
        }
    }
}