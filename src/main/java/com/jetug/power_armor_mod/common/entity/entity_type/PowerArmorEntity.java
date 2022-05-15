package com.jetug.power_armor_mod.common.entity.entity_type;

import com.jetug.power_armor_mod.common.entity.data.IPlayerData;
import com.jetug.power_armor_mod.common.util.enums.BodyPart;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
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

import static com.jetug.power_armor_mod.common.entity.data.DataManager.getPlayerData;
import static com.jetug.power_armor_mod.common.util.constants.Attributes.*;

public class PowerArmorEntity extends CreatureEntity implements IAnimatable, IJumpingMount
{
    private AnimationFactory factory = new AnimationFactory(this);

    protected boolean isJumping;
    protected float playerJumpPendingScale;

    private final PowerArmorPartEntity[] subEntities;
    public final PowerArmorPartEntity head;
    public final PowerArmorPartEntity body;
    public final PowerArmorPartEntity leftArm;
    public final PowerArmorPartEntity rightArm;
    public final PowerArmorPartEntity leftLeg;
    public final PowerArmorPartEntity rightLeg;

    public PowerArmorEntity(EntityType<? extends CreatureEntity> type, World worldIn) {
        super(type, worldIn);
        head   =   new PowerArmorPartEntity(this, BodyPart.HEAD, 0.7f, 0.7f);
        body   =   new PowerArmorPartEntity(this, BodyPart.BODY, 1.0f, 1.0f);
        leftArm  = new PowerArmorPartEntity(this, BodyPart.LEFT_ARM, 0.7f, 1.0f);
        rightArm = new PowerArmorPartEntity(this, BodyPart.RIGHT_ARM, 0.7f, 1.0f);
        leftLeg  = new PowerArmorPartEntity(this, BodyPart.LEFT_LEG, 0.6f, 1.0f);
        rightLeg = new PowerArmorPartEntity(this, BodyPart.RIGHT_LEG, 0.6f, 1.0f);
        subEntities = new PowerArmorPartEntity[]{head, body, leftArm, rightArm, leftLeg, rightLeg};

//       GeoBone headBone = powerArmorModel.getBoneAP(HEAD_BONE_NAME);
//       PlayerEntity player = Minecraft.getInstance().player;
//       if(headBone == null)
//           player.sendMessage(new StringTextComponent("null"), this.getUUID());
//       else
//           player.sendMessage(new StringTextComponent("not null"), this.getUUID());

        this.noCulling = true;
    }

//    private void setupPowerArmorParts(PowerArmorPartEntity[] parts){
//        for(PowerArmorPartEntity part: parts){
//            part
//        }
//    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return CreatureEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 1000.0D)
                .add(Attributes.ATTACK_DAMAGE, 0.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.20D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.0D)
                .add(Attributes.JUMP_STRENGTH, 2.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
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
    public boolean isMultipartEntity() {
        return true;
    }

    @Override
    public boolean isPickable() {
        return false;
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

        this.tickPart(this.head, 0, 2.1,0);
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
    public boolean isInvisible() {
        ClientPlayerEntity clientPlayer = Minecraft.getInstance().player;
        PointOfView pov = Minecraft.getInstance().options.getCameraType();

        if(hasPassenger(clientPlayer) && pov == PointOfView.FIRST_PERSON)
            return true;
        return super.isInvisible();
    }

    public ActionResultType onInteract(PlayerEntity player, Hand hand) {
        this.doPlayerRide(player);
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
            player.sendMessage(new StringTextComponent("message"), this.getUUID());
        }
    }
}