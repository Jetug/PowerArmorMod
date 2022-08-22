package com.jetug.power_armor_mod.common.minecraft.entity;

import com.google.common.collect.ImmutableList;
import com.jetug.power_armor_mod.common.capability.data.ArmorDataProvider;
import com.jetug.power_armor_mod.common.capability.data.IArmorPartData;
import com.jetug.power_armor_mod.common.capability.data.IPlayerData;
import com.jetug.power_armor_mod.common.util.enums.BodyPart;
import com.jetug.power_armor_mod.common.util.enums.EquipmentType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;

import static com.jetug.power_armor_mod.common.capability.data.DataManager.getPlayerData;
import static com.jetug.power_armor_mod.common.util.enums.BodyPart.*;

public class PowerArmorEntity extends CreatureEntity implements IAnimatable, IJumpingMount, IPowerArmor {
    private final AnimationFactory factory = new AnimationFactory(this);

    protected boolean isJumping;
    protected float playerJumpPendingScale;
    public final PowerArmorPartEntity headHitBox;
    public final PowerArmorPartEntity bodyHitBox;
    public final PowerArmorPartEntity leftArmHitBox;
    public final PowerArmorPartEntity rightArmHitBox;
    public final PowerArmorPartEntity leftLegHitBox;
    public final PowerArmorPartEntity rightLegHitBox;
    public final PowerArmorPartEntity[] subEntities;

    public final ArmorSlot head      = new ArmorSlot(this, HEAD      , EquipmentType.STANDARD);
    public final ArmorSlot body      = new ArmorSlot(this, BODY      , EquipmentType.STANDARD);
    public final ArmorSlot leftArm   = new ArmorSlot(this, LEFT_ARM  , EquipmentType.STANDARD);
    public final ArmorSlot rightArm  = new ArmorSlot(this, RIGHT_ARM , EquipmentType.STANDARD);
    public final ArmorSlot leftLeg   = new ArmorSlot(this, LEFT_LEG  , EquipmentType.STANDARD);
    public final ArmorSlot rightLeg  = new ArmorSlot(this, RIGHT_LEG , EquipmentType.STANDARD);
    public final ArmorSlot[] armorParts = new ArmorSlot[]{head, body, leftArm, rightArm, leftLeg, rightLeg};

    public PowerArmorEntity(EntityType<? extends CreatureEntity> type, World worldIn) {
        super(type, worldIn);
        headHitBox = new PowerArmorPartEntity(this, HEAD, 0.6f, 0.6f);
        bodyHitBox = new PowerArmorPartEntity(this, BODY, 0.7f, 1.0f);
        leftArmHitBox = new PowerArmorPartEntity(this, LEFT_ARM, 0.5f, 1.0f);
        rightArmHitBox = new PowerArmorPartEntity(this, RIGHT_ARM, 0.5f, 1.0f);
        leftLegHitBox = new PowerArmorPartEntity(this, LEFT_LEG, 0.6f, 1.0f);
        rightLegHitBox = new PowerArmorPartEntity(this, RIGHT_LEG, 0.6f, 1.0f);
        subEntities = new PowerArmorPartEntity[]{headHitBox, bodyHitBox, leftArmHitBox, rightArmHitBox, leftLegHitBox, rightLegHitBox};
        this.noCulling = true;
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return CreatureEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.ATTACK_DAMAGE, 0.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.20D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.0D)
                .add(Attributes.JUMP_STRENGTH, 2.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8D);
    }

    public ArmorSlot getArmorPart(BodyPart part) {
        switch (part) {
            case HEAD:
                return head;
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

    public float getArmorDurability(BodyPart bodyPart) {
        IArmorPartData cap = this.getCapability(ArmorDataProvider.POWER_ARMOR_PART_DATA).orElse(null);
        cap.syncFromServer();
        return cap.getDurability(bodyPart);
    }

    public void setArmorDurability(BodyPart bodyPart, float value) {
        if (level.isClientSide) {
            IArmorPartData cap = this.getCapability(ArmorDataProvider.POWER_ARMOR_PART_DATA).orElse(null);
            cap.setDurability(bodyPart, value);
            if (level.isClientSide)
                cap.syncWithServer();
        }
    }

    public void damageArmor(BodyPart bodyPart, float damage) {
        float durability = getArmorDurability(bodyPart) - damage;
        if (durability < 0)
            durability = 0;

        setArmorDurability(bodyPart, durability);
    }

    public boolean hurt(PowerArmorPartEntity part, DamageSource damageSource, float damage) {
        if (damage < 0.01F) {
            return false;
        } else {
            return true;
        }
    }


    public boolean isJumping() {
        return this.isJumping;
    }

    public void setIsJumping(boolean p_110255_1_) {
        this.isJumping = p_110255_1_;
    }

    public int posPointer = -1;
    public final double[][] positions = new double[64][3];

    public double[] getLatencyPos(int p_70974_1_, float p_70974_2_) {
        if (this.isDeadOrDying()) {
            p_70974_2_ = 0.0F;
        }

        p_70974_2_ = 1.0F - p_70974_2_;
        int i = this.posPointer - p_70974_1_ & 63;
        int j = this.posPointer - p_70974_1_ - 1 & 63;
        double[] adouble = new double[3];
        double d0 = this.positions[i][1];
        double d1 = this.positions[j][1] - d0;
        adouble[1] = d0 + d1 * (double)p_70974_2_;

        return adouble;
    }

    @Override
    public void aiStep() {
        super.aiStep();

        Vector3d[] aVector3d = new Vector3d[this.subEntities.length];
        for (int j = 0; j < this.subEntities.length; ++j) {
            aVector3d[j] = new Vector3d(this.subEntities[j].getX(), this.subEntities[j].getY(), this.subEntities[j].getZ());
        }

        float rotation = this.yRot * ((float) Math.PI / 180F);
        float xPos = MathHelper.cos(rotation);
        float zPos = MathHelper.sin(rotation);
        float armPos = 0.8f;
        float legPos = 0.2f;
//EnderDragonEntity

        float rotation2 = (float)(this.getLatencyPos(2, 1.0F)[1] - this.getLatencyPos(2, 1.0F)[1]) * 2.0F * ((float)Math.PI / 180F);
        float xPos2 = MathHelper.cos(rotation2);
        float zPos2 = MathHelper.sin(rotation2);

        float f4  = MathHelper.sin(this.yRot * ((float)Math.PI / 180F) - this.yRot * 0.01F );
        float f19 = MathHelper.cos(this.yRot * ((float)Math.PI / 180F) - this.yRot * 0.01F);

        //this.tickPart(this.headHitBox, (f4 * 0.5F * xPos2), 2.1, (-f19 * 0.5F * xPos2)); //


        float posX = MathHelper.sin(this.yBodyRot * ((float) Math.PI / 180F));
        float posZ = MathHelper.cos(this.yBodyRot * ((float) Math.PI / 180F));

        double posXZ = 0.2;

        this.headHitBox.setPos(this.getX() + (posXZ * posX), this.getY() + 2.1, -this.getZ() - (posXZ * posZ));
        this.bodyHitBox.setPos(this.getX() + (posXZ * posX), this.getY() + 1.2, this.getZ() - (posXZ * posZ));

        this.tickPart(this.headHitBox, (posXZ * posX), 2.1, - (posXZ * posZ));
        this.tickPart(this.bodyHitBox, (posXZ * posX), 1.2, - (posXZ * posZ));
        this.tickPart(this.rightArmHitBox, xPos * -armPos, 1.1, zPos * -armPos);
        this.tickPart(this.leftArmHitBox , xPos * armPos , 1.1, zPos * armPos);
        this.tickPart(this.rightLegHitBox, xPos * -legPos, 0, zPos * -legPos);
        this.tickPart(this.leftLegHitBox , xPos * legPos , 0, zPos * legPos);

        for (int l = 0; l < this.subEntities.length; ++l) {
            this.subEntities[l].xo = aVector3d[l].x;
            this.subEntities[l].yo = aVector3d[l].y;
            this.subEntities[l].zo = aVector3d[l].z;
            this.subEntities[l].xOld = aVector3d[l].x;
            this.subEntities[l].yOld = aVector3d[l].y;
            this.subEntities[l].zOld = aVector3d[l].z;
        }
    }

    private void tickPart(PowerArmorPartEntity part, double x, double y, double z) {
        part.setPos(this.getX() + x, this.getY() + y, this.getZ() + z);
    }

    //Settings
    //{
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

        if (hasPassenger(clientPlayer) && pov == PointOfView.FIRST_PERSON)
            return true;
        return super.isInvisible();
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        if (super.isInvulnerableTo(damageSource)) {
            return true;
        } else if (damageSource.getEntity() == getControllingPassenger()) {
            return true;
        }
        return false;
    }


    public ActionResultType onInteract(PlayerEntity player, Hand hand) {
        for (ArmorSlot subEntity : this.armorParts) {
            subEntity.setDurability(1);
        }
        this.doPlayerRide(player);

        return ActionResultType.sidedSuccess(this.level.isClientSide);
    }

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

        float posX = MathHelper.sin(this.yBodyRot * ((float) Math.PI / 180F));
        float posZ = MathHelper.cos(this.yBodyRot * ((float) Math.PI / 180F));

        double posXZ = -0.1;
        double posY = 0.9;
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

                    //ForgeHooks.onLivingJump(this);

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

                //this.calculateEntityAnimation(this, false);
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
    public ActionResultType mobInteract(PlayerEntity player, Hand p_230254_2_) {
        this.doPlayerRide(player);
        return ActionResultType.sidedSuccess(this.level.isClientSide);
    }

    @Override
    public void onPlayerJump(int jump) {
        if (jump < 0) {
            jump = 0;
        }

        if (jump >= 90) {
            this.playerJumpPendingScale = 1.0F;
        } else {
            this.playerJumpPendingScale = 0.4F + 0.4F * (float)jump / 90.0F;
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

    public void doPlayerRide(PlayerEntity player) {
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