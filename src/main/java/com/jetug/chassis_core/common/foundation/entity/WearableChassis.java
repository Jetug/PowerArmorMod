package com.jetug.chassis_core.common.foundation.entity;

import com.jetug.chassis_core.ChassisCore;
import com.jetug.chassis_core.Global;
import com.jetug.chassis_core.common.foundation.item.ChassisArmor;
import com.jetug.chassis_core.common.foundation.item.ChassisEquipment;
import com.jetug.chassis_core.common.util.helpers.Speedometer;
import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.util.AzureLibUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.HashMap;

import static com.jetug.chassis_core.common.data.constants.ChassisPart.*;
import static com.jetug.chassis_core.common.data.constants.Resources.resourceLocation;
import static net.minecraft.util.Mth.cos;
import static net.minecraft.util.Mth.sin;
import static org.apache.logging.log4j.Level.DEBUG;

public abstract class WearableChassis extends ChassisBase implements GeoEntity {
    public static final float ROTATION = (float) Math.PI / 180F;
    public static final int EFFECT_DURATION = 9;
    public static final HandEntity HAND_ENTITY = new HandEntity();
    public static final ResourceLocation DEFAULT_ICON = resourceLocation("textures/item/chassis.png");
    public final Speedometer speedometer = new Speedometer(this);
    private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);
    protected boolean isJumping;
    protected float jumpScale;

    public WearableChassis(EntityType<? extends ChassisBase> type, Level worldIn) {
        super(type, worldIn);
    }

    public WearableChassis(EntityType<? extends LivingEntity> pEntityType, Level pLevel, HashMap<String, Integer> partIdMap) {
        super(pEntityType, pLevel, partIdMap);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return ChassisBase.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 1000.0D)
                .add(Attributes.ATTACK_DAMAGE, 0.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.20D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.0D)
                .add(Attributes.JUMP_STRENGTH, 0.5D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8D);
    }

    public HandEntity getHandEntity() {
        return HAND_ENTITY;
    }

    public ResourceLocation getIcon() {
        return DEFAULT_ICON;
    }

    @Override
    public void tick() {
        super.tick();
        speedometer.tick();
        timer.tick();
    }


    @Override
    public boolean hurt(DamageSource damageSource, float damage) {
        float finalDamage = getDamageAfterAbsorb(damage);
        damageArmor(damageSource, damage);

        var passenger = getControllingPassenger();

        if (damageSource.is(DamageTypes.CACTUS) || (hasPassenger() && damageSource.getEntity() == passenger))
            return false;

        if (passenger != null)
            passenger.hurt(damageSource, finalDamage);

        return true;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (hasPassenger()) this.yHeadRot = this.getYRot();
    }

//    @Override
//    public boolean isInvisible() {
//        var clientPlayer = Minecraft.getInstance().player;
//        var pov = Minecraft.getInstance().options.getCameraType();
//
//        if (hasPassenger(clientPlayer) && pov == CameraType.FIRST_PERSON)
//            return true;
//        return super.isInvisible();
//    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        if (super.isInvulnerableTo(damageSource))
            return true;
        else
            return damageSource.getEntity() == getControllingPassenger();
    }

    @Override
    public InteractionResult interactAt(Player player, Vec3 vector, InteractionHand hand) {
        ChassisCore.LOGGER.log(DEBUG, level().isClientSide);

        if (isServerSide && !player.isPassenger()) {
            if (player.isShiftKeyDown()) {
                openGUI(player);
                return InteractionResult.SUCCESS;
            } else if (!isVehicle()) {
                this.ride(player);
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        if (!this.getPassengers().isEmpty() && this.getPassengers().get(0) instanceof LivingEntity livingEntity)
            return livingEntity;
        return null;
    }

    @Override
    public void positionRider(Entity entity, Entity.MoveFunction pCallback) {
        super.positionRider(entity, pCallback);

        var passenger = getControllingPassenger();
        if (passenger == null) return;

        var yOffset = passenger.isShiftKeyDown() ? 1.2f : 1.0f;
        var posY = getY() + getPassengersRidingOffset() + entity.getMyRidingOffset() - yOffset;
        entity.setPos(getX(), posY, getZ());

        if (entity instanceof LivingEntity livingEntity)
            livingEntity.yBodyRot = yBodyRot;
    }

    @Override
    public void travel(@NotNull Vec3 travelVector) {
        if (!isAlive()) return;
        if (isVehicle() && hasPassenger())
            travelWithPassenger(travelVector);
        else {
//            this.flyingSpeed = 0.02F;
            super.travel(travelVector);
        }
    }

    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity p_20123_) {
        return super.getDismountLocationForPassenger(p_20123_);
    }

    @Override
    public void checkDespawn() {}

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean dismountsUnderwater() {
        return false;
    }

//    @Override
//    public boolean rideableUnderWater() {
//        return true;
//    }

    @Override
    protected float tickHeadTurn(float pYRot, float pAnimStep) {
        if (hasPassenger())
            return super.tickHeadTurn(pYRot, pAnimStep);
        return pAnimStep;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Nullable
    public ChassisEquipment getEquipmentItem(String part) {
        var stack = getEquipment(part);
        if (!stack.isEmpty())
            return (ChassisEquipment) stack.getItem();
        return null;
    }

    public void openGUI(Player player) {
        Global.referenceMob = this;

        var provider = getMenuProvider();

        if (isServerSide && provider != null) {
            player.openMenu(getMenuProvider());
        }
    }

    @Nullable
    protected abstract MenuProvider getMenuProvider();

    @Nullable
    protected abstract MenuProvider getStantionMenuProvider();

    public void openStationGUI(Player player) {
        Global.referenceMob = this;
        var provider = getStantionMenuProvider();

        if (isServerSide && provider != null) {
            player.openMenu(provider);
        }
    }

    public Boolean isWalking() {
        if (!hasPassenger())
            return false;

        var entity = getControllingPassenger();
        return entity.xxa != 0.0 || entity.zza != 0.0;
    }

    public boolean hasPlayerPassenger() {
        return getControllingPassenger() instanceof Player;

    }

    public Player getPlayerPassenger() {
        if (getControllingPassenger() instanceof Player player)
            return player;
        return null;
    }

    public boolean hasPassenger() {
        return getControllingPassenger() instanceof LivingEntity;
    }

    public ItemStack getPassengerItem(EquipmentSlot slot) {
        return hasPassenger() ? getControllingPassenger().getItemBySlot(slot) : ItemStack.EMPTY;
    }

    public void jump() {
        jumpScale = 1.0F;
    }

    public void ride(LivingEntity entity) {
        entity.setYRot(getYRot());
        entity.setXRot(getXRot());
        entity.startRiding(this);
    }

    private float getDamageAfterAbsorb(float damage) {
        updateTotalArmor();
        return CombatRules.getDamageAfterAbsorb(damage, totalDefense, totalToughness);
    }

    private boolean isJumping() {
        return this.isJumping;
    }

    private double getCustomJump() {
        return this.getAttributeValue(Attributes.JUMP_STRENGTH);
    }

//    @Override
//    public boolean causeFallDamage(float height, float p_225503_2_, @NotNull DamageSource damageSource) {
//        pushEntitiesAround();
//
//        int immune = 5;
//        int damage = this.calculateFallDamage(height, p_225503_2_) / 2;
//        if (damage <= immune) {
//            return false;
//        } else {
//            if (this.isVehicle()) {
//                for(Entity entity : getIndirectPassengers()) {
//                    entity.hurt(DamageSource.FALL, damage - immune);
//                }
//            }
//            animateHurt();
//            playBlockFallSound();
//            return true;

//        }
//    }

    private void travelWithPassenger(Vec3 travelVector) {
        var entity = getControllingPassenger();
        if(entity == null) return;
        setRotationMatchingPassenger(entity);

//        if(entity.yya > 0)
//            jumpScale = 1.0F;

        if (jumpScale > 0.0F && !isJumping() && onGround())
            jump(entity);

//        this.flyingSpeed = getSpeed() * 0.1F;

        if (isControlledByLocalInstance())
            super.travel(new Vec3(entity.xxa, travelVector.y, entity.zza));
        else setDeltaMovement(Vec3.ZERO);

        if (onGround()) {
            jumpScale = 0.0F;
            isJumping = false;
        }
    }

    private void jump(LivingEntity entity) {
        var jump = getCustomJump() * jumpScale * getBlockJumpFactor();
        setDeltaMovement(getDeltaMovement().x, jump, getDeltaMovement().z);
        isJumping = true;
        hasImpulse = true;

        if (entity.zza > 0.0F) {
            float x = sin(getYRot() * ROTATION);
            float z = cos(getYRot() * ROTATION);
            setDeltaMovement(getDeltaMovement().add(
                    -0.4F * x * jumpScale,
                    0.0D,
                    0.4F * z * jumpScale));
        }

        jumpScale = 0.0F;
    }

    private void setRotationMatchingPassenger(LivingEntity livingEntity) {
        this.yRotO = getYRot();
        this.setYRot(livingEntity.getYRot());
        this.setXRot(livingEntity.getXRot() * 0.5F);
        this.setRot(getYRot(), getXRot());
    }
}