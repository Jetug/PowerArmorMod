package com.jetug.power_armor_mod.common.minecraft.entity;

import com.jetug.power_armor_mod.common.capability.data.IPlayerData;
import com.jetug.power_armor_mod.common.util.enums.BodyPart;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;

import static com.jetug.power_armor_mod.common.capability.data.DataManager.getPlayerData;

public class HitboxTestEntity extends AnimalEntity implements IAnimatable, IJumpingMount, IPowerArmor, IRideable, IEquipable   {
    public HitboxTestEntity(EntityType<? extends AnimalEntity> type, World world) {
        super(type, world);
    }

    private static final DataParameter<Boolean> DATA_SADDLE_ID = EntityDataManager.defineId(PigEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> DATA_BOOST_TIME = EntityDataManager.defineId(PigEntity.class, DataSerializers.INT);
    private static final Ingredient FOOD_ITEMS = Ingredient.of(Items.CARROT, Items.POTATO, Items.BEETROOT);
    private final BoostHelper steering = new BoostHelper(this.entityData, DATA_BOOST_TIME, DATA_SADDLE_ID);

//    public static AttributeModifierMap.MutableAttribute createAttributes() {
//        return CreatureEntity.createMobAttributes()
//                .add(Attributes.MAX_HEALTH, 10.0D)
//                .add(Attributes.MOVEMENT_SPEED, 0.20D);
//    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(3, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.2D, Ingredient.of(Items.CARROT_ON_A_STICK), false));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.2D, false, FOOD_ITEMS));
        this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.1D));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MobEntity.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0D).add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    @Nullable
    public Entity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
    }

    public boolean canBeControlledByRider() {
        Entity entity = this.getControllingPassenger();
        if (!(entity instanceof PlayerEntity)) {
            return false;
        } else {
            PlayerEntity playerentity = (PlayerEntity)entity;
            return playerentity.getMainHandItem().getItem() == Items.CARROT_ON_A_STICK || playerentity.getOffhandItem().getItem() == Items.CARROT_ON_A_STICK;
        }
    }

    public void onSyncedDataUpdated(DataParameter<?> p_184206_1_) {
        if (DATA_BOOST_TIME.equals(p_184206_1_) && this.level.isClientSide) {
            this.steering.onSynced();
        }

        super.onSyncedDataUpdated(p_184206_1_);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_SADDLE_ID, false);
        this.entityData.define(DATA_BOOST_TIME, 0);
    }

    public void addAdditionalSaveData(CompoundNBT p_213281_1_) {
        super.addAdditionalSaveData(p_213281_1_);
        this.steering.addAdditionalSaveData(p_213281_1_);
    }

    public void readAdditionalSaveData(CompoundNBT p_70037_1_) {
        super.readAdditionalSaveData(p_70037_1_);
        this.steering.readAdditionalSaveData(p_70037_1_);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.PIG_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.PIG_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.PIG_DEATH;
    }

    protected void playStepSound(BlockPos p_180429_1_, BlockState p_180429_2_) {
        this.playSound(SoundEvents.PIG_STEP, 0.15F, 1.0F);
    }

    public ActionResultType mobInteract(PlayerEntity p_230254_1_, Hand p_230254_2_) {
        boolean flag = this.isFood(p_230254_1_.getItemInHand(p_230254_2_));
        if (!flag && this.isSaddled() && !this.isVehicle() && !p_230254_1_.isSecondaryUseActive()) {
            if (!this.level.isClientSide) {
                p_230254_1_.startRiding(this);
            }

            return ActionResultType.sidedSuccess(this.level.isClientSide);
        } else {
            ActionResultType actionresulttype = super.mobInteract(p_230254_1_, p_230254_2_);
            if (!actionresulttype.consumesAction()) {
                ItemStack itemstack = p_230254_1_.getItemInHand(p_230254_2_);
                return itemstack.getItem() == Items.SADDLE ? itemstack.interactLivingEntity(p_230254_1_, this, p_230254_2_) : ActionResultType.PASS;
            } else {
                return actionresulttype;
            }
        }
    }

    public boolean isSaddleable() {
        return this.isAlive() && !this.isBaby();
    }

    protected void dropEquipment() {
        super.dropEquipment();
        if (this.isSaddled()) {
            this.spawnAtLocation(Items.SADDLE);
        }

    }

    public boolean isSaddled() {
        return this.steering.hasSaddle();
    }

    public void equipSaddle(@Nullable SoundCategory p_230266_1_) {
        this.steering.setSaddle(true);
        if (p_230266_1_ != null) {
            this.level.playSound((PlayerEntity)null, this, SoundEvents.PIG_SADDLE, p_230266_1_, 0.5F, 1.0F);
        }

    }

    public Vector3d getDismountLocationForPassenger(LivingEntity p_230268_1_) {
        Direction direction = this.getMotionDirection();
        if (direction.getAxis() == Direction.Axis.Y) {
            return super.getDismountLocationForPassenger(p_230268_1_);
        } else {
            int[][] aint = TransportationHelper.offsetsForDirection(direction);
            BlockPos blockpos = this.blockPosition();
            BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

            for(Pose pose : p_230268_1_.getDismountPoses()) {
                AxisAlignedBB axisalignedbb = p_230268_1_.getLocalBoundsForPose(pose);

                for(int[] aint1 : aint) {
                    blockpos$mutable.set(blockpos.getX() + aint1[0], blockpos.getY(), blockpos.getZ() + aint1[1]);
                    double d0 = this.level.getBlockFloorHeight(blockpos$mutable);
                    if (TransportationHelper.isBlockFloorValid(d0)) {
                        Vector3d vector3d = Vector3d.upFromBottomCenterOf(blockpos$mutable, d0);
                        if (TransportationHelper.canDismountTo(this.level, p_230268_1_, axisalignedbb.move(vector3d))) {
                            p_230268_1_.setPose(pose);
                            return vector3d;
                        }
                    }
                }
            }

            return super.getDismountLocationForPassenger(p_230268_1_);
        }
    }

    public void thunderHit(ServerWorld p_241841_1_, LightningBoltEntity p_241841_2_) {
        if (p_241841_1_.getDifficulty() != Difficulty.PEACEFUL && net.minecraftforge.event.ForgeEventFactory.canLivingConvert(this, EntityType.ZOMBIFIED_PIGLIN, (timer) -> {})) {
            ZombifiedPiglinEntity zombifiedpiglinentity = EntityType.ZOMBIFIED_PIGLIN.create(p_241841_1_);
            zombifiedpiglinentity.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
            zombifiedpiglinentity.moveTo(this.getX(), this.getY(), this.getZ(), this.yRot, this.xRot);
            zombifiedpiglinentity.setNoAi(this.isNoAi());
            zombifiedpiglinentity.setBaby(this.isBaby());
            if (this.hasCustomName()) {
                zombifiedpiglinentity.setCustomName(this.getCustomName());
                zombifiedpiglinentity.setCustomNameVisible(this.isCustomNameVisible());
            }

            zombifiedpiglinentity.setPersistenceRequired();
            net.minecraftforge.event.ForgeEventFactory.onLivingConvert(this, zombifiedpiglinentity);
            p_241841_1_.addFreshEntity(zombifiedpiglinentity);
            this.remove();
        } else {
            super.thunderHit(p_241841_1_, p_241841_2_);
        }

    }

    public void travel(Vector3d p_213352_1_) {
        this.travel(this, this.steering, p_213352_1_);
    }

    public float getSteeringSpeed() {
        return (float)this.getAttributeValue(Attributes.MOVEMENT_SPEED) * 0.225F;
    }

    public void travelWithInput(Vector3d p_230267_1_) {
        super.travel(p_230267_1_);
    }

    public boolean boost() {
        return this.steering.boost(this.getRandom());
    }

    public PigEntity getBreedOffspring(ServerWorld p_241840_1_, AgeableEntity p_241840_2_) {
        return EntityType.PIG.create(p_241840_1_);
    }

    public boolean isFood(ItemStack p_70877_1_) {
        return FOOD_ITEMS.test(p_70877_1_);
    }

    @OnlyIn(Dist.CLIENT)
    public Vector3d getLeashOffset() {
        return new Vector3d(0.0D, (double)(0.6F * this.getEyeHeight()), (double)(this.getBbWidth() * 0.4F));
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

//    @Override
//    public ActionResultType mobInteract(PlayerEntity player, Hand p_230254_2_) {
//        this.doPlayerRide(player);
//        return ActionResultType.sidedSuccess(this.level.isClientSide);
//    }

    @Override
    public boolean isInvisible() {
        ClientPlayerEntity clientPlayer = Minecraft.getInstance().player;
        PointOfView pov = Minecraft.getInstance().options.getCameraType();

        if (hasPassenger(clientPlayer) && pov == PointOfView.FIRST_PERSON)
            return true;
        return super.isInvisible();
    }

    @Override
    public float getArmorDurability(BodyPart bodyPart) {
        return 0;
    }

    @Override
    public void setArmorDurability(BodyPart bodyPart, float value) {

    }

    @Override
    public void damageArmor(BodyPart bodyPart, float damage) {

    }

    @Override
    public void onPlayerJump(int p_110206_1_) {

    }

    @Override
    public boolean canJump() {
        return true;
    }

    @Override
    public void handleStartJump(int p_184775_1_) {

    }

    @Override
    public void handleStopJump() {

    }

    private final AnimationFactory factory = new AnimationFactory(this);

    @Override
    public void registerControllers(AnimationData data) {
        AnimationController<HitboxTestEntity> controller = new AnimationController<>(this, "controller", 0, this::predicate);
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

}
